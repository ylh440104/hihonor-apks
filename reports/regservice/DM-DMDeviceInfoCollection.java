package cn.richinfo.dm.util;

import android.content.Context;
import android.text.TextUtils;
import com.dmyk.android.telephony.DmykAbsTelephonyManager;
import com.hihonor.regservice.MtUtils;
import com.richinfo.mthttp.Constant;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes.dex */
public class DMDeviceInfoCollection {
    private static final String TAG = "控制功能";
    private static String cacheBrand = null;
    private static String cacheImei1 = null;
    private static String cacheImei2 = null;
    private static String cacheModel = null;
    private static int cacheSlot = -1;
    private static String cacheType;

    public static String batteryCapacity(Context context) {
        return DmykPhoneInfo.getInstance(context).getBatteryCapacity();
    }

    public static String batteryCapacityCurr(Context context) {
        return DmykPhoneInfo.getInstance(context).getBatteryCapacityCurr();
    }

    public static String bluetoothMac(Context context) {
        return DmykPhoneInfo.getInstance(context).getBluetoothMac();
    }

    public static String cellId1(Context context) {
        return DmykPhoneInfo.getInstance(context).getCellId(0) + "";
    }

    public static String cellId2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getCellId(1) + "";
    }

    private static boolean checkIsSingle(Context context) {
        if (cacheSlot < 0) {
            cacheSlot = DmykPhoneInfo.getInstance(context).getPhoneCount();
        }
        return cacheSlot == 1;
    }

    public static String cpu(Context context) {
        return DmykPhoneInfo.getInstance(context).getCPUModel();
    }

    public static String dataCard(Context context) {
        return DmykPhoneInfo.getInstance(context).getDataState(0) + "";
    }

    public static String dataCard2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getDataState(1) + "";
    }

    public static String firmwareVer(Context context) {
        return DmykPhoneInfo.getInstance(context).getDeviceSoftwareVersion();
    }

    public static String get5GState1(Context context) {
        return DmykPhoneInfo.getInstance(context).get5GState(0) + "";
    }

    public static String get5GState2(Context context) {
        return checkIsSingle(context) ? MtUtils.NOT_SUPPORTED : DmykPhoneInfo.getInstance(context).get5GState(1) + "";
    }

    public static String get5gNetworkMode1(Context context) {
        return DmykPhoneInfo.getInstance(context).get5gNetworkMode(0) + "";
    }

    public static String get5gNetworkMode2(Context context) {
        return checkIsSingle(context) ? MtUtils.NOT_SUPPORTED : DmykPhoneInfo.getInstance(context).get5gNetworkMode(1) + "";
    }

    public static String getBatteryLevel(Context context) {
        return DmykPhoneInfo.getInstance(context).getBatteryLevel();
    }

    public static String getBatteryTemp(Context context) {
        return DmykPhoneInfo.getInstance(context).getBatteryTemp();
    }

    public static ArrayList<DmykAbsTelephonyManager.BluetoothInfo> getBluetoothInfo(Context context) {
        return DmykPhoneInfo.getInstance(context).getBluetoothInfo();
    }

    public static String getBrand(Context context) {
        if (TextUtils.isEmpty(cacheBrand)) {
            cacheBrand = DmykPhoneInfo.getInstance(context).getBrand();
        }
        return cacheBrand;
    }

    public static DmykAbsTelephonyManager.CellInfo getCellInfo1(Context context) {
        return DmykPhoneInfo.getInstance(context).getCellInfo(0);
    }

    public static DmykAbsTelephonyManager.CellInfo getCellInfo2(Context context) {
        if (!checkIsSingle(context)) {
            return DmykPhoneInfo.getInstance(context).getCellInfo(1);
        }
        DmykAbsTelephonyManager.CellInfo cellInfo = new DmykAbsTelephonyManager.CellInfo();
        cellInfo.erafcn4G = "";
        cellInfo.erafcn5G = "";
        cellInfo.band = "";
        cellInfo.rsrp4G = "";
        cellInfo.rsrp5G = "";
        cellInfo.rsrq4G = "";
        cellInfo.rsrq5G = "";
        cellInfo.sinr4G = "";
        cellInfo.sinr5G = "";
        cellInfo.operator = "";
        return cellInfo;
    }

    public static String getCountry(Context context) {
        return DmykPhoneInfo.getInstance(context).getCountry();
    }

    public static String getCurrentRam(Context context) {
        return DmykPhoneInfo.getInstance(context).getCurrentRam();
    }

    public static String getCurrentRom(Context context) {
        return DmykPhoneInfo.getInstance(context).getCurrentRom();
    }

    public static String getMobileNetType(Context context) {
        if (BusinessUtils.isWifi(context)) {
            return "18";
        }
        int netCard = DmykPhoneInfo.getInstance(context).getNetCard();
        return (netCard == 0 || netCard == 1) ? DmykPhoneInfo.getInstance(context).getNetworkType(netCard) + "" : Constant.SP_MODEL;
    }

    public static String getMobileSignalIntensity1(Context context) {
        return DmykPhoneInfo.getInstance(context).getMobileSignalIntensity(0) + "dBm";
    }

    public static String getMobileSignalIntensity2(Context context) {
        return checkIsSingle(context) ? "0dBm" : DmykPhoneInfo.getInstance(context).getMobileSignalIntensity(1) + "dBm";
    }

    public static String getModel(Context context) {
        if (TextUtils.isEmpty(cacheModel)) {
            cacheModel = DmykPhoneInfo.getInstance(context).getModel();
        }
        return cacheModel;
    }

    public static String getWLANState(Context context) {
        return DmykPhoneInfo.getInstance(context).getWLANState() + "";
    }

    public static DmykAbsTelephonyManager.WifiInfo getWifiInfo(Context context) {
        return DmykPhoneInfo.getInstance(context).getWifiInfo();
    }

    public static ArrayList<DmykAbsTelephonyManager.WifiInfo> getWifiList(Context context) {
        return DmykPhoneInfo.getInstance(context).getWifiList();
    }

    public static String hardware700M(Context context) {
        return DmykPhoneInfo.getInstance(context).get700M(2) + "";
    }

    public static String hardwareSA(Context context) {
        return DmykPhoneInfo.getInstance(context).getSA(2) + "";
    }

    public static String iccid1(Context context) {
        return DmykPhoneInfo.getInstance(context).getIccId(0);
    }

    public static String iccid2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getIccId(1);
    }

    public static String imei1(Context context) {
        if (TextUtils.isEmpty(cacheImei1)) {
            cacheImei1 = DmykPhoneInfo.getInstance(context).getGsmDeviceId(0);
        }
        return cacheImei1;
    }

    public static String imei2(Context context) {
        if (TextUtils.isEmpty(cacheImei2)) {
            cacheImei2 = checkIsSingle(context) ? Constant.SP_MODEL : DmykPhoneInfo.getInstance(context).getGsmDeviceId(1);
        }
        return cacheImei2;
    }

    public static String imsi1(Context context) {
        return DmykPhoneInfo.getInstance(context).getSubscriberId(0);
    }

    public static String imsi2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getSubscriberId(1);
    }

    public static String inType(Context context) {
        return DmykPhoneInfo.getInstance(context).getCTAModel();
    }

    public static String lac1(Context context) {
        return DmykPhoneInfo.getInstance(context).getLac(0) + "";
    }

    public static String lac2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getLac(1) + "";
    }

    public static String macAddress(Context context) {
        return DmykPhoneInfo.getInstance(context).getMacAddress();
    }

    public static String masterStatus(Context context) {
        return DmykPhoneInfo.getInstance(context).getMasterPhoneId() + "";
    }

    public static String netCard(Context context) {
        return DmykPhoneInfo.getInstance(context).getNetCard() + "";
    }

    public static String phoneNumber1(Context context) {
        return DmykPhoneInfo.getInstance(context).getPhoneNumber(0) + "";
    }

    public static String phoneNumber2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getPhoneNumber(1);
    }

    public static String ram(Context context) {
        return DmykPhoneInfo.getInstance(context).getRamStorageSize();
    }

    public static String rom(Context context) {
        return DmykPhoneInfo.getInstance(context).getRomStorageSize();
    }

    public static void setSwitch(Context context, DmykAbsTelephonyManager.SwitchConfigInfo switchConfigInfo) {
        if (switchConfigInfo == null) {
            LogUtils.err("控制功能", "调用开关配置接口异常，传入开关信息为空");
        } else {
            LogUtils.info("控制功能", String.format(new Locale("UTF-8"), "调用接口，配置<%s>开关:开关状态:%s,是否可见:%s,通知内容:%s", switchConfigInfo.type, switchConfigInfo.isOpen, switchConfigInfo.isShow, switchConfigInfo.notice));
            DmykPhoneInfo.getInstance(context).setSwitch(switchConfigInfo);
        }
    }

    public static String smsSwitch5g1(Context context) {
        return DmykPhoneInfo.getInstance(context).get5gMessageSwitch(0) + "";
    }

    public static String smsSwitch5g2(Context context) {
        return checkIsSingle(context) ? MtUtils.NOT_SUPPORTED : DmykPhoneInfo.getInstance(context).get5gMessageSwitch(1) + "";
    }

    public static String software700M(Context context) {
        return DmykPhoneInfo.getInstance(context).get700M(1) + "";
    }

    public static String softwareSA(Context context) {
        return DmykPhoneInfo.getInstance(context).getSA(1) + "";
    }

    public static String soltNetwork1(Context context) {
        return DmykPhoneInfo.getInstance(context).getNetworkType(0) + "";
    }

    public static String soltNetwork2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getNetworkType(1) + "";
    }

    public static String soltQuantity(Context context) {
        if (cacheSlot < 0) {
            cacheSlot = DmykPhoneInfo.getInstance(context).getPhoneCount();
        }
        return cacheSlot + "";
    }

    public static String soltService1(Context context) {
        return DmykPhoneInfo.getInstance(context).getSimState(0) + "";
    }

    public static String soltService2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).getSimState(1) + "";
    }

    public static String supportCarrierAggregation(Context context) {
        return DmykPhoneInfo.getInstance(context).isSupportCarrierAggregation();
    }

    public static String supportForeignRoaming(Context context) {
        return DmykPhoneInfo.getInstance(context).isSupportForeignRoaming() + "";
    }

    public static String supportR165G1(Context context) {
        return DmykPhoneInfo.getInstance(context).isSupportR165G(0) + "";
    }

    public static String supportR165G2(Context context) {
        return checkIsSingle(context) ? "" : DmykPhoneInfo.getInstance(context).isSupportR165G(1) + "";
    }

    public static String supportVoNRPlus(Context context) {
        return DmykPhoneInfo.getInstance(context).isSupportVoNRPlus() + "";
    }

    public static String systemVer(Context context) {
        return DmykPhoneInfo.getInstance(context).getOSVersion();
    }

    public static String tempValue1(Context context) {
        return DmykPhoneInfo.getInstance(context).getTempValue1(0);
    }

    public static String tempValue2(Context context) {
        return DmykPhoneInfo.getInstance(context).getTempValue2(0);
    }

    public static String totalFlowD(Context context, long j, long j2) {
        StringBuilder append;
        String str;
        long mobileTotalBytes = DmykPhoneInfo.getInstance(context).getMobileTotalBytes(0, j, j2);
        if (mobileTotalBytes > 0) {
            append = new StringBuilder().append(mobileTotalBytes);
            str = "B";
        } else {
            append = new StringBuilder().append(mobileTotalBytes);
            str = "";
        }
        return append.append(str).toString();
    }

    public static String totalFlowD2(Context context, long j, long j2) {
        StringBuilder append;
        String str;
        if (checkIsSingle(context)) {
            return "0B";
        }
        long mobileTotalBytes = DmykPhoneInfo.getInstance(context).getMobileTotalBytes(1, j, j2);
        if (mobileTotalBytes > 0) {
            append = new StringBuilder().append(mobileTotalBytes);
            str = "B";
        } else {
            append = new StringBuilder().append(mobileTotalBytes);
            str = "";
        }
        return append.append(str).toString();
    }

    public static String totalFlowW(Context context, long j, long j2) {
        StringBuilder append;
        String str;
        long wiFiTotalBytes = DmykPhoneInfo.getInstance(context).getWiFiTotalBytes(j, j2);
        if (wiFiTotalBytes >= 0) {
            append = new StringBuilder().append(wiFiTotalBytes);
            str = "B";
        } else {
            append = new StringBuilder().append(wiFiTotalBytes);
            str = "";
        }
        return append.append(str).toString();
    }

    public static String type(Context context) {
        if (TextUtils.isEmpty(cacheType)) {
            cacheType = DmykPhoneInfo.getInstance(context).getDeviceType() + "";
        }
        return cacheType;
    }

    public static String voiceCard(Context context) {
        return DmykPhoneInfo.getInstance(context).getVoiceCard() + "";
    }

    public static int volte(Context context) {
        return DmykPhoneInfo.getInstance(context).getVoLTEState(0);
    }

    public static int volte2(Context context) {
        if (checkIsSingle(context)) {
            return -1;
        }
        return DmykPhoneInfo.getInstance(context).getVoLTEState(1);
    }

    public static String vonrSwitch1(Context context) {
        return DmykPhoneInfo.getInstance(context).getVoNRSwitch(0) + "";
    }

    public static String vonrSwitch2(Context context) {
        return checkIsSingle(context) ? MtUtils.NOT_SUPPORTED : DmykPhoneInfo.getInstance(context).getVoNRSwitch(1) + "";
    }
}
