package cn.richinfo.mt.util;

import android.util.Base64;
import cn.richinfo.mt.MTSdk;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public final class AesCoder {
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final byte[] ivBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static AesCoder mAesCoder;

    static {
        System.loadLibrary("mt-jni");
    }

    private AesCoder() {
    }

    public static String decrypt(String str, String str2) {
        try {
            return new String(decrypt(generateKey(str), ivBytes, Base64.decode(str2, 2)), CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] decrypt(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(2, secretKeySpec, new IvParameterSpec(bArr));
        return cipher.doFinal(bArr2);
    }

    public static String encrypt(String str, String str2) {
        try {
            return Base64.encodeToString(encrypt(generateKey(str), ivBytes, str2.getBytes(CHARSET)), 2);
        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }

    public static byte[] encrypt(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(1, secretKeySpec, new IvParameterSpec(bArr));
        return cipher.doFinal(bArr2);
    }

    private static SecretKeySpec generateKey(String str) {
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = str.getBytes(CHARSET);
        messageDigest.update(bytes, 0, bytes.length);
        return new SecretKeySpec(messageDigest.digest(), "AES");
    }

    public static final synchronized AesCoder getInstance() {
        AesCoder aesCoder;
        synchronized (AesCoder.class) {
            if (mAesCoder == null) {
                mAesCoder = new AesCoder();
            }
            aesCoder = mAesCoder;
        }
        return aesCoder;
    }

    public String encrypt(String str) {
        try {
            return encrypt(keyFromJNI(), str);
        } catch (Exception unused) {
            return str;
        }
    }

    public String encrypt2(String str) {
        try {
            return "A@#@" + encrypt(keyFromJNI(), str) + "@#@" + SPUtil.getAppId(MTSdk.getApplication()) + "@#@" + SPUtil.getImei(MTSdk.getApplication()) + "@#@v2.2@#@" + MtykDeviceInfo.getInstance(MTSdk.getApplication()).getDmId();
        } catch (Exception unused) {
            return str;
        }
    }

    public native String keyFromJNI();
}
