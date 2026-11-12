# SystemMessage（com.hihonor.regservice）完整行为分析

对象：`SystemMessage_15.0.0.011.apk`（设备显示名"系统消息"）
包名：`com.hihonor.regservice`
版本：versionName=15.0.0.011 / versionCode=14
安装路径：`/product_h/region_comm/china/app/HnCmccDm`（HnCmccDm = Honor + 中国移动 CMCC 定制 DM 组件；`product_h` 为区域定制分区）
首次安装时间：2018-08-08（与系统镜像同源预装）
代码构成：彩讯科技（Richinfo）DM SDK 4.0.6 + MT SDK 2.1.6/4.0.3 深度定制（cn.richinfo.* / com.richinfo.* / com.dmyk.*）

依赖条件：启动时检查"WiFi 或移动数据 + 有 SIM 卡"，否则自杀（Process.killProcess）；业务层强制检查 `checkIfHaveYDSim`（移动卡），"无移动卡，不上报"。

---

## 1. 总体架构

| 模块 | 作用 |
|---|---|
| `cn.richinfo.dm.*` | DM（Device Management）主模块：采集、上报、配置下发、快照业务、动态更新 |
| `cn.richinfo.mt.*` + `com.richinfo.mthttp.*` | MT（Mobile Terminal）模块：自注册、心跳、LWM2M（CoAP）与 HTTP 双通道 |
| `com.dmyk.android.*` | 定制 telephony 扩展（双卡 IMSI/ICCID、5G、VoLTE 等底层读取） |
| `com.hihonor.regservice.*` | 荣耀壳层：Application、SIM/VoLTE 接收器、手电筒、电池温度 |

进程与组件（从设备 dumpsys 确认）：
- Service：`cn.richinfo.dm.service.DMService`、`cn.richinfo.mt.service.MTService`、`com.richinfo.mthttp.service.MTService`、`MTAlarmJobService`、`MTHeartJobService`（心跳保活，Alarm/JobScheduler/Timer 三通道）
- Receiver：`DMBroadCastReceiver`（14 种动作）、`MTBroadCastReceiver`、`SimStateChangeReceiver`、`VolteStateReceiver`、`BatteryTempReceiver`
- Manifest 声明 `android:sharedUserId="android.uid.phone"`（与电话系统共享 uid 的高权限声明）

---

## 2. 本地存储位置（文件都存哪里）

### 2.1 SharedPreferences（均为 App 私有 `getSharedPreferences(name, 0)`）

| 文件名 | 内容 |
|---|---|
| `config_dm` | DM 配置与状态：baseAcceptUrl/baseConfigUrl（默认 `https://a.fxltsbl.com`）、心跳间隔、应用上报开关（APP_REPORT_SWITCH）、快照开关（KEY_SANPSHOT_REPORT_SWITCH）、快照请求信息（KEY_SNAPSHOT_REQUEST_INFO）、注册 MD5（KEY_REGISITER_MD5）、switchUid（开关配置快照）、各业务开关（wifi/bluetooth/battery/network/roaming/preinstall/flowcollect）、休眠段、IP/MAC 缓存 HashMap、重试计数等约 60 个 key |
| `config_mt` | MT 侧：imei（KEY_IMEI）、imsi、appId、appName HashMap、上报成功时间、心跳计数 |
| `spConfig` / `spDeviceConfig` | MT HTTP 通道配置：服务器 URL（spUrl）、规则配置 JSON（spRuleConfig，含 reportTime/reportNum/heartBeatTime/retryInterval）、上报计数（spReportCount） |

### 2.2 SQLite 数据库 `cmcc_dm.db`（DbOpenHelper，version 64）

| 表 | 内容 |
|---|---|
| `table_name_battery_charge` | 充电采集：电量、温度、充电状态、时间戳 |
| `table_name_bluetooth` | 蓝牙采集：名称、MAC、UUID、状态 |
| `table_name_network` | 网络采集：cellid1/2、lac1/2、信号强度、网络类型、上下行流量 |
| `table_of_time_related_traffic` | 分时流量：每小时移动/WiFi 收发字节（_mobile_rx/_mobile_tx/_wifi_rx/_wifi_tx） |
| `table_app_collect_list` + `app_info_*` | 应用使用采集：每 App `_useTime`（使用时长）、`_launchCount`（启动次数）、`_firstTime` |
| `table_name_wifi_list` | WiFi 列表：SSID、BSSID、信号强度、时间 |
| `appinfo_record`（MT 模块用） | 快照业务的应用记录（app_package/app_name/usage_time/launch_count/count_time） |

### 2.3 文件目录

| 路径 | 内容 |
|---|---|
| `{filesDir}/.DMLog/log_4.0.6_<日期>.log` | 内部运行日志（LogToFile2，含各业务状态） |
| `{filesDir}/dynamic/patch.jar` | 服务器下发的动态补丁包（DexClassLoader 加载） |
| `/proc/cpuinfo`、`/proc/meminfo`、`/sys/class/net/wlan0/address` | 环境读取（CPU 名、内存、MAC） |

---

## 3. 网络端点（传哪里去）

基础域：
- 生产 `https://a.fxltsbl.com`、调试 `https://b.fxltsbl.com`
- 快照域 `https://auc.fxltsbl.com`
- LWM2M (CoAP) `coap://m.fxltsbl.com:5683`（调试 `coap://shipei.fxltsbl.com:5683`）

### 3.1 DM 上报端点全表（UrlConstant.java）

| 端点 | 对应业务 |
|---|---|
| `/accept` | 基础接收 |
| `/TerminalConfigService/terminalConf?func=tsdk:termimalfullconfig` | 拉取全量配置 |
| `/TerminalConfigService/terminalConf?func=tsdk:checkupdate` | 检查 SDK 更新 |
| `/TerminalConfigService/auth?tsdk:sdkAuth` | 快照业务配置查询 |
| `/sdkService?func=tsdk:postreglog` | 自注册上报 |
| `/sdkService?func=tsdk:posthblog` | 心跳上报 |
| `/sdkService?func=tsdk:postapplylog` | 应用使用采集上报 |
| `/sdkService?func=tsdk:configlog` | 配置日志上报 |
| `/flowCollectService?func=tsdk:flowcollect` | 分时流量上报 |
| `/network?` | 网络信息上报 |
| `/wifiInfo?` | WiFi 列表上报 |
| `/bluetooth?func=tsdk:postBluetoothInfo` | 蓝牙上报 |
| `/batteryCharging?` | 充电上报 |
| `/internationalRoaming?` | 国际漫游上报 |
| `/preInstalledApp?` | 预装应用上报（一次性） |
| `/logReport?`、`/errorDiagnosis?` | 日志文件/异常诊断上报 |
| `https://a.fxltsbl.com/getIpAddr` | 获取出口 IP |
| `https://auc.fxltsbl.com/AuthCenter/datareport?func=tsdk:photoReg` | 快照业务上报（release） |
| `sdkphoto/accept?func=tsdk:photoReg` | 快照业务上报（debug） |

### 3.2 MT 端点（HttpSDK.java，POST）

| 端点 | 业务 |
|---|---|
| `/dm-multi-accept/multi/getMultiTerminalConfig` | 拉配置（configType=autoRegist / keepAlive） |
| `/dm-multi-accept/multi/acceptMultiTerminalRegInfo` | 自注册上报 |
| `/dm-multi-accept/multi/acceptMultiTerminalHeartbeatInfo` | 心跳上报 |
| `coap://m.fxltsbl.com:5683` | LWM2M 终端管理协议（Leshan 栈，端点为 AES 加密串） |

---

## 4. Token / 认证 / 加密机制（重点）

### 4.1 DM 侧请求签名（token）

每次请求拼在 URL query：

```
&appkey=<dmkey>&timestr=<毫秒时间戳>&imei=<IMEI1>&version=v2&token=<MD5(IMEI1 + timestr后8位)>
```

- token = `MobileUtil.getVerifyMD5(context, timestr)` = `MD5(MD5Encode(imei1 + getLast8(timestr)))`
  - `getLast8`：取时间戳字符串最后 8 位
  - 即 **token = MD5(IMEI1 + 时间戳后8位)**，无随机数、无 HMAC —— 已知 IMEI 即可对任意时间伪造
- appkey = `A100008208`（AndroidManifest 的 `<meta-data android:name="dmkey" android:value="A100008208"/>`，由 `MobileUtil.getAppkey()` 读取）
- 完整请求 URL 示例（z.java 配置拉取）：
  `a.fxltsbl.com/accept?brand=..&model=..&mac=..&imei=..&imei2=..&version=v2&sdkver=4.0.6&dynamicVer=..&action=filter&timestr=..&token=..&appkey=..`

### 4.2 DM 侧请求体加密

- 类：`EncryptJsonRequest.getEncryptJsonRequest()` → `AesUtil.getInstance().aesEncrypt(body)`
- 算法：`AES/ECB/PKCS5Padding`，输出 Base64
- 密钥：`libaes-jni.so`（native `keyFromJNI()`）硬编码字符串 **`134e3265829ff82daf16e7b740a600b5`**，实际 AES key = `MD5("134e3265829ff82daf16e7b740a600b5")`（16 字节，AES-128）
- 结论：密钥硬编码在 .so 中，可被离线提取 → 流量可解密、请求可伪造

### 4.3 MT 侧加密

- 类：`cn.richinfo.mt.util.AesCoder`（native `keyFromJNI()` → `libmt-jni.so`）
- 硬编码字符串 **`564e3459679cc85fgh13e7b889o125u7`**（32 字符）
- 算法：`AES/CBC/PKCS7Padding`，IV = 16 字节全 0，Base64（NO_WRAP）
- 实际 key = `SHA-256("564e3459679cc85fgh13e7b889o125u7")` → 256 位；同样可离线提取
- `encrypt2()` 构造的 LWM2M endpoint 格式：`A@#@@<AES密文>@#@<appId>@#@<imei>@#@v2.2@#@<dmId>`

### 4.4 快照业务鉴权

- `TerminalConfigService/auth?tsdk:sdkAuth&imei=..&imei2=..&brand=..&model=..&appid=..&pkgname=..&sign=<Base64(签名)>&promotionid=..&requestid=..&timestr=..&token=<同上MD5>` —— 服务端返回 authResult=N 则拒绝
- 上报附带 `&appkey=..&timestr=..&imei=..&version=v2&token=..`

### 4.5 第三方触发保护

- 广播 `cn.richinfo.dmyk.action.THIRD_REQUEST`（H5/第三方触发快照）注册在权限 `com.dm.permission.THIRD_SEND` 之下（signature 级），普通应用无法伪造

---

## 5. 采集内容全清单

### 5.1 自注册上报（RegisterInfo，53 字段）

sdkVersion、imei1/imei2、brand/model、firmwareVer、systemVer、cpu、rom/ram、type、iccid1/iccid2、imsi1/imsi2、mac、cellId(1/2)、lac(1/2)、channel、dataCard(1/2)、masterStatus、soltQuantity、soltNetwork(1/2)、inType、verify（含屏幕分辨率、传感器列表、存储/内存、电池、CPU、MAC、IMEI/IMSI/SimSerialNumber）、phoneNumber1/phoneNumber2、status5g(1/2)、networkMode5g(1/2)、smsSwitch5g(1/2)、vonrSwitch(1/2)、software/hardware700M、software/hardwareSA、netCard、voiceCard、tempValue(1/2)、carrierAggregation、foreignRoaming、voNRPlus、R16_5G(1/2)、bluetoothMac

### 5.2 周期性采集

| 业务 | 内容 | 默认周期 |
|---|---|---|
| 应用使用 | 每 App 使用时长、启动次数（UsageStatsManager） | 168h（7天）上报，时钟粒度统计 |
| 分时流量 | 移动/WiFi 上下行（NetworkStatsManager） | 随心跳 |
| 心跳 | 设备+卡+网络状态 | 24h |
| 网络/WiFi/蓝牙/充电 | 见 2.2 表 | 事件触发+周期 |
| 漫游 | 国际漫游状态 | 事件触发 |
| 预装应用 | 一次性 | 首次激活 |

服务器可通过 `systemParams`（interval、networkInterval、keepAliveRate、appRate、sleepSwitch、autoRegTime/autoRegRetryCount、regRate/regReportMax、各类 dataLifeTime）远程修改采集与上报周期；通过 `whitelistconfig` 远程开关各业务；通过 `urlConfig` 远程切换服务器地址。

### 5.3 快照业务（photoReg）

- 由外部（H5 或系统服务）广播触发（`snapshotRequest`，携带 thirdAction/currentTime/加密 info/pkg/receiverClass）
- SDK 校验白名单开关 → 采集设备+卡+应用信息（bean.l：40 字段，含 imei/iccid/imsi/phoneNumber/appId/promotionId/requestId/pkgName）→ AES 加密上报 `auc.fxltsbl.com`
- **本 APK 内不含拍照、截屏、录屏代码**（无 Camera.takePicture / MediaProjection / PixelCopy）；"拍照采集"仅为日志标签，活跃的是注册归因与信息上报

### 5.4 日志上报

- logConfig 配置时间窗口；可将本地收集的日志文件压缩后上传（C0030l/URL_LOG_FILE_REPORT）

---

## 6. 动态能力（服务器控制）

1. **动态补丁**：`O.java` 从 `checkupdate` 检查更新 → 下载 patch.jar 到 `{filesDir}/dynamic/` → MD5 校验 → `DexClassLoader` 加载 `cn.richinfo.dmpatch.business.DMController` 替换默认控制器（版本必须匹配 4.0.6 前缀）
2. **远程配置**：baseUrl 切换、全部开关、周期、休眠时段、开关配置（如"5G 开关设置项"的可见性/通知文案）

---

## 7. 关键结论

1. 这是**中国移动定制 ROM 预装的 DM 设备管理组件**（非黑色软件，但属于运营商厂家可控采集通道），主要面向移动卡用户激活。
2. 采集范围广：设备全标识（IMEI/IMSI/ICCID/MEID/MAC/手机号）、每 App 使用行为、流量、WiFi/蓝牙、充电、预装应用、传感器/屏幕。
3. 认证体系弱：token = MD5(IMEI + 时间戳后8位)，appkey 硬编码（A100008208）；加密密钥硬编码于两个 .so（`134e3265829ff82daf16e7b740a600b5` / `564e3459679cc85fgh13e7b889o125u7`）→ 可离线解密/伪造。
4. 具备服务端远程改周期/开关/服务器地址与动态加载代码能力。
5. 快照业务（photoReg）触发端在本 APK 之外，本 APK 无影像采集代码。

## 证据文件索引

- `reports2/x_MobileUtil.java` — token 生成（getVerifyMD5）
- `reports2/x_AesUtil.java` / `x_AesCoder.java` — 两套 AES 实现
- `reports2/x_DMService.java` / `x_DMController.java` — 主业务调度
- `reports2/x_RegisterInfo.java` — 自注册 53 字段
- `reports2/x_DMDeviceInfoCollection.java` — 设备信息采集
- `reports2/x_bean_l.java` — 快照字段
- `reports2/x_dm_a_z.java` — 配置下发解析
- `reports2/x_dm_a_O.java` — 动态补丁加载
- `reports2/x_db*` / `x_SPUtils.java` — 存储结构
- `reports2/aes_jni_strings.txt` / `mt_jni_full.txt` — 硬编码密钥证据
- `reports2/urls_by_class.txt` — 端点与类对应
