package com.hihonor.regservice;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import cn.richinfo.dm.DMConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmyk.android.telephony.DmykAbsTelephonyManager;
import com.hihonor.android.ims.HwImsManager;
import com.hihonor.android.os.SystemPropertiesEx;
import com.hihonor.android.telephony.HwTelephonyManager;
import com.hihonor.android.telephony.ServiceStateEx;
import com.hihonor.android.util.SystemInfo;
import com.hihonor.uikit.hwresources.utils.HwWidgetInstantiator;
import com.richinfo.mthttp.Constant;
import com.richinfo.mthttp.HttpRequestAgent;
import com.richinfo.mthttp.HttpRequestCallback;
import com.richinfo.mthttp.MTSDK;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MtUtils {
    public static final String APPKEY = "A100008208";
    private static final int ARRAY_LENGTH_MIN = 1;
    private static final int ARRAY_LIMIT = 2;
    private static final long BYTE_CARDINAL_NUMBER = 1024;
    private static final String HEY2 = "HEY2";
    private static final String HEY2_SCREEN_SIZE = "12.1";
    private static final String IS_CONNECTED = "1";
    private static final int MEMORY_UNIT = 1024;
    private static final String MT_HTTP_URL = "https://a.fxltsbl.com/";
    public static final String NETWORK_2G = "2g";
    public static final String NETWORK_3G = "3g";
    public static final String NETWORK_4G = "4g";
    public static final String NETWORK_5G = "5g";
    private static final int NETWORK_TYPE_UNKNOWN = 0;
    private static final int NETWORK_TYPE_WIFI = 18;
    public static final String NETWORK_UNKNOW = "unknow";
    public static final String NETWORK_WIFI = "wifi";
    private static final String NOT_CONNECTED = "0";
    public static final String NOT_SUPPORTED = "-1";
    private static final int NSA_STATE2 = 2;
    private static final int NSA_STATE5 = 5;
    private static final long ONE_GB = 1000000000;
    private static final String PATH_CPU = "/proc/cpuinfo";
    private static final int RESPONSE_SUCCESS = 200;
    private static final String TAG = "MtUtils";
    public static final String TEMPLATE_ID = "TY000020";
    private static final String VOLTE_STATE_OFF = "1";
    private static final String VOLTE_STATE_ON = "0";
    private static final String VOLTE_STATE_UNKNOWN = "-1";
    private static Application mApplication;
    private static MtUtils mMtUtils;
    private static final String CPU_INFO = SystemPropertiesEx.get("ro.config.cpu_info_display", "");
    private static final String PRODUCT_VERSION_PRE = SystemPropertiesEx.get("ro.config.product_version_pre", "");

    private MtUtils() {
    }

    public static synchronized MtUtils getInstance() {
        MtUtils mtUtils;
        synchronized (MtUtils.class) {
            if (mMtUtils == null) {
                mMtUtils = new MtUtils();
            }
            mtUtils = mMtUtils;
        }
        return mtUtils;
    }

    public void initMtHttp(Application application) {
        Log.i(TAG, "initMtHttp");
        mApplication = application;
        MTSDK.getInstance().init(application, 1);
        MTSDK.getInstance().setUrl(MT_HTTP_URL);
        MTSDK.setHeartChanel(0);
        MTSDK.getInstance().setHttpRequestAgent(new HttpRequestAgent() { // from class: com.hihonor.regservice.MtUtils.1
            @Override // com.richinfo.mthttp.HttpRequestAgent
            public void request(String str, String str2, String str3, final HttpRequestCallback httpRequestCallback) {
                JSONObject jSONObject;
                RequestQueue newRequestQueue = Volley.newRequestQueue(MtUtils.mApplication);
                try {
                    jSONObject = new JSONObject(str3);
                } catch (JSONException e) {
                    Log.i(MtUtils.TAG, "JSONException " + e.getMessage());
                    jSONObject = null;
                }
                newRequestQueue.add(new JsonObjectRequest(1, str2, jSONObject, new Response.Listener<JSONObject>() { // from class: com.hihonor.regservice.MtUtils.1.1
                    @Override // com.android.volley.Response.Listener
                    public void onResponse(JSONObject jSONObject2) {
                        Log.i(MtUtils.TAG, "onVolleyResponse");
                        httpRequestCallback.onResponse(MtUtils.RESPONSE_SUCCESS, jSONObject2.toString());
                    }
                }, new Response.ErrorListener() { // from class: com.hihonor.regservice.MtUtils.1.2
                    @Override // com.android.volley.Response.ErrorListener
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(MtUtils.TAG, "onVolleyError->" + volleyError.getMessage());
                        httpRequestCallback.onFailure(volleyError.getMessage());
                    }
                }));
            }
        });
        MTSDK.getInstance().setDeviceManager(new HnIDeviceForMt());
    }

    public String getImei1() {
        return DmykAbsTelephonyManager.getDefault(mApplication).getGsmDeviceId(0);
    }

    public String getImsi() {
        return DmUtils.getSubscriberId(0, mApplication);
    }

    public String getMacAddress() {
        return DmUtils.getMacAddress(mApplication);
    }

    public String getRom() {
        String valueOf = String.valueOf(DmUtils.roundStorageSize(Environment.getDataDirectory().getTotalSpace()) / BYTE_CARDINAL_NUMBER);
        long j = 0;
        if (!valueOf.isEmpty()) {
            try {
                j = DmUtils.calculateStorageSize(Long.parseLong(valueOf) * BYTE_CARDINAL_NUMBER);
            } catch (NumberFormatException unused) {
                Log.e(TAG, "numberformat exception");
            }
        }
        return (j / ONE_GB) + " GB";
    }

    public String getRam() {
        int ceil;
        String deviceRam = SystemInfo.getDeviceRam();
        if (deviceRam != null) {
            try {
                ceil = (int) Math.ceil(Float.valueOf(Float.valueOf(deviceRam).floatValue() / 1048576.0f).doubleValue());
            } catch (NumberFormatException unused) {
                Log.e(TAG, "numberformat exception");
            }
            return ceil + " GB";
        }
        ceil = 0;
        return ceil + " GB";
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x00c1, code lost:
    
        if (r4 != null) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00c3, code lost:
    
        r4.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00c7, code lost:
    
        com.hihonor.regservice.Log.e(com.hihonor.regservice.MtUtils.TAG, "getCPUModel IOException");
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00a4, code lost:
    
        if (r4 != null) goto L98;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 6, insn: 0x00cc: MOVE (r3 I:??[OBJECT, ARRAY]) = (r6 I:??[OBJECT, ARRAY]), block:B:106:0x00cc */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00ba A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00b1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x009d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0094 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getCPUModel() {
        /*
            Method dump skipped, instructions count: 233
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.hihonor.regservice.MtUtils.getCPUModel():java.lang.String");
    }

    public String getSysVersion() {
        return String.valueOf(Build.VERSION.RELEASE);
    }

    public String getSoftwareVer() {
        String str = PRODUCT_VERSION_PRE;
        if (TextUtils.isEmpty(str)) {
            return Build.DISPLAY.replaceFirst(Build.MODEL + "[\\W]*", "");
        }
        return Build.DISPLAY.replaceFirst(str + "[\\W]*", "");
    }

    public String getSoftwareName() {
        return Build.MODEL;
    }

    public String getVolte() {
        if (!isInvalidPhoneId(0, mApplication) && DmUtils.getSimType(DmUtils.getSubscriberId(0, mApplication)) == 2) {
            if (DmUtils.getSimState(0, mApplication) == 1) {
                return DMConfig.DM_SNAPSHOT_FROM_SDK;
            }
            if (HwTelephonyManager.getDefault().getDefault4GSlotId() == 0) {
                return HwImsManager.isEnhanced4gLteModeSettingEnabledByUser(mApplication, 0) ? Constant.SP_MODEL : DMConfig.DM_SNAPSHOT_FROM_SDK;
            }
        }
        return "-1";
    }

    private static boolean isInvalidPhoneId(int i, Context context) {
        return context == null || i < 0 || i >= DmUtils.getPhoneCount(context);
    }

    public String getNetType() {
        int networkType = getNetworkType();
        if (networkType == NETWORK_TYPE_WIFI) {
            return "wifi";
        }
        if (networkType == 20) {
            return "5g";
        }
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case Request.Method.PATCH /* 7 */:
            case 11:
                return "2g";
            case 3:
            case 5:
            case Request.Method.TRACE /* 6 */:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case HwWidgetInstantiator.TYPE_MASK /* 15 */:
                return "3g";
            case 13:
                return "4g";
            default:
                return "unknow";
        }
    }

    private int getNetworkType() {
        TelephonyManager telephonyManager;
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        NetworkInfo.State state;
        Application application = mApplication;
        if (application == null || (telephonyManager = (TelephonyManager) application.getSystemService(TelephonyManager.class)) == null || isInvalidPhoneId(0, mApplication) || DmUtils.getSimState(0, mApplication) == 1 || DmUtils.getSimType(DmUtils.getSubscriberId(0, mApplication)) != 2 || (connectivityManager = (ConnectivityManager) mApplication.getSystemService(ConnectivityManager.class)) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable()) {
            return 0;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo != null && (state = networkInfo.getState()) != null && (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)) {
            return NETWORK_TYPE_WIFI;
        }
        if (isNsaState(0)) {
            return getHwNetworkTypeForNsa(0);
        }
        HwTelephonyManager.getDefault();
        return HwTelephonyManager.getDataNetworkType(telephonyManager, 0);
    }

    private static int getHwNetworkTypeForNsa(int i) {
        HwTelephonyManager.getDefault();
        ServiceState serviceStateForSubscriber = HwTelephonyManager.getServiceStateForSubscriber(i);
        if (serviceStateForSubscriber == null) {
            return 0;
        }
        return ServiceStateEx.getConfigRadioTechnology(serviceStateForSubscriber);
    }

    private static boolean isNsaState(int i) {
        int nsaState;
        HwTelephonyManager.getDefault();
        ServiceState serviceStateForSubscriber = HwTelephonyManager.getServiceStateForSubscriber(i);
        return serviceStateForSubscriber != null && (nsaState = ServiceStateEx.getNsaState(serviceStateForSubscriber)) >= 2 && nsaState <= 5;
    }

    public String getPhoneNumber() {
        return DmUtils.getPhoneNumber(0, mApplication);
    }

    public String getSn() {
        TelephonyManager telephonyManager = DmUtils.getTelephonyManager(mApplication);
        if (telephonyManager == null) {
            return null;
        }
        Log.i(TAG, "telephonyManager is not null");
        return telephonyManager.getDeviceId();
    }

    public String getScreenSize() {
        if (Build.MODEL.contains(HEY2)) {
            return HEY2_SCREEN_SIZE;
        }
        return null;
    }

    public String getResolution() {
        DisplayMetrics displayMetrics = mApplication.getResources().getDisplayMetrics();
        return String.valueOf(displayMetrics.widthPixels) + "*" + String.valueOf(displayMetrics.heightPixels);
    }

    public String getNetworkStatus() {
        Application application = mApplication;
        if (application == null) {
            return "-1";
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService("connectivity");
        if (connectivityManager == null) {
            Log.i(TAG, "connManager is null, isConnected return false");
            return "-1";
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected() ? DMConfig.DM_SNAPSHOT_FROM_SDK : Constant.SP_MODEL;
        }
        Log.i(TAG, "networkInfo is null, isConnected return false");
        return "-1";
    }
}
