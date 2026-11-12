package cn.richinfo.dm.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import cn.richinfo.dm.DMConfig;
import cn.richinfo.dm.DMSDK;
import com.android.volley.VolleyError;
import com.richinfo.mthttp.Constant;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

/* loaded from: classes.dex */
public class MobileUtil {
    private static final String TAG = "控制功能";

    public static boolean dataCheck(Context context) {
        String str;
        String soltQuantity = DMDeviceInfoCollection.soltQuantity(context);
        String imei1 = DMDeviceInfoCollection.imei1(context);
        String imsi1 = DMDeviceInfoCollection.imsi1(context);
        String iccid1 = DMDeviceInfoCollection.iccid1(context);
        if (DMConfig.DM_SNAPSHOT_FROM_SDK.equals(soltQuantity)) {
            LogUtils.info("控制功能", "数据完整性校验<slotQuantity=" + soltQuantity + ",imei1=" + LogUtils.safetyLog(imei1) + ",imsi1=" + LogUtils.safetyLog(imsi1) + ",iccid1=" + LogUtils.safetyLog(iccid1) + ">");
            if (TextUtils.isEmpty(imei1)) {
                str = "IMEI1为空";
            } else {
                if (!TextUtils.isEmpty(imsi1) || TextUtils.isEmpty(iccid1)) {
                    if ((!imsiCheck(imsi1) || !TextUtils.isEmpty(iccid1)) && (!imsiCheck(imsi1) || TextUtils.isEmpty(iccid1) || locationCheck1(context))) {
                        return true;
                    }
                    str = "卡1 IMSI或ICCID异常";
                }
                str = "卡1 IMSI或ICCID为空";
            }
        } else {
            String imei2 = DMDeviceInfoCollection.imei2(context);
            String imsi2 = DMDeviceInfoCollection.imsi2(context);
            String iccid2 = DMDeviceInfoCollection.iccid2(context);
            LogUtils.info("控制功能", "数据完整性校验<slotQuantity=" + soltQuantity + ",imei1=" + LogUtils.safetyLog(imei1) + ",imsi1=" + LogUtils.safetyLog(imsi1) + ",iccid1=" + LogUtils.safetyLog(iccid1) + ",imei2=" + LogUtils.safetyLog(imei2) + ",imsi2=" + LogUtils.safetyLog(imsi2) + ",iccid2=" + LogUtils.safetyLog(iccid2) + ">");
            if (TextUtils.isEmpty(imei1) || TextUtils.isEmpty(imei2)) {
                str = "IMEI1或IMEI2为空";
            } else {
                if (!TextUtils.isEmpty(imsi1) || TextUtils.isEmpty(iccid1)) {
                    if (!imsiCheck(imsi1) || !TextUtils.isEmpty(iccid1)) {
                        if (TextUtils.isEmpty(imsi2) && !TextUtils.isEmpty(iccid2)) {
                            str = "卡2 IMSI或ICCID为空";
                        } else if (!imsiCheck(imsi2) || !TextUtils.isEmpty(iccid2)) {
                            if (imsiCheck(imsi1) && imsiCheck(imsi2) && !locationCheck1(context) && !locationCheck2(context)) {
                                str = "卡槽1、卡槽2CellId或Lac异常";
                            } else if (imsiCheck(imsi1) && !imsiCheck(imsi2) && !locationCheck1(context)) {
                                str = "卡槽1CellId或Lac异常";
                            } else {
                                if (!imsiCheck(imsi2) || imsiCheck(imsi1) || locationCheck2(context)) {
                                    return true;
                                }
                                str = "卡槽2CellId或Lac异常";
                            }
                        }
                    }
                    str = "卡1 IMSI或ICCID异常";
                }
                str = "卡1 IMSI或ICCID为空";
            }
        }
        LogUtils.warn("控制功能", str);
        return false;
    }

    public static String getAppkey(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("dmkey");
        } catch (Exception e) {
            LogUtils.err("控制功能", e.getMessage());
            return "appkey";
        }
    }

    protected static String getBrand() {
        return DMSDK.isKeepSecret() ? "secretBrand" : Build.BRAND;
    }

    private static String getCpuHardwareByFile() {
        HashMap hashMap = new HashMap();
        try {
            Scanner scanner = new Scanner(new File("/proc/cpuinfo"));
            while (scanner.hasNextLine()) {
                try {
                    String[] split = scanner.nextLine().split(": ");
                    if (split.length > 1) {
                        hashMap.put(split[0].trim(), split[1].trim());
                    }
                } finally {
                }
            }
            scanner.close();
        } catch (Exception e) {
            LogUtils.err("控制功能", e.getMessage());
        }
        return (String) hashMap.get("Hardware");
    }

    public static String getCpuName() {
        if (DMSDK.isKeepSecret()) {
            return "secretCpu";
        }
        try {
            String cpuHardwareByFile = getCpuHardwareByFile();
            return TextUtils.isEmpty(cpuHardwareByFile) ? Build.HARDWARE : cpuHardwareByFile;
        } catch (Exception unused) {
            return Build.HARDWARE;
        }
    }

    public static String getDeviceID(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception unused) {
            return "";
        }
    }

    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            try {
                String subscriberId = telephonyManager.getSubscriberId();
                if (subscriberId != null) {
                    return subscriberId.trim();
                }
            } catch (Exception e) {
                LogUtils.err("控制功能", e.getMessage());
            }
        }
        return "";
    }

    public static String getLast8(String str) {
        return str.length() > 8 ? str.substring(str.length() - 8) : str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0023, code lost:
    
        r5 = r3.trim();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getMacAddress(android.content.Context r5) {
        /*
            r5 = 0
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()     // Catch: java.lang.Exception -> L4a
            java.lang.String r1 = "cat /sys/class/net/wlan0/address "
            java.lang.Process r0 = r0.exec(r1)     // Catch: java.lang.Exception -> L4a
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch: java.lang.Exception -> L47
            java.io.InputStream r2 = r0.getInputStream()     // Catch: java.lang.Exception -> L47
            r1.<init>(r2)     // Catch: java.lang.Exception -> L47
            java.io.LineNumberReader r2 = new java.io.LineNumberReader     // Catch: java.lang.Throwable -> L3b
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L3b
            java.lang.String r3 = ""
        L1b:
            if (r3 == 0) goto L34
            java.lang.String r3 = r2.readLine()     // Catch: java.lang.Throwable -> L28
            if (r3 == 0) goto L1b
            java.lang.String r5 = r3.trim()     // Catch: java.lang.Throwable -> L28
            goto L34
        L28:
            r3 = move-exception
            throw r3     // Catch: java.lang.Throwable -> L2a
        L2a:
            r4 = move-exception
            r2.close()     // Catch: java.lang.Throwable -> L2f
            goto L33
        L2f:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch: java.lang.Throwable -> L3b
        L33:
            throw r4     // Catch: java.lang.Throwable -> L3b
        L34:
            r2.close()     // Catch: java.lang.Throwable -> L3b
            r1.close()     // Catch: java.lang.Exception -> L47
            goto L47
        L3b:
            r2 = move-exception
            throw r2     // Catch: java.lang.Throwable -> L3d
        L3d:
            r3 = move-exception
            r1.close()     // Catch: java.lang.Throwable -> L42
            goto L46
        L42:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch: java.lang.Exception -> L47
        L46:
            throw r3     // Catch: java.lang.Exception -> L47
        L47:
            r0.destroy()     // Catch: java.lang.Exception -> L4a
        L4a:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.richinfo.dm.util.MobileUtil.getMacAddress(android.content.Context):java.lang.String");
    }

    protected static String getModel() {
        return DMSDK.isKeepSecret() ? "secretModel" : Build.MODEL;
    }

    public static String getRomSpace(Context context) {
        if (DMSDK.isKeepSecret()) {
            return Constant.SP_MODEL;
        }
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return Formatter.formatFileSize(context, statFs.getBlockCount() * statFs.getBlockSize());
    }

    protected static String getSysVersion() {
        return DMSDK.isKeepSecret() ? "secretVer" : Build.VERSION.RELEASE;
    }

    public static String getTotalRam() {
        if (DMSDK.isKeepSecret()) {
            return Constant.SP_MODEL;
        }
        String str = null;
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            try {
                BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
                try {
                    str = bufferedReader.readLine().split("\\s+")[1];
                    bufferedReader.close();
                    fileReader.close();
                } finally {
                }
            } finally {
            }
        } catch (Exception e) {
            LogUtils.err("控制功能", e.getMessage());
        }
        int ceil = str != null ? (int) Math.ceil(Float.parseFloat(str) / 1048576.0f) : 0;
        if (ceil > 0) {
            return ceil + "GB";
        }
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("/proc/meminfo"));
            return ((int) Math.ceil(new Float(Float.valueOf(properties.getProperty("MemTotal").toLowerCase().replace("kb", "").trim()).floatValue() / 1048576.0f).doubleValue())) + "GB";
        } catch (Exception e2) {
            LogUtils.err("控制功能", e2.getMessage());
            return "0 GB";
        }
    }

    public static String getVerifyMD5(Context context, String str) {
        return MD5.MD5Encode(DMDeviceInfoCollection.imei1(context) + getLast8(str));
    }

    private static boolean imsiCheck(String str) {
        return !TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str) && str.length() >= 10;
    }

    public static boolean locationCheck1(Context context) {
        boolean z;
        try {
            if (TextUtils.isEmpty(DMDeviceInfoCollection.imsi1(context)) || !DMDeviceInfoCollection.imsi1(context).startsWith("460") || TextUtils.isEmpty(DMDeviceInfoCollection.iccid1(context))) {
                return false;
            }
            long parseLong = Long.parseLong(DMDeviceInfoCollection.cellId1(context));
            int parseInt = Integer.parseInt(DMDeviceInfoCollection.lac1(context));
            String soltService1 = DMDeviceInfoCollection.soltService1(context);
            LogUtils.info("控制功能", "基站信息校验1 <cellId=" + LogUtils.safetyLog(parseLong + "") + ",lac=" + LogUtils.safetyLog(parseInt + "") + ",slotService=" + soltService1 + ">");
            boolean z2 = parseLong > 0 && parseInt > 0;
            if (!TextUtils.isEmpty(soltService1)) {
                if (soltService1.equals("5")) {
                    z = true;
                    return z2 && z;
                }
            }
            z = false;
            if (z2) {
                return false;
            }
        } catch (Exception e) {
            LogUtils.err("控制功能", e.getMessage());
            return false;
        }
    }

    public static boolean locationCheck2(Context context) {
        boolean z;
        try {
            if (DMConfig.DM_SNAPSHOT_FROM_SDK.equals(DMDeviceInfoCollection.soltQuantity(context))) {
                return true;
            }
            if (TextUtils.isEmpty(DMDeviceInfoCollection.imsi2(context)) || !DMDeviceInfoCollection.imsi2(context).startsWith("460") || TextUtils.isEmpty(DMDeviceInfoCollection.iccid2(context))) {
                return false;
            }
            long parseLong = Long.parseLong(DMDeviceInfoCollection.cellId2(context));
            int parseInt = Integer.parseInt(DMDeviceInfoCollection.lac2(context));
            String soltService2 = DMDeviceInfoCollection.soltService2(context);
            LogUtils.info("控制功能", "基站信息校验2 Check<cellId=" + LogUtils.safetyLog(parseLong + "") + ",lac=" + LogUtils.safetyLog(parseInt + "") + ",slotService=" + soltService2 + ">");
            boolean z2 = parseLong > 0 && parseInt > 0;
            if (!TextUtils.isEmpty(soltService2)) {
                if (soltService2.equals("5")) {
                    z = true;
                    return z2 && z;
                }
            }
            z = false;
            if (z2) {
                return false;
            }
        } catch (Exception e) {
            LogUtils.err("控制功能", e.getMessage());
            return false;
        }
    }

    public static String printError(VolleyError volleyError) {
        if (volleyError != null) {
            try {
                if (volleyError.networkResponse != null) {
                    return "statusCode=" + volleyError.networkResponse.statusCode + "\n" + new String(volleyError.networkResponse.data);
                }
            } catch (Exception e) {
                LogUtils.err("控制功能", e.getMessage());
            }
        }
        return "";
    }
}
