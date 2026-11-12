package cn.richinfo.dm.a;

import android.content.Context;
import android.text.TextUtils;
import cn.richinfo.dm.DMConfig;
import cn.richinfo.dm.DMSDK;
import cn.richinfo.dm.constance.PrivateConstance;
import cn.richinfo.dm.constance.PublicConstance;
import cn.richinfo.dm.db.DbManager;
import cn.richinfo.dm.net.encyptrequest.EncryptJsonRequest;
import cn.richinfo.dm.util.BusinessUtils;
import cn.richinfo.dm.util.DMDeviceInfoCollection;
import cn.richinfo.dm.util.LogUtils;
import cn.richinfo.dm.util.MobileUtil;
import cn.richinfo.dm.util.SPUtils;
import cn.richinfo.dm.util.VolleyUtil;
import com.dmyk.android.telephony.DmykAbsTelephonyManager;
import java.net.URLEncoder;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class z {

    /* renamed from: a, reason: collision with root package name */
    private static z f48a;
    private Context b;

    public interface a {
        void a();

        void a(JSONObject jSONObject);
    }

    private z(Context context) {
        this.b = context;
    }

    private int a(JSONObject jSONObject, String str) {
        StringBuilder sb;
        String str2;
        if (jSONObject.has(str)) {
            try {
                int i = jSONObject.getInt(str);
                if (i > 0) {
                    return i;
                }
            } catch (Exception unused) {
            }
            sb = new StringBuilder();
            str2 = "解析systemParams异常，未获取有效配置，key：";
        } else {
            sb = new StringBuilder();
            str2 = "解析systemParams异常，无此key：";
        }
        LogUtils.warn(PublicConstance.LOG_TAG_CFG, sb.append(str2).append(str).toString());
        return 0;
    }

    public static synchronized z a(Context context) {
        z zVar;
        synchronized (z.class) {
            if (f48a == null) {
                f48a = new z(context);
            }
            zVar = f48a;
        }
        return zVar;
    }

    private String a() {
        String str = System.currentTimeMillis() + "";
        String str2 = DMSDK.getBaseConfigUrl(this.b) + "&brand=" + a(DMDeviceInfoCollection.getBrand(this.b)) + "&model=" + a(DMDeviceInfoCollection.getModel(this.b)) + "&mac=" + a(DMDeviceInfoCollection.macAddress(this.b)) + "&imei=" + a(DMDeviceInfoCollection.imei1(this.b)) + "&imei2=" + a(DMDeviceInfoCollection.imei2(this.b)) + "&version=" + a(DMConfig.PROTOCAL_VERSION) + "&sdkver=" + a(DMConfig.SDK_VERSION) + "&dynamicVer=" + a(SPUtils.getDMPatchVersion(this.b)) + "&action=" + a("filter") + "&timestr=" + a(str) + "&token=" + a(MobileUtil.getVerifyMD5(this.b, str)) + "&appkey=" + a(MobileUtil.getAppkey(this.b));
        LogUtils.debug(PublicConstance.LOG_TAG_CFG, "url=" + str2);
        return str2;
    }

    private String a(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception unused) {
            return null;
        }
    }

    private JSONObject a(String str, JSONObject jSONObject, JSONObject jSONObject2) {
        try {
            JSONObject jSONObject3 = jSONObject.has(str) ? jSONObject.getJSONObject(str) : new JSONObject();
            Iterator<String> keys = jSONObject2.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    JSONObject jSONObject4 = jSONObject2.getJSONObject(next);
                    String string = jSONObject4.getString(PrivateConstance.CFG_UUID);
                    if (jSONObject3.length() != 0 && jSONObject3.has(next) && TextUtils.equals(string, jSONObject3.getString(next))) {
                        LogUtils.warn(PublicConstance.LOG_TAG_CFG, str + "的开关<type=" + next + "><uuid=" + string + ">与上一次配置一致，本次配置不生效");
                    } else {
                        jSONObject3.put(next, string);
                        DmykAbsTelephonyManager.SwitchConfigInfo handleSwitchCfg = BusinessUtils.handleSwitchCfg(str, next, jSONObject4);
                        if (handleSwitchCfg != null) {
                            DMDeviceInfoCollection.setSwitch(this.b, handleSwitchCfg);
                        }
                    }
                } catch (Exception e) {
                    LogUtils.err(PublicConstance.LOG_TAG_CFG, str + "的开关配置异常：" + e.getMessage());
                }
            }
            return jSONObject3;
        } catch (Exception e2) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "解析开关配置列表异常：" + e2.getMessage());
            return new JSONObject();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(JSONObject jSONObject) {
        f(jSONObject);
        c(jSONObject);
        d(jSONObject);
        e(jSONObject);
        b(jSONObject);
    }

    private void a(JSONObject jSONObject, long j) {
        try {
            String string = jSONObject.getString("sleepSwitch");
            SPUtils.saveSleepSwitch(this.b, string);
            if (!BusinessUtils.switchCheck(string)) {
                SPUtils.saveSleepStartTime(this.b, 0L);
                SPUtils.saveSleepEndTime(this.b, 0L);
            } else {
                long j2 = jSONObject.has("startTime") ? jSONObject.getLong("startTime") : 0L;
                int i = jSONObject.has("sleepTime") ? jSONObject.getInt("sleepTime") : 0;
                SPUtils.saveSleepStartTime(this.b, j2);
                SPUtils.saveSleepEndTime(this.b, (i * j) + j2);
            }
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "解析休眠配置异常：" + e.getMessage());
        }
    }

    private void b(JSONObject jSONObject) {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_CFG, "解析异常诊断配置");
            if (jSONObject.has(PrivateConstance.CFG_TYPE_LOG)) {
                JSONObject jSONObject2 = jSONObject.getJSONObject(PrivateConstance.CFG_TYPE_LOG);
                if (jSONObject2.has(PrivateConstance.CFG_PARAM_PARAM)) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(PrivateConstance.CFG_PARAM_PARAM);
                    long j = jSONObject3.getLong("logStartTime");
                    long j2 = jSONObject3.getLong("logEndTime");
                    SPUtils.saveLogStartTime(this.b, j);
                    SPUtils.saveLogEndTime(this.b, j2);
                    try {
                        JSONObject jSONObject4 = new JSONObject();
                        jSONObject4.put("type", PrivateConstance.CFG_TYPE_LOG);
                        jSONObject4.put("logStartTime", j);
                        jSONObject4.put("logEndTime", j2);
                        LogUtils.noticeCfgChange(jSONObject4);
                    } catch (JSONException e) {
                        LogUtils.reportErr(PublicConstance.LOG_TAG_CFG, "异常诊断埋点异常", e);
                    }
                }
            }
        } catch (Exception e2) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "异常诊断配置解析异常：" + e2);
        }
    }

    private void c(JSONObject jSONObject) {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_CFG, "解析开关配置");
            if (jSONObject.has(PrivateConstance.CFG_TYPE_SWITCH)) {
                JSONObject jSONObject2 = jSONObject.getJSONObject(PrivateConstance.CFG_TYPE_SWITCH);
                if (jSONObject2.length() == 0) {
                    LogUtils.warn(PublicConstance.LOG_TAG_CFG, "开关列表为空，不做配置");
                    return;
                }
                String switchUid = SPUtils.getSwitchUid(this.b);
                JSONObject jSONObject3 = TextUtils.isEmpty(switchUid) ? new JSONObject() : new JSONObject(switchUid);
                String imei1 = DMDeviceInfoCollection.imei1(this.b);
                String imei2 = DMDeviceInfoCollection.imei2(this.b);
                if (jSONObject2.has(imei1)) {
                    jSONObject3.put(imei1, a(imei1, jSONObject3, jSONObject2.getJSONObject(imei1)));
                }
                if (jSONObject2.has(imei2)) {
                    jSONObject3.put(imei2, a(imei2, jSONObject3, jSONObject2.getJSONObject(imei2)));
                }
                SPUtils.saveSwitchUid(this.b, jSONObject3.toString());
            }
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "开关列表配置异常：" + e);
        }
    }

    private void d(JSONObject jSONObject) {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_CFG, "解析系统参数配置");
            if (jSONObject.has(PrivateConstance.CFG_TYPE_SYS)) {
                JSONObject jSONObject2 = jSONObject.getJSONObject(PrivateConstance.CFG_TYPE_SYS);
                if (jSONObject2.has(PrivateConstance.CFG_PARAM_PARAM)) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(PrivateConstance.CFG_PARAM_PARAM);
                    long j = DMConfig.DEFAULT_TIME_UNIT;
                    if (jSONObject3.has("interval")) {
                        j = jSONObject3.getLong("interval");
                        LogUtils.info(PublicConstance.LOG_TAG_CFG, "平台配置周期单位：" + j);
                        if (j < 60000) {
                            j = DMConfig.DEFAULT_TIME_UNIT;
                            LogUtils.info(PublicConstance.LOG_TAG_CFG, "周期异常，强制改为:" + j);
                        }
                    }
                    int a2 = a(jSONObject3, "networkInterval");
                    if (a2 > 0) {
                        SPUtils.saveNetworkInterval(this.b, a2 * j);
                    }
                    int a3 = a(jSONObject3, "keepAliveRate");
                    if (a3 > 0) {
                        SPUtils.saveHeartbeatInterval(this.b, a3 * j);
                    }
                    int a4 = a(jSONObject3, "appRate");
                    if (a4 > 0) {
                        SPUtils.saveAppHeartbeatInterval(this.b, a4 * j);
                    }
                    LogUtils.info(PublicConstance.LOG_TAG_CFG, "解析周期参数完成，重置各个计时器");
                    BusinessUtils.checkAlarms(this.b);
                    if (jSONObject3.has("sleepSwitch")) {
                        a(jSONObject3, j);
                    }
                    int a5 = a(jSONObject3, "autoRegTime");
                    if (a5 > 0) {
                        SPUtils.saveRetryInterval(this.b, a5 * 60000);
                    }
                    int a6 = a(jSONObject3, "autoRegRetryCount");
                    if (a6 > 0) {
                        SPUtils.saveMaxRetryCount(this.b, a6);
                    }
                    int a7 = a(jSONObject3, "regRate");
                    int a8 = a(jSONObject3, "regReportMax");
                    if (a7 > 0 && a8 > 0) {
                        String str = a8 + "#" + (a7 * j);
                        LogUtils.debug(PublicConstance.LOG_TAG_CFG, "saveMaxReportConfig:" + str);
                        SPUtils.saveMaxReportConfig(this.b, str);
                    }
                    float a9 = a(jSONObject3, "appDataLifeTime");
                    if (a9 > 0.0f) {
                        SPUtils.saveAppDataLifetime(this.b, ((long) a9) * DMConfig.HOUR);
                    }
                    float a10 = a(jSONObject3, "flowDataLifeTime");
                    if (a10 > 0.0f) {
                        SPUtils.saveFlowDataLifetime(this.b, ((long) a10) * DMConfig.HOUR);
                    }
                    float a11 = a(jSONObject3, "netDataLifeTime");
                    if (a11 > 0.0f) {
                        SPUtils.saveNetDataLifetime(this.b, ((long) a11) * DMConfig.HOUR);
                    }
                    float a12 = a(jSONObject3, "bluetoothDataLifeTime");
                    if (a12 > 0.0f) {
                        SPUtils.saveBluetoothDataLifetime(this.b, ((long) a12) * DMConfig.HOUR);
                    }
                    float a13 = a(jSONObject3, "batteryDataLifeTime");
                    if (a13 > 0.0f) {
                        SPUtils.saveBatteryDataLifetime(this.b, ((long) a13) * DMConfig.HOUR);
                    }
                    float a14 = a(jSONObject3, "wifiDataLiftTime");
                    if (a14 > 0.0f) {
                        SPUtils.saveWifiLifetime(this.b, ((long) a14) * DMConfig.HOUR);
                    }
                    int a15 = a(jSONObject3, "ipRate");
                    if (a15 > 0) {
                        SPUtils.saveIpRate(this.b, a15);
                    }
                    int a16 = a(jSONObject3, "ipReqMax");
                    if (a16 > 0) {
                        SPUtils.saveIpMaxCount(this.b, a16);
                    }
                    if (jSONObject3.has("validity")) {
                        SPUtils.saveIpValidity(this.b, jSONObject3.getInt("validity"));
                    }
                    if (jSONObject3.has("isSdkUpdate")) {
                        SPUtils.saveDynamicLoadSwitch(this.b, jSONObject3.getString("isSdkUpdate"));
                    }
                    if (jSONObject3.has("switch5g")) {
                        SPUtils.save5GReportSwitch(this.b, jSONObject3.getString("switch5g"));
                    }
                    if (jSONObject3.has("flowSwitch")) {
                        SPUtils.saveTimeRelatedTrafficSwitch(this.b, jSONObject3.getString("flowSwitch"));
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "SDK参数配置异常：" + e);
        }
    }

    private void e(JSONObject jSONObject) {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_CFG, "解析平台地址配置");
            if (jSONObject.has(PrivateConstance.CFG_TYPE_URL)) {
                JSONObject jSONObject2 = jSONObject.getJSONObject(PrivateConstance.CFG_TYPE_URL);
                if (jSONObject2.has(PrivateConstance.CFG_PARAM_PARAM)) {
                    String string = jSONObject2.getJSONObject(PrivateConstance.CFG_PARAM_PARAM).getString("baseUrl");
                    if (TextUtils.isEmpty(string)) {
                        return;
                    }
                    LogUtils.info(PublicConstance.LOG_TAG_CFG, "平台配置BaseURL=" + string);
                    SPUtils.saveBaseAcceptUrl(this.b, string);
                    SPUtils.saveBaseConfigUrl(this.b, string);
                    try {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("type", PrivateConstance.CFG_TYPE_URL);
                        jSONObject3.put("baseUrl", string);
                        LogUtils.noticeCfgChange(jSONObject3);
                    } catch (JSONException e) {
                        LogUtils.reportErr(PublicConstance.LOG_TAG_CFG, "切换地址埋点异常", e);
                    }
                }
            }
        } catch (Exception e2) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "服务器地址配置解析异常：" + e2);
        }
    }

    private void f(JSONObject jSONObject) {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_CFG, "解析白名单配置");
            if (!jSONObject.has(PrivateConstance.CFG_TYPE_WHITE_LIST)) {
                LogUtils.debug(PublicConstance.LOG_TAG_CFG, "参数异常，没有：whitelistconfig");
                return;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject(PrivateConstance.CFG_TYPE_WHITE_LIST);
            if (jSONObject2.has(PrivateConstance.CFG_PARAM_PARAM)) {
                JSONObject jSONObject3 = jSONObject2.getJSONObject(PrivateConstance.CFG_PARAM_PARAM);
                if (jSONObject3.has("keepAlive")) {
                    SPUtils.saveHeartbeatSwitch(this.b, jSONObject3.getString("keepAlive"));
                }
                if (jSONObject3.has("logReport")) {
                    SPUtils.saveUploadLogSwitch(this.b, jSONObject3.getString("logReport"));
                }
                if (jSONObject3.has("appInfo")) {
                    SPUtils.saveAppReportSwitch(this.b, jSONObject3.getString("appInfo"));
                }
                if (jSONObject3.has("businessInfo")) {
                    SPUtils.saveBusinessInfoSwitch(this.b, jSONObject3.getString("businessInfo"));
                }
                if (jSONObject3.has("photoReg")) {
                    SPUtils.saveSnapshotReportSwitch(this.b, jSONObject3.getString("photoReg"));
                }
                if (jSONObject3.has(PrivateConstance.CFG_TYPE_NET)) {
                    String string = jSONObject3.getString(PrivateConstance.CFG_TYPE_NET);
                    SPUtils.saveNetworkReportSwitch(this.b, string);
                    if (!BusinessUtils.switchCheck(string)) {
                        DbManager.getInstance(this.b).clearNetworkTable();
                        LogUtils.info(PublicConstance.LOG_TAG_CFG, "平台关闭网络采集开关，已删除本地缓存");
                    }
                }
                if (jSONObject3.has("internationalRoaming")) {
                    SPUtils.saveRoamingReportSwitch(this.b, jSONObject3.getString("internationalRoaming"));
                }
                if (jSONObject3.has("bluetooth")) {
                    String string2 = jSONObject3.getString("bluetooth");
                    SPUtils.saveBluetoothReportSwitch(this.b, string2);
                    if (!BusinessUtils.switchCheck(string2)) {
                        DbManager.getInstance(this.b).clearBluetoothLogTable();
                        LogUtils.info(PublicConstance.LOG_TAG_CFG, "平台关闭蓝牙采集开关，已删除本地缓存");
                    }
                }
                if (jSONObject3.has("batteryCharging")) {
                    String string3 = jSONObject3.getString("batteryCharging");
                    SPUtils.saveBatteryChargingReportSwitch(this.b, string3);
                    if (!BusinessUtils.switchCheck(string3)) {
                        DbManager.getInstance(this.b).clearBatteryChargeTable();
                        LogUtils.info(PublicConstance.LOG_TAG_CFG, "平台关闭充电采集开关，已删除本地缓存");
                    }
                }
                if (jSONObject3.has("preInstalledApp")) {
                    SPUtils.savePreInstallReportSwitch(this.b, jSONObject3.getString("preInstalledApp"));
                }
                if (jSONObject3.has("wifi")) {
                    String string4 = jSONObject3.getString("wifi");
                    SPUtils.saveWifiListReportSwitch(this.b, string4);
                    if (!BusinessUtils.switchCheck(string4)) {
                        DbManager.getInstance(this.b).clearWifiTable();
                        LogUtils.info(PublicConstance.LOG_TAG_CFG, "平台关闭wifi采集开关，已删除本地缓存");
                    }
                }
                if (jSONObject3.has(PrivateConstance.CFG_TYPE_FLOW)) {
                    SPUtils.saveTimeRelatedTrafficSwitch(this.b, jSONObject3.getString(PrivateConstance.CFG_TYPE_FLOW));
                }
            }
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_CFG, "解析白名单异常：" + e);
        }
    }

    public synchronized void a(a aVar) {
        if (!BusinessUtils.switchCheck(SPUtils.getHeartbeatSwitch(this.b))) {
            LogUtils.info(PublicConstance.LOG_TAG_CFG, "清空本地配置");
            SPUtils.saveSdkConfig(this.b, "");
        }
        String sdkConfig = SPUtils.getSdkConfig(this.b);
        LogUtils.info(PublicConstance.LOG_TAG_CFG, "本地配置信息为：" + sdkConfig);
        if (TextUtils.isEmpty(sdkConfig)) {
            b(aVar);
        } else {
            try {
                aVar.a(new JSONObject(sdkConfig));
            } catch (Exception unused) {
                aVar.a();
            }
        }
    }

    public synchronized void b(a aVar) {
        LogUtils.info(PublicConstance.LOG_TAG_CFG, "连接服务器更新配置");
        String a2 = a();
        VolleyUtil.getQueue(this.b).add(EncryptJsonRequest.getEncryptJsonRequest(1, a2, null, new x(this, aVar), new y(this, a2, aVar)));
    }
}
