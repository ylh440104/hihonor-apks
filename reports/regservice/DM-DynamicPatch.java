package cn.richinfo.dm.a;

import android.content.Context;
import android.text.TextUtils;
import cn.richinfo.dm.DMConfig;
import cn.richinfo.dm.DMSDK;
import cn.richinfo.dm.constance.PrivateConstance;
import cn.richinfo.dm.constance.PublicConstance;
import cn.richinfo.dm.net.UrlConstant;
import cn.richinfo.dm.net.encyptrequest.EncryptJsonRequest;
import cn.richinfo.dm.util.BusinessUtils;
import cn.richinfo.dm.util.DMDeviceInfoCollection;
import cn.richinfo.dm.util.LogUtils;
import cn.richinfo.dm.util.MD5;
import cn.richinfo.dm.util.MobileUtil;
import cn.richinfo.dm.util.SPUtils;
import cn.richinfo.dm.util.VolleyUtil;
import com.richinfo.mthttp.Constant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class O {

    /* renamed from: a, reason: collision with root package name */
    private static O f20a;
    private static String b;
    private Context c;

    private O(Context context) {
        this.c = context;
        b = this.c.getFilesDir().getPath() + PrivateConstance.DYNAMIC_PATH;
    }

    private cn.richinfo.dm.bean.o a(JSONObject jSONObject) {
        LogUtils.debug(PublicConstance.LOG_TAG_UP, "getUpdateInfoFromJson:" + jSONObject);
        cn.richinfo.dm.bean.o oVar = new cn.richinfo.dm.bean.o();
        if (jSONObject.has("version")) {
            oVar.d(jSONObject.getString("version"));
        }
        if (jSONObject.has("versionName")) {
            oVar.e(jSONObject.getString("versionName"));
        }
        if (jSONObject.has("filePath")) {
            oVar.b(jSONObject.getString("filePath"));
        }
        if (jSONObject.has("md5")) {
            oVar.c(jSONObject.getString("md5"));
        }
        if (jSONObject.has("size")) {
            oVar.a(jSONObject.getInt("size"));
        }
        if (jSONObject.has("feature")) {
            oVar.a(jSONObject.getString("feature"));
        }
        return oVar;
    }

    private void a(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                outputStream.flush();
                return;
            }
            outputStream.write(bArr, 0, read);
        }
    }

    public static int b(String str) {
        if (TextUtils.isEmpty(str) || str.lastIndexOf(".") < 5) {
            return -1;
        }
        return Integer.parseInt(str.substring(str.lastIndexOf(".") + 1));
    }

    public static final synchronized O b(Context context) {
        O o;
        synchronized (O.class) {
            if (f20a == null) {
                f20a = new O(context);
            }
            o = f20a;
        }
        return o;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(JSONObject jSONObject) {
        if (jSONObject.has("sdkUpdate")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("sdkUpdate");
            if (jSONObject2.has("isOpen")) {
                if (!"Y".equals(jSONObject2.getString("isOpen"))) {
                    a(this.c);
                } else if (jSONObject2.has(PrivateConstance.CFG_PARAM_PARAM)) {
                    cn.richinfo.dm.bean.o a2 = a(jSONObject2.getJSONObject(PrivateConstance.CFG_PARAM_PARAM));
                    if (c(a2.c())) {
                        DMSDK.getExecutor().execute(new N(this, a2));
                    }
                }
            }
        }
    }

    private void c(JSONObject jSONObject) {
        String jSONObject2 = jSONObject.toString();
        LogUtils.debug(PublicConstance.LOG_TAG_UP, "params=" + jSONObject2);
        String str = DMSDK.getBaseAcceptUrl(this.c) + UrlConstant.URL_QUERY_SDK_UPDATE + g();
        LogUtils.debug(PublicConstance.LOG_TAG_UP, "url=" + str);
        VolleyUtil.getQueue(this.c).add(EncryptJsonRequest.getEncryptJsonRequest(1, str, jSONObject2, new L(this), new M(this, str)));
    }

    private boolean c(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(DMConfig.SDK_VERSION) && b(SPUtils.getDMPatchVersion(this.c)) < b(str);
    }

    private String d(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception unused) {
            return null;
        }
    }

    private JSONObject f() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("appKey", MobileUtil.getAppkey(this.c));
        jSONObject.put("func", "checkupdate");
        jSONObject.put("brand", DMDeviceInfoCollection.getBrand(this.c));
        jSONObject.put("model", DMDeviceInfoCollection.getModel(this.c));
        jSONObject.put("imei", DMDeviceInfoCollection.imei1(this.c));
        return jSONObject;
    }

    private String g() {
        StringBuilder sb = new StringBuilder();
        String str = System.currentTimeMillis() + "";
        sb.append("&brand=").append(DMDeviceInfoCollection.getBrand(this.c)).append("&model=").append(d(DMDeviceInfoCollection.getModel(this.c))).append("&imei=").append(d(DMDeviceInfoCollection.imei1(this.c))).append("&imei2=").append(d(DMDeviceInfoCollection.imei2(this.c))).append("&version=").append(d(DMConfig.PROTOCAL_VERSION)).append("&mac=").append(d(DMDeviceInfoCollection.macAddress(this.c))).append("&appkey=").append(d(MobileUtil.getAppkey(this.c))).append("&sdkver=").append(d(DMConfig.SDK_VERSION)).append("&timestr=").append(d(str)).append("&token=").append(MobileUtil.getVerifyMD5(this.c, str));
        return sb.toString();
    }

    public InputStream a(String str) {
        URL url;
        LogUtils.debug(PublicConstance.LOG_TAG_UP, "getInputStream");
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            LogUtils.err(PublicConstance.LOG_TAG_UP, e.getMessage());
            url = null;
        }
        if (url == null) {
            return null;
        }
        try {
            return ((HttpURLConnection) url.openConnection()).getInputStream();
        } catch (Exception e2) {
            LogUtils.err(PublicConstance.LOG_TAG_UP, e2.getMessage());
            return null;
        }
    }

    public void a() {
        if (b() != null) {
            b().delete();
        }
        SPUtils.saveDMPatchVersion(this.c, Constant.SP_MODEL);
    }

    public void a(Context context) {
        File file = new File(context.getFilesDir().getPath() + PrivateConstance.DYNAMIC_PATH);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file2 : listFiles) {
                    file2.delete();
                }
            }
            SPUtils.saveDMPatchVersion(context, Constant.SP_MODEL);
        }
    }

    public void a(cn.richinfo.dm.bean.o oVar) {
        if (TextUtils.isEmpty(oVar.a())) {
            return;
        }
        File file = new File(b + "/" + PrivateConstance.DYNAMIC_FILE_NAME);
        LogUtils.info(PublicConstance.LOG_TAG_UP, "下载地址=" + oVar.a());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                InputStream a2 = a(oVar.a());
                try {
                    a(a2, fileOutputStream);
                    if (a2 != null) {
                        a2.close();
                    }
                    fileOutputStream.close();
                } finally {
                }
            } finally {
            }
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_UP, "下载动态包异常：" + e.getMessage());
        }
    }

    public File b() {
        File file;
        File file2 = null;
        try {
            file = new File(c());
        } catch (Exception e) {
            e = e;
        }
        try {
            if (file.exists()) {
                if (file.length() != 0) {
                    return file;
                }
            }
            return null;
        } catch (Exception e2) {
            e = e2;
            file2 = file;
            LogUtils.err(PublicConstance.LOG_TAG_UP, e.getMessage());
            return file2;
        }
    }

    public void b(cn.richinfo.dm.bean.o oVar) {
        LogUtils.info(PublicConstance.LOG_TAG_UP, "保存动态更新包");
        File file = new File(b);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        if (listFiles.length > 0) {
            for (File file2 : listFiles) {
                file2.delete();
            }
        }
        a(oVar);
        String str = b + "/" + PrivateConstance.DYNAMIC_FILE_NAME;
        try {
            LogUtils.info(PublicConstance.LOG_TAG_UP, "动态包MD5: " + MD5.fileMD5(str));
            String b2 = oVar.b();
            if (TextUtils.isEmpty(b2) || !b2.equals(MD5.fileMD5(str))) {
                LogUtils.warn(PublicConstance.LOG_TAG_UP, "校验不通过");
                new File(str).delete();
                SPUtils.saveDMPatchVersion(this.c, Constant.SP_MODEL);
            } else {
                SPUtils.saveDMPatchVersion(this.c, oVar.c());
                SPUtils.saveDynamicMd5(this.c, b2);
            }
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_UP, e.getMessage());
        }
    }

    public String c() {
        return this.c.getFilesDir().getPath() + PrivateConstance.DYNAMIC_PATH + "/" + PrivateConstance.DYNAMIC_FILE_NAME;
    }

    public void d() {
        try {
            LogUtils.info(PublicConstance.LOG_TAG_UP, "查询动态更新配置信息");
            c(f());
        } catch (Exception e) {
            LogUtils.err(PublicConstance.LOG_TAG_UP, e.getMessage());
        }
    }

    public boolean e() {
        String str;
        LogUtils.info(PublicConstance.LOG_TAG_UP, "动态更新检查");
        if (!DMSDK.isDynamicReport()) {
            str = BusinessUtils.toggleCloseMsg(PublicConstance.LOG_TAG_UP, "setDynamicReport");
        } else {
            if (BusinessUtils.switchCheck(SPUtils.getDynamicLoadSwitch(this.c))) {
                return true;
            }
            str = PrivateConstance.LOG_MSG_WHITELIST;
        }
        LogUtils.warn(PublicConstance.LOG_TAG_UP, str);
        return false;
    }
}
