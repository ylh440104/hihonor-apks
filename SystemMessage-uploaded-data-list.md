# SystemMessage（com.hihonor.regservice）完整上传内容清单

> 基于反编译源码逐字段静态分析。结论：**没有上传统"浏览记录"（浏览器历史/书签/搜索词）**，
> 最接近"行为记录"的是"每 App 使用时长/启动次数"统计。下面逐模块列全。

## 0. 先回答最关心的问题

| 数据类别 | 是否上传 | 依据（代码扫描） |
|---|---|---|
| 浏览器历史/书签/下载记录 | **否** | 全源码无 BOOKMARKS/Browser/chrome/download 等 API 调用 |
| 搜索关键词 / 网页内容 | **否** | 无 WebView 内容抓取、无输入法内容、无剪贴板（ClipboardManager 扫描为空） |
| 联系人 / 短信 / 通话记录 | **否** | 无 ContactsContract / Telephony.Sms / CallLog 调用 |
| 相册 / 照片 / 文件内容 | **否** | 无 MediaStore 读取；无拍照/录屏（无 takePicture/MediaProjection） |
| 日历 / 账号 | **否** | 无 CalendarContract / AccountManager |
| **每个 App 的使用时长+启动次数** | **是**（核心行为数据） | UsageStatsManager.queryAndAggregateUsageStats，见第 3 节 |
| 每个 App 的移动/WiFi 流量 | **是** | NetworkStatsManager.queryDetailsForUid，见第 3 节 |
| 设备标识（IMEI/IMSI/ICCID/手机号/MAC/SN） | **是** | 多处，见第 1、2 节 |
| 基站信息（CellID/LAC）、WiFi 列表、蓝牙列表 | **是** | 见第 4、5、6 节 |
| GPS 经纬度 | **未发现** | 无 getLastKnownLocation/requestLocationUpdates；仅 isProviderEnabled("gps") 读开关状态 |

## 1. 自注册上报（postreglog）—— 一次性，每次插卡/换卡

来源 `RegisterInfo.java`，53 个字段：

- 标识：sdkVersion, imei1, imei2, iccid1, iccid2, imsi1, imsi2, meid(部分版本), mac, bluetoothMac
- 设备：brand, model, firmwareVer, systemVer, cpu, rom, ram, type, soltQuantity
- 卡槽：dataCard, dataCard2, masterStatus, netCard, voiceCard, channel, inType, phoneNumber1, phoneNumber2
- 网络：cellId, cellId2, lac, lac2, soltNetwork1, soltNetwork2
- 5G/通信能力：status5g1/2, networkMode5g1/2, smsSwitch5g1/2, vonrSwitch1/2, software700M, hardware700M, softwareSA, hardwareSA, carrierAggregation, foreignRoaming, voNRPlus, R16_5G1/2, tempValue1/2
- verify 汇总串（`VerifyUtil.getVerifyInfo`）：**屏幕分辨率、全部传感器清单（加速度/陀螺仪/气压计/计步器等 21 种，Y/N）、总内存、SD 剩余空间、CPU、MAC、IMEI、IMSI、SimSerialNumber、电池、ROM 空间**

## 2. 心跳（posthblog）—— 默认 24h

- 重复携带：基站/SIM 完整性数据（imei1/2 + imsi + iccid + cellId + lac 的校验组合）
- 触发同时捎带：**分时流量、充电记录、蓝牙记录、网络信息、WiFi 列表**（见下）

## 3. 应用使用采集（postapplylog）—— 默认 7 天周期 ★最值得注意

来源 `C0020b` + `bean.a`，上传体格式（一行一个 App，`|` 分隔）：

```
pkgName|launchCount|useTime(前台时长累计)|wifiRx|wifiTx|mobileRx|mobileTx|flags|firstTime
```

- 采集方式：`UsageStatsManager.queryAndAggregateUsageStats(0, end)` → 每个包的 `getTotalTimeInForeground()` + 反射读 `mLaunchCount`
- 同时用 `NetworkStatsManager.queryDetailsForUid` 按 UID 汇总每个 App 的移动/WiFi 收发字节
- 上报体字段：imei1, imei2, imsi1, imsi2, iccid1, iccid2, brand, model, sdkVersion, sendTime, startTime, endTime, **logAppAry（全部 App 的上述行记录拼接）**、totalFlowW, totalFlowD

> 即：服务器知道每台设备上装了哪些 App、每个 App 被用了多久、打开过多少次、耗了多少流量。

## 4. 网络采集（network / flowcollect）

来源 `bean.g` + `K`：

- imsi1/2, iccid1/2, ssid, bssid, networkSignalIntensity1/2, wlanSignalIntensity, wlanSwitch
- dataCardStatus1/2, channel, timestamp, slotNetwork1/2, lac1/2, cellId1/2
- **erafcn4G/5G 频点、band、RSRP、RSRQ、SINR（4G/5G 信令级参数）**、operator1/2
- 分时流量：每小时移动/WiFi 上下行字节（`table_of_time_related_traffic` 表）
- 基站定位：`getCellLocation` → GsmCellLocation(CID/LAC) / CdmaCellLocation(SystemId/NetworkId)

## 5. WiFi 列表上报（wifiInfo）

- wifiListArray：**历史连接过的 WiFi 列表**（SSID + BSSID + 信号强度，本地库表 `table_name_wifi_list` 累积）
- imei1/2, sendTime

## 6. 蓝牙采集（bluetooth）

- 蓝牙设备名、MAC、UUID、连接时长、断开时间、状态（本地表 `table_name_bluetooth`）

## 7. 充电采集（batteryCharging）

- 充电状态、电量、电池温度、时间戳（本地表 `table_name_battery_charge`）

## 8. 国际漫游（internationalRoaming）

- 漫游状态

## 9. 预装应用（preInstalledApp）

- **开机后一次性**上报预装应用清单：appArray（包名列表）+ imei1/2 + sendTime

## 10. 快照业务（photoReg / auc.fxltsbl.com）

来源 `bean.l`（40 字段）：imei1/2, meid, brand, model, firmwareVer, systemVer, cpu, rom, ram, type, iccid1/2, imsi1/2, mac, cellId1/2, lac1/2, channel, dataCard1/2, masterStatus, soltQuantity, soltService1/2, soltNetwork1/2, inType, phoneNumber1/2, appId, appSdkVersion, promotionId, requestId, comeFrom, sendTime

- 触发方在本 APK 之外（H5/系统服务下发加密参数），本 APK 只负责打包信息并加密上传（用于营销归因类场景）

## 11. MT 侧上报（dm-multi-accept，字段可远程配置）

`Funtion.getRuleData()`：服务器下发 FieldConfigEntity 决定上报哪些字段，可选项包括：

- imei1/2, imsi/imsi2, mac, rom, ram, cpu, sysVersion, softwareVer, softwareName, volte, netType, **phoneNumber**, appKey, **bluetoothMac**, **routerMac（路由器 MAC）**, **sn, brand, model, gpu, board, 分辨率, templateId, batteryCapacity, batteryCapacityCurr, screenSize（屏幕尺寸）, networkStatus, wearingStatus（佩戴状态）, appInfo（应用列表，最长 4000 字符）**
- 全部值经 `AES_KEY="564e3459679cc85fgh13e7b889o125u7"` 加密后上传

## 12. 收集但不直接显式上传的本地数据

- `.DMLog` 运行日志、`dynamic/patch.jar`（服务器下发补丁）
- 应用使用本地库（7 天滚动）、WiFi 历史库、蓝牙库、充电库、网络库（均等上报后清理）

## 附：与"浏览记录"的准确边界

它**不读浏览器数据库、不读页面内容、不读输入内容**；它读的是 **系统级 App 使用统计（哪个 App 用了多久）** 和 **系统级网络流量统计（每个 App 耗了多少流量）**。
"你在什么时间刷了多久视频"这类行为画像可以由这两种数据推导，但**具体浏览了什么页面/搜索了什么词，本组件没有能力采集**。

---
*方法：jadx 全量反编译 + 关键词 API 扫描（浏览器/联系人/短信/通话/剪贴板/媒体/定位/日历全类目）。证据见 `reports/regservice/` 与 `reports3/`。*