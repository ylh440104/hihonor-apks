package cn.richinfo.dm.util;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class AesUtil {
    private static final String TAG = "AesUtil";
    private static String defautKey;
    public static AesUtil mAesUtil;

    static {
        System.loadLibrary("aes-jni");
    }

    public static String deCodeAES(String str, String str2) {
        try {
            return new String(decrypt(Base64.decode(str, 0), DigestUtils.md5(str2)));
        } catch (Exception unused) {
            return "";
        }
    }

    private static byte[] decrypt(byte[] bArr, byte[] bArr2) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKeySpec);
        return cipher.doFinal(bArr);
    }

    public static String encodeAES(String str, String str2) {
        return new String(Base64.encode(encrypt(str.getBytes("UTF-8"), DigestUtils.md5(str2)), 0));
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, secretKeySpec);
        return cipher.doFinal(bArr);
    }

    public static final synchronized AesUtil getInstance() {
        AesUtil aesUtil;
        synchronized (AesUtil.class) {
            if (mAesUtil == null) {
                mAesUtil = new AesUtil();
            }
            aesUtil = mAesUtil;
        }
        return aesUtil;
    }

    public String aesDecrypt(String str) {
        try {
            String keyFromJNI = keyFromJNI();
            defautKey = keyFromJNI;
            return deCodeAES(str, keyFromJNI);
        } catch (Exception e) {
            LogUtils.err(TAG, e.getMessage());
            return "";
        }
    }

    public String aesEncrypt(String str) {
        String keyFromJNI = keyFromJNI();
        defautKey = keyFromJNI;
        return encodeAES(str, keyFromJNI);
    }

    public native String keyFromJNI();
}
