# 分析报告：荣耀设备 HiView / 运营商预装组件

设备：荣耀 ALT-AN00（Android 14 / MagicOS，安全补丁 2026-05）
分析对象：从设备提取的 5 个系统 APK
分析方式：GitHub Actions 全量反编译（jadx 1.5.1 全量 Java + apktool 2.9.3 + raw unzip），产出在 `decompile-out` artifact 中

## APK 清单与校验

| 文件 | 包名 | 版本 | SHA256 |
|---|---|---|---|
| AutoRegSms_15.1.0.100.apk | com.hihonor.android.AutoRegSms | 15.1.0.100 | f8c9b1a1ad096a19...e23ccd |
| com.hihonor.hiviewpush.common.HiViewPushApplication_15.0.0.034.apk | com.hihonor.hiviewpush | 15.0.0.034 | d0b272ad3ca14fa1...a68d20 |
| hiview_15.0.0.036.apk | com.hihonor.hiview | 15.0.0.036 | 7299409854910c73...299d9e2 |
| SystemMessage_15.0.0.011.apk | com.hihonor.regservice | 15.0.0.011 | 162b87b4e4a3a8e3...a3d3c58 |
| HiViewTunnel_15.0.0.034.apk | com.hihonor.idaptunnel | 15.0.0.034 | 3efc56560efd8003...6d6f498b |

## 1. SystemMessage（com.hihonor.regservice）— 运营商 DM/MT 客户端，最重要的一个

这是彩讯科技（Richinfo, `cn.richinfo.*`）为运营商提供的 DM（Device Management）/MT SDK 在荣耀上的落地，包名 `com.hihonor.regservice`。

上报端点（`cn/richinfo/dm/net/UrlConstant.java`、`PublicConstance.java`）：

- 基础域 `https://a.fxltsbl.com`（生产）/ `https://b.fxltsbl.com`（调试）
- `https://auc.fxltsbl.com/AuthCenter/datareport?func=tsdk:photoReg`（快照/拍照业务上报）
- `coap://m.fxltsbl.com:5683`（LWM2M）
- （fxltsbl.com 即近期"中国移动 RegService"曝光事件中被扒出的上报域名，解析到 CMNET 网段）

上报功能开关列表（`func=tsdk:*`）：
`postreglog` 自注册、`posthblog` 心跳、`postapplylog` 应用采集、`flowcollect` 流量采集、`photoReg` 拍照/快照业务、`postBluetoothInfo` 蓝牙采集、`termimalfullconfig` 配置下发、`checkupdate` 更新、`configlog`、`errorDiagnosis`、`logReport`、`preInstalledApp` 预装应用、`wifiInfo` Wifi 采集、`network`、`batteryCharging`、`internationalRoaming`、`getIpAddr`。

上报内容（来源 `PublicConstance.java` 的 LOG_TAG 分类）：
应用采集、充电采集、蓝牙采集、心跳采集、国际漫游、网络采集、预装应用、自注册、拍照采集、流量采集、Wifi 采集、文件上传、异常诊断。

隐私相关行为：

- 读取 IMEI1/IMEI2/IMSI/ICCID、双卡 subscriberId、号码（`getLine1NumberFromImpu`）
- 本地数据库 `appinfo_record` 记录每个 App 的 **使用时长（usage_time）+ 启动次数（launch_count）**，定时汇总上报（`cn/richinfo/mt/c/b.java`）
- "快照/拍照"业务：`snapshotRequest` 广播触发，数据经 AES 加密上报到 `auc.fxltsbl.com`（`cn/richinfo/dm/a/D.java`）。**注意：代码中未发现 Camera.takePicture / MediaProjection / ScreenCapture 的调用，拍照实现不在本 APK 内（另一处 H5 侧或系统侧），本 APK 只负责请求转发与上报。**
- 数据经 AES 加密 + RSA 保护密钥后上报

电信相关判断：`BusinessUtils.isYdSim()` 判断"移动卡"后启用对应业务；`com.dmyk.android.telephony` 命名空间属于运营商定制栈。整体是运营商定制的设备管理组件，非恶意软件，但其"系统消息"的伪装名和应用使用/流量/设备标识采集值得知情。

## 2. AutoRegSms（com.hihonor.android.AutoRegSms）— 运营商自动注册短信

- 功能：CDMA（电信）设备的 **OTA 自动注册**。开机、换卡、IMS 状态变化（`BOOT_COMPLETED` / `ACTION_HW_SWITCH_SLOT_DONE` / `IMS_SERVICE_STATE_CHANGED`）时，自动发送**加密数据短信** 到注册服务器号码完成设备注册：
  - 默认目标号码 `10659401`，也支持系统属性 `autoreg_destination_num` 覆盖（`e.z()`）
  - 发送失败重试最多 3 次，间隔 10 分钟（`h.java`）
  - 含 **卫星通信** 分支（`SatelliteAutoRegService`，`SATELLITE_COMMUNICATION` 权限）：卫星卡模式下经卫星链路做自动注册
- 权限：SEND_SMS / RECEIVE_SMS / READ_SMS / MODIFY_PHONE_STATE / SATELLITE_COMMUNICATION
- 结论：运营商（电信）CDMA/卫星 OTA 注册机制，属于正常运营商行为，不是隐蔽扣费/恶意短信。但它是"会自己发短信的预装应用"，普通用户无感知。

## 3. hiview（com.hihonor.hiview）— HiView 系统诊断与用户体验数据采集框架

荣耀自带的诊断/日志/用户体验改进框架，52 个权限（含后台定位、`PACKAGE_USAGE_STATS`、`READ_PRIVILEGED_PHONE_STATE`、`QUERY_ALL_PACKAGE`、`REBOOT` 等）。

采集内容（`UeHiEventKeys.java` 定义的事件 ID 可见）：已安装应用列表、应用使用统计、应用安装/卸载/更新事件、使用时长（小时粒度）、内存、内外存储、亮度、字体、语言、输入法、WiFi 状态、网络名、开关机事件、传感器（光线）、root 状态、用户套餐切换、性能数据等。

相关服务：
- `LogCollectService` / `LogCollectReceiver`（日志采集）
- `NcDftService`（DFT 诊断）、`DiagKitService`（系统诊断）
- Dubai 服务（`HwDubaiService`）：蓝牙/WiFi 功耗统计、调制解调器日志抓取（`BUFFERED_LOG_MAGIC_FETCH_MODEM_LOG` 等魔数分类：设置、手机、包版本、modem 日志、用户体验、WiFi/BT 芯片数据）
- 上传通道：`logservice*.dt.hihonorcloud.com`、`configserver-*.platform.hihonorcloud.com`（荣耀云日志服务）
- 日志文件名以 DeviceId（SN/UUID）命名（`MetricFilePackager`），敏感日志用随机 UUID 替代 SN（`FileUploadManager`）

结论：荣耀官方的日志与用户体验大数据采集框架（相当于华为 HiView 的延续），默认自助日志与统计上传通道。属厂商诊断系统，非第三方软件。

## 4. HiViewTunnel（com.hihonor.idaptunnel）— 日志上传隧道

`com.hihonor.idaptunnel`（IDAP Tunnel）：日志专用上传组件。

- 核心类：`LogUploadService`、`UploadStateMachine` / `HttpAuthStateMachine`（带认证的上传状态机）、`HttpsStorageServerConn`（HTTPS 存储服务器连接）、`AndroidCaTrustManager`
- 从 GRS 配置服务获取上传域名（`com/hihonor/framework/network/grs/*`），上报设备 SN（`DeviceInfo.getSn()` → `Build.getSerial()`）、版本、型号
- 有 `ScreenOffReceiver`（息屏时机触发）
- 结论：荣耀日志上传隧道（配合 hiview 使用），本身职责单一：把日志加密/压缩后经认证 HTTPS 上传到荣耀服务器。权限较低（仅网络等基础权限）。

## 5. com.hihonor.hiviewpush（HiViewPushApplication）— HiView 推送 SDK

- `PushManagerService` + `com.hihonor.push.sdk`（荣耀推送 SDK）：为 hiview 提供推送 token、接收服务端推送（`PushReceiver`）
- 定义了 signatureOrSystem 级权限 `com.hihonor.hiviewpush.hihonor.permission.PROCESS_PUSH_MSG`
- 结论：标准推送通道组件，用于服务端向 HiView 下发指令/配置。无异常采集行为。

## 关键结论摘要

1. **SystemMessage 是这批里隐私面最大的**：`com.hihonor.regservice`（运营商 DM 组件，彩讯 SDK）——采集设备标识、双卡信息、每 App 使用时长/启动次数、流量，上报到 `fxltsbl.com`（与近期"移动 RegService"话题同域）；另有"快照/拍照"业务位（photoReg），但拍照本体不在本 APK 中。
2. **AutoRegSms 会自主发短信**：向 `10659401`（可被系统属性改写）发送加密注册短信，这是电信 CDMA/卫星 OTA 注册设计，不是隐秘恶意行为，但确实无感。
3. **hiview + HiViewTunnel + HiViewPush 构成完整采集-上传链路**：hiview 采集（含应用使用统计、定位、传感器），Tunnel 负责上传到 hihonorcloud，Push 提供下推通道。属荣耀官方诊断体系。
4. 与"SystemHelper"风波的区别：本批组件均为荣耀/运营商官方组件（签名系统级），其中 regservice（SystemMessage）的功能与近日曝光的 SystemHelper 类组件有交集（设备/应用/流量采集上报第三方域），但并未发现录音、录屏、读取聊天记录的证据。

## 复现方式

- 工作流：`.github/workflows/decompile.yml`（workflow_dispatch 或 push 触发）
- 产物：`decompile-out` artifact（jadx 全量 Java + apktool + raw unpacked，约 19 MB 压缩 / 106 MB 解压）
- 本地脚本示例：`jadx -d out --no-res apks/xxx.apk && apktool d -f -o out2 apks/xxx.apk`

---
*本报告基于静态分析（反编译源码 + 清单），结论以代码证据为准；未包含动态抓包验证。*