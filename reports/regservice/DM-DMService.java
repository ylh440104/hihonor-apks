package cn.richinfo.dm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;
import cn.richinfo.dm.DMConfig;
import cn.richinfo.dm.DMController;
import cn.richinfo.dm.DMFuncImpl;
import cn.richinfo.dm.DMSDK;
import cn.richinfo.dm.a.O;
import cn.richinfo.dm.constance.PrivateConstance;
import cn.richinfo.dm.constance.PublicConstance;
import cn.richinfo.dm.receiver.DynamicBroadcast;
import cn.richinfo.dm.util.BusinessUtils;
import cn.richinfo.dm.util.LogToFile2;
import cn.richinfo.dm.util.LogUtils;
import cn.richinfo.dm.util.MD5;
import cn.richinfo.dm.util.SPUtils;
import dalvik.system.DexClassLoader;
import java.util.Locale;

/* loaded from: classes.dex */
public class DMService extends Service {

    /* renamed from: a, reason: collision with root package name */
    public static String f67a = "控制功能";
    private DMFuncImpl b;
    private int c = 240000;
    private a d;
    private DynamicBroadcast e;

    class a extends CountDownTimer {

        /* renamed from: a, reason: collision with root package name */
        int f68a;

        public a(long j, long j2, int i) {
            super(j, j2);
            this.f68a = i;
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            DMService.this.stopSelf(this.f68a);
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            LogUtils.debug(DMService.f67a, "计时器：" + j);
        }
    }

    private void a(int i) {
        if (DMSDK.isServiceKeepAlive()) {
            return;
        }
        a aVar = this.d;
        if (aVar != null) {
            aVar.cancel();
        }
        a aVar2 = new a(this.c, 60000L, i);
        this.d = aVar2;
        aVar2.start();
    }

    private void a(Intent intent) {
        c();
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        b(intent);
    }

    private boolean a() {
        if (!BusinessUtils.switchCheck(SPUtils.getSleepSwitch(this))) {
            return false;
        }
        long sleepStartTime = SPUtils.getSleepStartTime(this);
        long sleepEndTime = SPUtils.getSleepEndTime(this);
        LogUtils.info(PublicConstance.LOG_TAG_BS, "休眠时间:" + LogUtils.formatTime(sleepStartTime) + "~" + LogUtils.formatTime(sleepEndTime));
        if (sleepStartTime <= 0 || sleepEndTime <= 0) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        LogUtils.info(f67a, "currentTime: " + LogUtils.formatTime(currentTimeMillis));
        return sleepStartTime < currentTimeMillis && currentTimeMillis < sleepEndTime;
    }

    private DMFuncImpl b() {
        if (!DMSDK.isDynamicReport()) {
            LogUtils.warn(f67a, BusinessUtils.toggleCloseMsg(PublicConstance.LOG_TAG_UP, "setDynamicReport"));
            return null;
        }
        O b = O.b(this);
        if ("N".equals(SPUtils.getDynamicLoadSwitch(this))) {
            b.a();
            LogUtils.warn(f67a, "动态更新开关未开启");
            return null;
        }
        try {
            if (b.b() == null) {
                LogUtils.info(f67a, "本地无动态包");
                return null;
            }
            String fileMD5 = MD5.fileMD5(b.c());
            String dynamicMd5 = SPUtils.getDynamicMd5(this);
            if (!TextUtils.equals(dynamicMd5, fileMD5)) {
                LogUtils.warn(f67a, "动态包校验未通过，预期动态包MD5为：" + dynamicMd5 + "本地动态包MD5为：" + fileMD5);
                return null;
            }
            Class loadClass = new DexClassLoader(b.c(), getCacheDir().getAbsolutePath(), null, DMService.class.getClassLoader()).loadClass(PrivateConstance.CLASS_PATCH_CONTROLLER);
            String str = (String) loadClass.getMethod(PrivateConstance.METHOD_PATCH_VERSION, new Class[0]).invoke(null, new Object[0]);
            if (str == null || !str.startsWith(DMConfig.SDK_VERSION)) {
                LogUtils.warn(f67a, "动态包版本信息为：" + str + "，无法匹配，不加载动态包");
                b.a();
                return null;
            }
            this.b = (DMFuncImpl) loadClass.getMethod(PrivateConstance.METHOD_PATCH_INSTANCE, Context.class).invoke(null, this);
            LogUtils.info(f67a, "动态包加载完成");
            return this.b;
        } catch (Exception e) {
            LogUtils.err(f67a, "加载动态包异常：" + e.getMessage());
            return null;
        }
    }

    private void b(Intent intent) {
        if (!BusinessUtils.checkIfHaveYDSim(this)) {
            LogUtils.reportW(f67a, "无移动卡，不上报");
            return;
        }
        String action = intent.getAction();
        LogUtils.info(PublicConstance.LOG_TAG_BS, "DMService:处理业务action=" + action);
        if (PublicConstance.DM_ACTION_SIM_STATE_CHANGED.equals(action)) {
            this.b.onSimStateChange();
            return;
        }
        if (PublicConstance.DM_ACTION_REGISTER_RETRY.equals(action)) {
            this.b.onRegisterRetry();
            return;
        }
        if (PublicConstance.DM_ACTION_HEARTBEAT.equals(action)) {
            this.b.onHeartbeat();
            return;
        }
        if (PublicConstance.DM_ACTION_HEARTBEAT_RETRY.equals(action)) {
            this.b.onHeartbeatRetry();
            return;
        }
        if (PublicConstance.DM_ACTION_APP_INFO.equals(action)) {
            this.b.onAppReport();
            return;
        }
        if (PublicConstance.DM_ACTION_APP_INFO_RETRY.equals(action)) {
            this.b.onAppReportRetry();
            return;
        }
        if (PublicConstance.DM_ACTION_DM_CONNECTIVITY_CHANGE.equals(action)) {
            this.b.onConnectivityChange();
            return;
        }
        if (PublicConstance.ACTION_VOLTE_STATE_CHANGE.equals(action)) {
            this.b.onVolteChange(intent);
            return;
        }
        if (PublicConstance.DM_ACTION_BATTERY_CHARGE_STATE_CHANGE.equals(action)) {
            this.b.onBatteryChange(intent);
            return;
        }
        if (PublicConstance.DM_ACTION_BLUETOOTH_CONNECTIVITY_CHANGE.equals(action)) {
            this.b.onBluetoothChange();
            return;
        }
        if (PublicConstance.DM_ACTION_NETWORK_COLLECT.equals(action)) {
            this.b.onNetworkCollect();
        } else if (PublicConstance.DM_ACTION_THIRD_REQUEST.equals(action)) {
            this.b.onThirdRequest(intent);
        } else if (PublicConstance.DM_ACTION_SNAPSHOT_RETRY.equals(action)) {
            this.b.onSnapshotRetry();
        }
    }

    private void c() {
        LogUtils.info(f67a, "initController");
        DMFuncImpl b = b();
        this.b = b;
        if (b == null) {
            this.b = DMController.getInstance(this);
        } else {
            LogUtils.startInfo(f67a, "动态包已加载");
        }
    }

    private void d() {
        String str = f67a;
        Locale locale = Locale.getDefault();
        Object[] objArr = new Object[9];
        objArr[0] = Long.valueOf(DMSDK.getMinAppUpdateTime());
        objArr[1] = DMSDK.getBaseConfigUrl(this);
        objArr[2] = Long.valueOf(DMSDK.getSleepTime());
        objArr[3] = DMSDK.getDebugLogPath();
        objArr[4] = Long.valueOf(DMSDK.getTimeUnit());
        objArr[5] = Boolean.valueOf(DMSDK.isReportApp());
        objArr[6] = Boolean.valueOf(DMSDK.isCollectPreinstall());
        objArr[7] = Boolean.valueOf(DMSDK.isKeepSecret());
        objArr[8] = Boolean.valueOf(DMSDK.getListener() == null);
        LogUtils.info(str, String.format(locale, "SDK初始化配置为:\ngetMinAppUpdateTime=%s\ngetBaseConfigUrl=%s\ngetSleepTime=%s\ngetDebugLogPath=%s\ngetTimeUnit=%s\nisReportApp=%s\nisCollectPreinstall=%s\nisKeepSecret=%s\nlistener==null(%s)\n", objArr));
    }

    private void e() {
        try {
            this.e = new DynamicBroadcast();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(PublicConstance.DM_ACTION_THIRD_REQUEST_DYNAMIC);
            if (Build.VERSION.SDK_INT >= 33) {
                registerReceiver(this.e, intentFilter, "com.dm.permission.THIRD_SEND", null, 2);
            } else {
                registerReceiver(this.e, intentFilter, "com.dm.permission.THIRD_SEND", null);
            }
        } catch (Exception e) {
            LogUtils.err(f67a, e.getMessage());
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        try {
            e();
            LogToFile2.init(this);
            c();
            try {
                d();
            } catch (Exception unused) {
            }
        } catch (Exception e) {
            LogUtils.err(f67a, e.getMessage());
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtils.info(f67a, "Service 销毁");
        DynamicBroadcast dynamicBroadcast = this.e;
        if (dynamicBroadcast != null) {
            unregisterReceiver(dynamicBroadcast);
            this.e = null;
        }
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        LogUtils.info(f67a, "SDK_Version=4.0.6");
        try {
            LogUtils.info(f67a, "日志上报检查");
            LogUtils.initReport(BusinessUtils.logCheck(this), this);
            if (a()) {
                LogUtils.reportW(f67a, "SDK need to sleep");
            } else {
                a(intent);
            }
        } catch (Exception e) {
            LogUtils.err(f67a, e.getMessage());
        }
        a(i2);
        return super.onStartCommand(intent, i, i2);
    }
}
