# 勘误：应用列表 / 应用使用上报在本次样本中被硬编码关闭

对象：`SystemMessage_15.0.0.011.apk`（com.hihonor.regservice，荣耀「系统消息」）
方法：jadx + apktool smali 字节码双向核对

本文件用于修正《SystemMessage-uploaded-data-list.md》中对"应用使用列表"的描述：
**SDK 具备"全量已安装应用"的采集与上报代码，但本版本（15.0.0.011）的上报开关被荣耀壳层硬编码关闭。**

---

## 1. 采集代码：确实是"全量"（含系统应用）

`cn/richinfo/dm/a/b.smali`（jadx: `cn.richinfo.dm.a.C0020b`）：

```smali
const/16 v1, 0x2005          # MATCH_UNINSTALLED_PACKAGES|GET_ACTIVITIES|GET_SERVICES
invoke-virtual {v0, v1}, Landroid/content/pm/PackageManager;->getInstalledPackages(I)Ljava/util/List;
```

- 一次枚举全部已安装包（含系统应用；含已卸载但保留数据的包），未见系统/第三方过滤逻辑；
- 与应用使用统计（UsageStatsManager）、按 UID 流量统计（NetworkStatsManager）合并；
- 上报体（C0001a.a()）每个应用一行：应用名、包名、使用时长、启动次数、移动/WiFi 收发字节等。

## 2. 但上传开关被硬编码为关闭（关键）

`com/hihonor/regservice/DMApplication.smali`（应用进程每次启动都会执行）：

```smali
:cond_5
const/4 v0, 0x0
invoke-static {v0}, Lcn/richinfo/dm/DMSDK;->setReportApp(Z)V
```

- 全 APK 搜索 `setReportApp`：**仅此一处调用**（另有一处只是错误日志字符串）；不存在 `setReportApp(true)`；
- 该调用位于 tablet/watch 分支的提前 return 之外，普通手机必然执行。

## 3. 上报闸门 h()：双条件，必挂

`cn/richinfo/dm/a/b.smali` 方法 `h()Z`（应用上报总闸）：

1. `SPUtils.getAppReportSwitch()` 必须为 "Y"（默认 null，服务器配置默认不开）；
2. **且** `DMSDK.isReportApp()` 必须为 true —— 本版本恒为 false（见第 2 点）。

→ 所有可核实的外部入口（APPINFO 广播→DMService→onAppReport、连通性变化触发更新、报警调度 checkAlarms）均先过此闸门，故本版本**不会上传应用使用列表**。

## 4. 相关联的其它"被关"项

- **预装应用列表**：`DMApplication` 启动即 `savePreInstallReportTime(context, 1)`（预标记"已上报"），加上开关默认 "N"（双关）；
- **MT 侧 appInfo 字段**：`HnIDeviceForMt.getAppInfo()` 返回 `null`；且 MT 栈（MTSdk/mthttp）仅在平板/手表初始化。

## 5. 边界与修正口径

- 以上为**静态分析结论**；动态抓包与服务器配置属于另一层变量（含动态补丁改写 DMController 的可能性）；
- 本版该开关由**荣耀侧**在壳层关闭（是否与合规策略/定制合约有关，无法从代码判定）；
- 其它采集项（设备标识、流量、网络/基站、WiFi、蓝牙、充电等）**不受此开关影响**，仍按各自开关运行（多数默认 "Y"）；
- 若系统更新或 ROM 变体不同，请以对应 APK 重新核对（套用本方法）。

---
生成：2026-11-12 | 样本 SHA256 见 README
