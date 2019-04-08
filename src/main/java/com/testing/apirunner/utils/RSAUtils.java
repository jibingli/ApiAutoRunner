package com.testing.apirunner.utils;


import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiangxuan on 2019/2/15.
 */
public class RSAUtils {/** */
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /** */
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /** */
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** */
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /** */
    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /** */
    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /** */
    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /** */
    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /** */
    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static PrivateKey getPrivateKey(String privatekeystr) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privatekeystr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /** */
    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        PublicKey publicK = getPublicKey(publicKey);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static PublicKey getPublicKey(String publicKeyStr) {
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (PublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** */
    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** */
    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /** */
    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }


    public static void main(String[] args) throws Exception {
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANQtkp176elAvXDuu6w7tYXeFSxUfNTDqvVt8R45eg0ud1HPsAyyB3ejCH/8p3sPuLx/YKbemW5IC7SXMfxzSB4Mq75G/NiMDwefJzgn1ClQkeZQIJ8bX4JVtK+biotrG6J57MybKDeeMNTsVl5tYCON7yW5AKehL6xQnoHTsefFAgMBAAECgYEAhfqM9SK0zpQRy4kEOrqtYe2dQxPKi3NOtZGJysMSfdZUg/V4PlwAeRwalu7MNtnzlht8xYIUMl4N/ifm6XlGFIUnvjPnjrx7RSyB/b0hRzbnFTQFe2ykhB9jhcSXWB1ER9jvSsfpDsfLfnPboJM3GRn43LQvELAv4aASyrGenLUCQQDpmVuDN7bLUM7NkNBR9BRXVKCaCNAE+ukm6Nz3Qklc1INiuvl72AxPvhbSvgqilju4awP1uMIU8I8oDzcJYuBDAkEA6IZZNy4AR5NZHibskfbLkv/knQdgOBn6ChVy8Rtflzeny9WigR9xa7geSrmDXUWQZ75moe7UbFEEho4f2ZL7VwJAP7hyiw+mkD1hvBdVjBVtewj9qibfP4yGDvQUWmo9gtIBaOwh49NiQFpU9XWbhxA+CCdA9EVKw9V+52mHFMtg6wJAdJyj83NjeoHgCKoWrGEr2Q3yNfoz/A6zAgmdumMy/mBQC36ZX85IEHCm6Gy+/7DaadzoFb/z0lqTPXPbbz8yQwJBALKzCPhyqmVkplVSpnlFNlBVJzxVRm46AM5+N7lrmV+AJi3AkIr2tKuu774sqzS44m8VHdl4MjlKowteajFeNpw=";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3zJILWQI9ps3ngqwFUlEcaJK70Skq1e4pL7aWIU3ltjO1zUkO5eL7xKf30ybBQ2dq5+jZsmC63hruijcDQ7GD3WBoKq2RQ3J6/+K9jae2cxXyxhDRjCVxFfwrYFM9jJhmqQLF/JtB7S19V//Xhjiw07BxZR9QOA/8Xlqh6m+oewIDAQAB";

        String sourceStr = "{\"order_date\":\"20190216123111\",\"bg_ret_url\":\"http://127.0.0.2:80\",\"mer_id\":\"20190215001\",\"card_no\":\"9558820200001234567\",\"idCard\":\"429005199308150152\",\"bank_name\":\"农业银行\",\"trans_amt\":\"0.01\",\"cust_name\":\"向旋\",\"bank_no\":\"10310000\",\"order_id\":\"pay2019012511554076164\",\"cmd_id\":\"1017\"}";
        sourceStr="{\"order_date\":\"20190305123339\",\"mer_id\":\"20190215001\",\"card_no\":\"6227001219170596371\",\"bank_phone\":\"13106644413\",\"identity_id\":\"fintek-dev10496\",\"idCard\":\"620103199011112050\",\"cust_name\":\"温霞瑶\",\"order_id\":\"2019030512333975434369\",\"cmd_id\":\"1023\"}";
        sourceStr="{\"mer_id\":\"20190215001\",\"order_id\":\"2019030512333975434369\",\"resp_code\":\"102302\",\"resp_desc\":\"访问受限\"}";
        System.out.println("加密前：" + sourceStr);
        byte[] encryptStrByte = RSAUtils.encryptByPublicKey(sourceStr.getBytes(), publicKey);
        byte[] btt = Base64.encodeBase64(encryptStrByte);
        String encryptContent = new String(btt);
        String sign = RSAUtils.sign(sourceStr.getBytes(), privateKey);
        System.out.println("加密完成：sign=" + sign);
        System.out.println("加密完成：data=" + encryptContent);
//
        String encryptStr = "cVSADHYj73CNm24qiY6vW+Midw8PRYU/0ATjwf7uytyVSUdZfSnQwg4M15Iqn2MhyRsv4PvHcdd+41YbnmyViBJe4Ozpa5KzOGl+kBbCKZd8bimoc+QJIDAWv7wM4PhpfMbi8R0MtqrgesCrrFmtPnuBv9eBn12z9SIAfou5KOw=";
        String encrypSign="FoJ3ubphNnahRIZKc/p2oUMkhJz0/Vr/k1AL5svpq3owMg0HrZllW2O6g5e461rXF09SZ2pM0OG5VpaGMsJVuqPnfOxbcAJRBFt8VhkDNP9TL3g8aDXugxwavM0Q028loP//5XOitdlJ1nKXRXem9rg9avIlRC2JlBgPVahl+Qg=";
//        encryptStr = encryptContent;
//        encrypSign=sign;
        System.out.println("解密前data：" + encryptStr);
        System.out.println("长度：" + encryptStr.length());
        // 私钥解密
        byte[] decryptStrByte = RSAUtils.decryptByPrivateKey(Base64.decodeBase64(encryptStr), privateKey);
        String sourceStr_1 = new String(decryptStrByte);
        System.out.println("解密后：" + sourceStr_1);
        System.out.println(RSAUtils.verify(sourceStr_1.getBytes(), publicKey, encrypSign));

    }
}


