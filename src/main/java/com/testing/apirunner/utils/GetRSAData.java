package com.testing.apirunner.utils;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

public class GetRSAData {
    //    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANQtkp176elAvXDuu6w7tYXeFSxUfNTDqvVt8R45eg0ud1HPsAyyB3ejCH/8p3sPuLx/YKbemW5IC7SXMfxzSB4Mq75G/NiMDwefJzgn1ClQkeZQIJ8bX4JVtK+biotrG6J57MybKDeeMNTsVl5tYCON7yW5AKehL6xQnoHTsefFAgMBAAECgYEAhfqM9SK0zpQRy4kEOrqtYe2dQxPKi3NOtZGJysMSfdZUg/V4PlwAeRwalu7MNtnzlht8xYIUMl4N/ifm6XlGFIUnvjPnjrx7RSyB/b0hRzbnFTQFe2ykhB9jhcSXWB1ER9jvSsfpDsfLfnPboJM3GRn43LQvELAv4aASyrGenLUCQQDpmVuDN7bLUM7NkNBR9BRXVKCaCNAE+ukm6Nz3Qklc1INiuvl72AxPvhbSvgqilju4awP1uMIU8I8oDzcJYuBDAkEA6IZZNy4AR5NZHibskfbLkv/knQdgOBn6ChVy8Rtflzeny9WigR9xa7geSrmDXUWQZ75moe7UbFEEho4f2ZL7VwJAP7hyiw+mkD1hvBdVjBVtewj9qibfP4yGDvQUWmo9gtIBaOwh49NiQFpU9XWbhxA+CCdA9EVKw9V+52mHFMtg6wJAdJyj83NjeoHgCKoWrGEr2Q3yNfoz/A6zAgmdumMy/mBQC36ZX85IEHCm6Gy+/7DaadzoFb/z0lqTPXPbbz8yQwJBALKzCPhyqmVkplVSpnlFNlBVJzxVRm46AM5+N7lrmV+AJi3AkIr2tKuu774sqzS44m8VHdl4MjlKowteajFeNpw=";
//    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALfMkgtZAj2mzeeCrAVSURxokrvRKSrV7ikvtpYhTeW2M7XNSQ7l4vvEp/fTJsFDZ2rn6NmyYLreGu6KNwNDsYPdYGgqrZFDcnr/4r2Np7ZzFfLGENGMJXEV/CtgUz2MmGapAsX8m0HtLX1X/9eGOLDTsHFlH1A4D/xeWqHqb6h7AgMBAAECgYEAox5gIsgM8BBAvw0+g76Jk8/PIfbANW8FXfIldln6Wzr364pUI2+socrnU09HHtAmUT+ebM4dgNqrRjbOGgyS1jYPUZW6KAVve4Lq/M7LECye9XYKuNwlT5tWOsCSk9/FenMqvGswb/glHhjRzgaxEvH1FdF6mVPsL2+44lSaKykCQQDl3ZnDp3hpfwVzvPYBn13PGe4us/6jxK3GdVcN+nBuILlptaUs69QdoJ1OTLJlMMtJfk1ymoO+dScDhm0x6cYfAkEAzLIr6jWaTa4JJt5eGmJOnZIsEU/hNEH9QKaM0AcwJkYo5eTz5AL9QUYd5nHfYuOPYb8kGVHiSCKMu1HmzN06JQJANPHIs+cD9hCaueDBLPh/C++mC73Lnf70I6ztQzv233bMHgwHooQjFDvlX56MzH/joubjgc0TITAsr0QsLH3y8wJBAJrnPlpoJBQi3uQeJUJ8IJgXtOeI9pjwUzFomGkY93QPZgXLhFGJfZO29wucIvuXz7qdxjivAbmrA6sB6NIhnE0CQDuh7OHoqkZM+62NfScyNjVtqPFDN4fjZfyLM3tYWy7NKolnC20SmfkBmxm3YKz9Gs2mViA5p1NZgsfu5Il2IjI=";
//    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDULZKde+npQL1w7rusO7WF3hUsVHzUw6r1bfEeOXoNLndRz7AMsgd3owh//Kd7D7i8f2Cm3pluSAu0lzH8c0geDKu+RvzYjA8Hnyc4J9QpUJHmUCCfG1+CVbSvm4qLaxuieezMmyg3njDU7FZebWAjje8luQCnoS+sUJ6B07HnxQIDAQAB";


    public static Map<String, String> getEncryptDataAndSign(Map<String, Object> buildMap, String publicKey, String privateKey) throws Exception {
        String sourceStr = JsonUtil.toString(buildMap);
        byte[] encryptStrByte = RSAUtils.encryptByPublicKey(sourceStr.getBytes(), publicKey);
        byte[] btt = Base64.encodeBase64(encryptStrByte);
        String encryptContent = new String(btt);
        String sign = RSAUtils.sign(sourceStr.getBytes(), privateKey);

        Map<String, String> result = new HashMap<>();
        result.put("data", encryptContent);
        result.put("sign", sign);
        return result;
    }

    public static String getDecryptData(String data, String sign, String publicKey, String privateKey) throws Exception {
        System.out.println("解密前data：" + data);
        System.out.println("长度：" + data.length());
        // 私钥解密
        byte[] decryptStrByte = RSAUtils.decryptByPrivateKey(Base64.decodeBase64(data), privateKey);
        String sourceStr_1 = new String(decryptStrByte);
        System.out.println("解密验签结果：" + RSAUtils.verify(sourceStr_1.getBytes(), publicKey, sign));
        if (RSAUtils.verify(sourceStr_1.getBytes(), publicKey, sign)) {
            return sourceStr_1;
        } else {
            return null;
        }
    }
}
