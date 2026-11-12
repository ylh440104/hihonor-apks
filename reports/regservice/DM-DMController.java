package cn.richinfo.dm;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import cn.richinfo.dm.a.C0020b;
import cn.richinfo.dm.a.C0022d;
import cn.richinfo.dm.a.C0024f;
import cn.richinfo.dm.a.C0026h;
import cn.richinfo.dm.a.C0029k;
import cn.richinfo.dm.a.C0030l;
import cn.richinfo.dm.a.C0034p;
import cn.richinfo.dm.a.D;
import cn.richinfo.dm.a.K;
import cn.richinfo.dm.a.O;
import cn.richinfo.dm.a.Q;
import cn.richinfo.dm.a.r;
import cn.richinfo.dm.a.u;
import cn.richinfo.dm.a.w;
import cn.richinfo.dm.bean.TrafficMomentInfor;
import cn.richinfo.dm.constance.PrivateConstance;
import cn.richinfo.dm.constance.PublicConstance;
import cn.richinfo.dm.util.BusinessUtils;
import cn.richinfo.dm.util.LogUtils;
import cn.richinfo.dm.util.SPUtils;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class DMController implements DMFuncImpl {
    private static final String TAG = "控制功能";
    private static DMController mDMController;
    private long lastBatteryChargeUpdateTime;
    private long lastBluetoothTime;
    private long lastNetChange;
    private long lastRegTime;
    private final Context mContext;

    private DMController(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a() {
        C0030l.a(this.mContext).b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(int i) {
        C0022d.a(this.mContext).a(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(Intent intent) {
        try {
            D.a(this.mContext).a(intent);
            D.a(this.mContext).b();
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_SP, "拍照上报异常：" + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(boolean z) {
        boolean z2;
        C0020b a2 = C0020b.a(this.mContext);
        if (z) {
            z2 = true;
        } else {
            a2.f();
            z2 = a2.b();
        }
        if (z2 && a2.e()) {
            a2.d();
        } else {
            a2.g();
        }
    }

    private void appInfoReport(final boolean z) {
        LogUtils.startInfo(PublicConstance.LOG_TAG_APP, z ? "retry" : "");
        if (C0020b.a(this.mContext).h()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$Uj09tTuLJlFErIST4qT1hgyWoAs
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.a(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b() {
        O.b(this.mContext).d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(Intent intent) {
        try {
            C0026h.a(this.mContext).a(intent, this.mContext);
        } catch (Exception e) {
            LogUtils.collectErr(PublicConstance.LOG_TAG_TT, "volte变化上报异常", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(boolean z) {
        C0029k a2 = C0029k.a(this.mContext);
        if (!z) {
            a2.c();
        }
        if (a2.b() && a2.a()) {
            a2.a(z);
        } else {
            a2.d();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        LogUtils.info(PublicConstance.LOG_TAG_TT, "采集分时流量信息");
        try {
            if (TrafficMomentInfor.getFromSP(this.mContext) == null) {
                K.a(this.mContext).c();
            }
        } catch (Exception e) {
            LogUtils.collectErr(PublicConstance.LOG_TAG_TT, "采集异常", e);
        }
    }

    private void checkLogFileReport() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_FILE);
        if (C0030l.a(this.mContext).a()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$sa3Pfra4gY2WJq0alirA-oHX3OI
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.a();
                }
            });
        }
    }

    private void checkUpdate() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_UP);
        if (O.b(this.mContext).e()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$YJfzxoF9r1arSFF8wJveYtA9P3g
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.b();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        C0034p.a(this.mContext).b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e() {
        u a2 = u.a(this.mContext);
        if (a2.a() && a2.c()) {
            a2.f();
        } else {
            a2.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f() {
        D.a(this.mContext).b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g() {
        C0022d.a(this.mContext).e();
    }

    public static synchronized DMController getInstance(Context context) {
        DMController dMController;
        synchronized (DMController.class) {
            if (mDMController == null) {
                mDMController = new DMController(context);
            }
            dMController = mDMController;
        }
        return dMController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void h() {
        C0024f.a(this.mContext).d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void i() {
        C0034p.a(this.mContext).d();
    }

    private void insertTrafficIno() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_TT);
        DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$STYJDoYk106yNDT5kNLvLW8L5k8
            @Override // java.lang.Runnable
            public final void run() {
                DMController.this.c();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j() {
        r a2 = r.a(this.mContext);
        a2.a();
        if (a2.c()) {
            a2.d();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void k() {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_REG, "休眠<" + DMSDK.getSleepTime() + "ms>后开始");
            SystemClock.sleep(DMSDK.getSleepTime());
            u a2 = u.a(this.mContext);
            if (a2.b()) {
                a2.d();
                if (a2.c() && a2.a()) {
                    a2.f();
                } else {
                    a2.e();
                }
            } else {
                LogUtils.collectW(PublicConstance.LOG_TAG_REG, "超过最大上报次数，终止本次上报");
            }
        } catch (Exception e) {
            LogUtils.err("控制功能", "sendRegisterMsg error:" + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l() {
        w.a(this.mContext).b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m() {
        Q.a(this.mContext).d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n() {
        K.a(this.mContext).e();
    }

    private void networkCollect() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_NET);
        if (C0034p.a(this.mContext).a()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$ay_tMnDBBViJWkPuuiCSsXb8bsw
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.d();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o() {
        C0024f.a(this.mContext).e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p() {
        Q.a(this.mContext).b();
    }

    private void reportBatteryChargeLog() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_BC);
        if (C0022d.a(this.mContext).d()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$rSPZRwwZy-pHj1uusZfpOffT9iY
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.g();
                }
            });
        }
    }

    private void reportBluetoothLog() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_BT);
        if (C0024f.a(this.mContext).c()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$clwzaIeJBpmyVroXRZmJkBeFlWw
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.h();
                }
            });
        }
    }

    private void reportHeartbeat(final boolean z) {
        LogUtils.startInfo(PublicConstance.LOG_TAG_HB, z ? "retry" : "");
        if (TextUtils.isEmpty(SPUtils.getRegMD5(this.mContext))) {
            LogUtils.info(PublicConstance.LOG_TAG_HB, "无自注册记录，停止心跳，触发自注册");
            reportRegister();
        } else {
            LogUtils.info(PublicConstance.LOG_TAG_HB, "开始心跳");
            if (C0029k.a(this.mContext).e()) {
                DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$SVatQ4UhjhuhiOqiejHBABaQnKA
                    @Override // java.lang.Runnable
                    public final void run() {
                        DMController.this.b(z);
                    }
                });
            }
        }
    }

    private void reportNetworkMsg() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_NET);
        if (C0034p.a(this.mContext).c()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$vcSEgWLwfa9L31HqOgS1wW9zKCs
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.i();
                }
            });
        }
    }

    private void reportPreInstallMsg() {
        long preInstallReportTime = SPUtils.getPreInstallReportTime(this.mContext);
        LogUtils.info("控制功能", "预装应用已于" + LogUtils.formatTime(preInstallReportTime) + "上报");
        if (preInstallReportTime != 0) {
            LogUtils.collectW("控制功能", "已上报过，不再上报");
            return;
        }
        LogUtils.startInfo(PublicConstance.LOG_TAG_PI);
        if (r.a(this.mContext).b()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$MHpW15JGizO6K_lM52-0zgvQMHc
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.j();
                }
            });
        }
    }

    private void reportRegister() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_REG);
        DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$J5AjK39jp8-sk9sFdjq4JXQ2njw
            @Override // java.lang.Runnable
            public final void run() {
                DMController.this.k();
            }
        });
    }

    private void reportRoamingMsg() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_IR);
        if (w.a(this.mContext).a()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$50_Dl1oRKj19XUEr3qaj4WikTnc
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.l();
                }
            });
        }
    }

    private void reportWifiListMsg() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_WIFI);
        if (Q.a(this.mContext).c()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$xjtxFZhICevk_Sh93Siuvl6ktQ0
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.m();
                }
            });
        }
    }

    private void timeTrafficReport() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_TT);
        if (K.a(this.mContext).d()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$HZEQmSLZYwmj-TS7V_M1taZu0yc
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.n();
                }
            });
        }
    }

    private void updateBatteryChargeLog(final int i) {
        if (C0022d.a(this.mContext).b()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$1Cu4xnnUbqSWOMH6SAnpMGt3qWA
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.a(i);
                }
            });
        }
    }

    private void updateBluetoothLog() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_BT);
        if (C0024f.a(this.mContext).b()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$pk1ZSGDbdM8RXn-uOkmC7AwT2AM
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.o();
                }
            });
        }
    }

    private void wifiListCollect() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_WIFI);
        if (Q.a(this.mContext).a()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$5gGIQcT9tdl8ouMw29EEHlNSLZw
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.p();
                }
            });
        }
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onAppReport() {
        appInfoReport(false);
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onAppReportRetry() {
        appInfoReport(true);
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onBatteryChange(Intent intent) {
        LogUtils.startInfo(PublicConstance.LOG_TAG_BC, "记录充电状态变化");
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastBatteryChargeUpdateTime < 5000) {
            LogUtils.collectW(PublicConstance.LOG_TAG_BC, "充电采集频繁触发，终止本次采集，5s后重试");
        } else {
            this.lastBatteryChargeUpdateTime = currentTimeMillis;
            updateBatteryChargeLog(intent.getIntExtra(PublicConstance.EXTRA_BATTERY_CHARGE_STATE, -1));
        }
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onBluetoothChange() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_BC, "蓝牙连接状态变化");
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastBluetoothTime < 5000) {
            LogUtils.collectW(PublicConstance.LOG_TAG_BC, "蓝牙采集频繁触发，终止本次采集，5s后重试");
        } else {
            this.lastBluetoothTime = currentTimeMillis;
            updateBluetoothLog();
        }
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onConnectivityChange() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastNetChange;
        if (j > 0 && elapsedRealtime - j < 5000) {
            LogUtils.warn("控制功能", "网络变化广播重复触发，不响应");
            return;
        }
        this.lastNetChange = elapsedRealtime;
        try {
            LogUtils.startInfo(PublicConstance.LOG_TAG_TT);
            K.a(this.mContext).c();
        } catch (Exception e) {
            LogUtils.collectErr(PublicConstance.LOG_TAG_TT, "采集异常", e);
        }
        try {
            final C0020b a2 = C0020b.a(this.mContext);
            if (a2.h()) {
                ExecutorService executor = DMSDK.getExecutor();
                a2.getClass();
                executor.execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$MVtvbTq32IUGsJxl5t57NENlXKg
                    @Override // java.lang.Runnable
                    public final void run() {
                        C0020b.this.i();
                    }
                });
            }
        } catch (Exception e2) {
            LogUtils.collectErr(PublicConstance.LOG_TAG_APP, "更新异常", e2);
        }
        reportRoamingMsg();
        BusinessUtils.checkAlarms(this.mContext);
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onHeartbeat() {
        reportHeartbeat(false);
        timeTrafficReport();
        reportBatteryChargeLog();
        reportBluetoothLog();
        reportNetworkMsg();
        reportWifiListMsg();
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onHeartbeatRetry() {
        reportHeartbeat(true);
    }

    public void onHeartbeatSuccess() {
        checkUpdate();
        checkLogFileReport();
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onNetworkCollect() {
        networkCollect();
        wifiListCollect();
    }

    public void onRegSuccess() {
        reportHeartbeat(false);
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onRegisterRetry() {
        LogUtils.startInfo(PublicConstance.LOG_TAG_REG, "自注册重试");
        DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$wR08wtMtKFLlMhoQi5sbCx3ilmE
            @Override // java.lang.Runnable
            public final void run() {
                DMController.this.e();
            }
        });
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onSimStateChange() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastRegTime;
        if (j > 0 && elapsedRealtime - j < 60000) {
            LogUtils.collectW(PublicConstance.LOG_TAG_REG, "自注册重复触发，终止本次上报，60s后重试");
            return;
        }
        this.lastRegTime = elapsedRealtime;
        insertTrafficIno();
        reportRegister();
        reportPreInstallMsg();
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onSnapshotRetry() {
        DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$zXNd6ltMpSRYyBZuNfyH8YxzQ28
            @Override // java.lang.Runnable
            public final void run() {
                DMController.this.f();
            }
        });
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onThirdRequest(final Intent intent) {
        if (BusinessUtils.switchCheck(SPUtils.getSnapshotReportSwitch(this.mContext))) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$4OS2D_4AagJuaAJbSxMurVBfWwk
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.a(intent);
                }
            });
        } else {
            LogUtils.warn(PublicConstance.LOG_TAG_SP, PrivateConstance.LOG_MSG_WHITELIST);
        }
    }

    @Override // cn.richinfo.dm.DMFuncImpl
    public void onVolteChange(final Intent intent) {
        if (C0026h.a(this.mContext).a()) {
            DMSDK.getExecutor().execute(new Runnable() { // from class: cn.richinfo.dm.-$$Lambda$DMController$9fObZfMtR8fgcDW0s79hrC60aOw
                @Override // java.lang.Runnable
                public final void run() {
                    DMController.this.b(intent);
                }
            });
        }
    }
}
