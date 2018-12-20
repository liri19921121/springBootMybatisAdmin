package com.admin.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 加密算法用于对 秘钥进行加密处理
 * Created by junjie.taojj on 3/9/17.
 */
public class EncryptUtil {
    private static final Logger log = LoggerFactory.getLogger(EncryptUtil.class);

    public static String sha256Encrypt(String raw){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            byte[] hash = digest.digest(("yhw"+raw).getBytes());
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error(" encrypt error", e);
            return null;
        }
    }

    public static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String getRandomString(String base, int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomNumeric(int length) {
        String base = "0123456789";
        return getRandomString(base, length);
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        return getRandomString(base, length);
    }

    public static String desEncrypt(String src, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec spec = new DESKeySpec(key.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            return Hex.encodeHexString(cipher.doFinal(src.getBytes()));
        } catch (Exception e) {
            return src;
        }
    }

    public static String decrypt(String src, String key) {
        try {
// DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
// 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
// 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
// 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
// Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
// 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
// 真正开始解密操作
            return new String(cipher.doFinal(Hex.decodeHex(src.toCharArray())));
        } catch (Exception e) {
            log.error("what fuck thing");
        }
        return "";
    }
}
