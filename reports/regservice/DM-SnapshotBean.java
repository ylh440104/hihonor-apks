package cn.richinfo.dm.bean;

import android.content.Context;
import cn.richinfo.dm.DMConfig;
import cn.richinfo.dm.constance.PrivateConstance;
import cn.richinfo.dm.constance.PublicConstance;
import cn.richinfo.dm.util.BusinessUtils;
import cn.richinfo.dm.util.DMDeviceInfoCollection;
import cn.richinfo.dm.util.LogUtils;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class l {
    private String A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String F;
    private String G;
    private String H;
    private String I;
    private String J;
    private String K;
    private String L;
    private String M;
    private String N;

    /* renamed from: a, reason: collision with root package name */
    private String f61a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private String o;
    private String p;
    private String q;
    private String r;
    private String s;
    private String t;
    private String u;
    private String v;
    private String w;
    private String x;
    private String y;
    private String z;

    public static l a(Context context, m mVar, JSONObject jSONObject) {
        LogUtils.info(PublicConstance.LOG_TAG_SP, "开始采集");
        l lVar = new l();
        lVar.D(DMConfig.SDK_VERSION);
        lVar.n(DMDeviceInfoCollection.imei1(context));
        lVar.o(DMDeviceInfoCollection.imei2(context));
        lVar.c(DMDeviceInfoCollection.getBrand(context));
        lVar.w(DMDeviceInfoCollection.getModel(context));
        lVar.k(DMDeviceInfoCollection.firmwareVer(context));
        lVar.K(DMDeviceInfoCollection.systemVer(context));
        lVar.h(DMDeviceInfoCollection.cpu(context));
        lVar.C(DMDeviceInfoCollection.rom(context));
        lVar.A(DMDeviceInfoCollection.ram(context));
        lVar.L(DMDeviceInfoCollection.type(context));
        lVar.l(DMDeviceInfoCollection.iccid1(context));
        lVar.m(DMDeviceInfoCollection.iccid2(context));
        lVar.p(DMDeviceInfoCollection.imsi1(context));
        lVar.q(DMDeviceInfoCollection.imsi2(context));
        lVar.u(DMDeviceInfoCollection.macAddress(context));
        lVar.d(DMDeviceInfoCollection.cellId1(context));
        lVar.s(DMDeviceInfoCollection.lac1(context));
        lVar.e(DMDeviceInfoCollection.cellId2(context));
        lVar.t(DMDeviceInfoCollection.lac2(context));
        lVar.f(DMDeviceInfoCollection.getMobileNetType(context));
        lVar.i(DMDeviceInfoCollection.dataCard(context));
        lVar.v(DMDeviceInfoCollection.masterStatus(context));
        lVar.E(System.currentTimeMillis() + "");
        lVar.H(DMDeviceInfoCollection.soltQuantity(context));
        lVar.j(DMDeviceInfoCollection.dataCard2(context));
        lVar.I(DMDeviceInfoCollection.soltService1(context));
        lVar.J(DMDeviceInfoCollection.soltService2(context));
        lVar.F(DMDeviceInfoCollection.soltNetwork1(context));
        lVar.G(DMDeviceInfoCollection.soltNetwork2(context));
        lVar.r(DMDeviceInfoCollection.inType(context));
        lVar.M(mVar.g);
        lVar.b(mVar.d);
        lVar.a(mVar.f62a);
        lVar.z(mVar.e);
        lVar.B(mVar.f);
        lVar.g((mVar.b.equals("unknown") || !mVar.b.contains(".")) ? DMConfig.DM_SNAPSHOT_FROM_H5 : DMConfig.DM_SNAPSHOT_FROM_SDK);
        if (a("phoneNumber1", jSONObject)) {
            lVar.x(DMDeviceInfoCollection.phoneNumber1(context));
        } else {
            lVar.x(PrivateConstance.DM_UNCOLLECT);
        }
        if (!a(context)) {
            if (a("phoneNumber2", jSONObject)) {
                lVar.y(DMDeviceInfoCollection.phoneNumber2(context));
            } else {
                lVar.y(PrivateConstance.DM_UNCOLLECT);
            }
        }
        return lVar;
    }

    private static boolean a(Context context) {
        return DMConfig.DM_SNAPSHOT_FROM_SDK.equals(DMDeviceInfoCollection.soltQuantity(context));
    }

    private static boolean a(String str, JSONObject jSONObject) {
        return BusinessUtils.ifCollect(jSONObject, str);
    }

    public void A(String str) {
        this.k = str;
    }

    public void B(String str) {
        this.K = str;
    }

    public void C(String str) {
        this.j = str;
    }

    public void D(String str) {
        this.f61a = str;
    }

    public void E(String str) {
        this.w = str;
    }

    public void F(String str) {
        this.B = str;
    }

    public void G(String str) {
        this.C = str;
    }

    public void H(String str) {
        this.x = str;
    }

    public void I(String str) {
        this.z = str;
    }

    public void J(String str) {
        this.A = str;
    }

    public void K(String str) {
        this.h = str;
    }

    public void L(String str) {
        this.l = str;
    }

    public void M(String str) {
        this.G = str;
    }

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("sdkVersion", this.f61a);
        jSONObject.put("imei1", this.b);
        jSONObject.put("imei2", this.c);
        jSONObject.put("meid", this.d);
        jSONObject.put("brand", this.e);
        jSONObject.put("model", this.f);
        jSONObject.put("firmwareVer", this.g);
        jSONObject.put("systemVer", this.h);
        jSONObject.put("cpu", this.i);
        jSONObject.put("rom", this.j);
        jSONObject.put("ram", this.k);
        jSONObject.put("type", this.l);
        jSONObject.put("iccid1", this.m);
        jSONObject.put("iccid2", this.n);
        jSONObject.put("imsi1", this.o);
        jSONObject.put("imsi2", this.p);
        jSONObject.put("mac", this.q);
        jSONObject.put("cellId", this.r);
        jSONObject.put("lac", this.s);
        jSONObject.put("channel", this.t);
        jSONObject.put("dataCard", this.u);
        jSONObject.put("masterStatus", this.v);
        jSONObject.put("sendTime", this.w);
        jSONObject.put("soltQuantity", this.x);
        jSONObject.put("dataCard2", this.y);
        jSONObject.put("soltService1", this.z);
        jSONObject.put("soltService2", this.A);
        jSONObject.put("soltNetwork1", this.B);
        jSONObject.put("soltNetwork2", this.C);
        jSONObject.put("cellId2", this.D);
        jSONObject.put("lac2", this.E);
        jSONObject.put("inType", this.F);
        jSONObject.put("verify", this.G);
        jSONObject.put("appSdkVersion", this.H);
        jSONObject.put("appId", this.I);
        jSONObject.put("promotionId", this.J);
        jSONObject.put("requestId", this.K);
        jSONObject.put("comeFrom", this.L);
        jSONObject.put("phoneNumber1", this.M);
        jSONObject.put("phoneNumber2", this.N);
        return jSONObject;
    }

    public void a(String str) {
        this.I = str;
    }

    public JSONObject b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("sdkVersion", this.f61a);
        jSONObject.put("imei1", this.b);
        jSONObject.put("imei2", this.c);
        jSONObject.put("meid", this.d);
        jSONObject.put("brand", this.e);
        jSONObject.put("model", this.f);
        jSONObject.put("firmwareVer", this.g);
        jSONObject.put("systemVer", this.h);
        jSONObject.put("type", this.l);
        jSONObject.put("iccid1", this.m);
        jSONObject.put("iccid2", this.n);
        jSONObject.put("imsi1", this.o);
        jSONObject.put("imsi2", this.p);
        jSONObject.put("masterStatus", this.v);
        return jSONObject;
    }

    public void b(String str) {
        this.H = str;
    }

    public void c(String str) {
        this.e = str;
    }

    public void d(String str) {
        this.r = str;
    }

    public void e(String str) {
        this.D = str;
    }

    public void f(String str) {
        this.t = str;
    }

    public void g(String str) {
        this.L = str;
    }

    public void h(String str) {
        this.i = str;
    }

    public void i(String str) {
        this.u = str;
    }

    public void j(String str) {
        this.y = str;
    }

    public void k(String str) {
        this.g = str;
    }

    public void l(String str) {
        this.m = str;
    }

    public void m(String str) {
        this.n = str;
    }

    public void n(String str) {
        this.b = str;
    }

    public void o(String str) {
        this.c = str;
    }

    public void p(String str) {
        this.o = str;
    }

    public void q(String str) {
        this.p = str;
    }

    public void r(String str) {
        this.F = str;
    }

    public void s(String str) {
        this.s = str;
    }

    public void t(String str) {
        this.E = str;
    }

    public String toString() {
        return "mSnapshotMsg{sdkVersion='" + this.f61a + "', imei1='" + this.b + "', imei2='" + this.c + "', meid='" + this.d + "', brand='" + this.e + "', model='" + this.f + "', firmwareVer='" + this.g + "', systemVer='" + this.h + "', cpu='" + this.i + "', rom='" + this.j + "', ram='" + this.k + "', type='" + this.l + "', iccid1='" + this.m + "', iccid2='" + this.n + "', imsi1='" + this.o + "', imsi2='" + this.p + "', mac='" + this.q + "', cellId='" + this.r + "', lac='" + this.s + "', channel='" + this.t + "', dataCard='" + this.u + "', masterStatus='" + this.v + "', sendTime='" + this.w + "', soltQuantity='" + this.x + "', dataCard2='" + this.y + "', soltService1='" + this.z + "', soltService2='" + this.A + "', soltNetwork1='" + this.B + "', soltNetwork2='" + this.C + "', cellId2='" + this.D + "', lac2='" + this.E + "', inType='" + this.F + "', appId='" + this.I + "', appSdkVersion='" + this.H + "', promotionId='" + this.J + "', requestId='" + this.K + "', comeFrom='" + this.L + "', phoneNumber1='" + this.M + "', phoneNumber2='" + this.N + "'}";
    }

    public void u(String str) {
        this.q = str;
    }

    public void v(String str) {
        this.v = str;
    }

    public void w(String str) {
        this.f = str;
    }

    public void x(String str) {
        this.M = str;
    }

    public void y(String str) {
        this.N = str;
    }

    public void z(String str) {
        this.J = str;
    }
}
