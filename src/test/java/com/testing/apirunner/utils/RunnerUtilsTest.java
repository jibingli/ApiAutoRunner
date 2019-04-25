package com.testing.apirunner.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class RunnerUtilsTest {

    @Test
    public void testGetStream() {
        InputStream stream = RunnerUtils.getStream("template/template.swagger");
        System.out.println(stream.toString());
    }

    @Test
    public void testGetFile() {
        File file = RunnerUtils.getFile("template/template.swagger");
        System.out.println(file);
    }

    @Test
    public void testGetMedia() {
    }

    @Test
    public void testGetFileString() {
        String fileString = RunnerUtils.getFileString("template/template.swagger");
        System.out.println(fileString);
    }

    @Test
    public void testGetListFromCsv() {
        Object[][] listFromCsv = RunnerUtils.getListFromCsv("data/login.csv");
        for (Object[] objects : listFromCsv) {
            System.out.println("-----------");
            for (Object object : objects) {
                System.out.println(object);
            }
        }
    }

    @Test(dataProvider = "getData")
    public void testGetMapFromCsv(Map<String, Object> map) {
        System.out.println("------");
        map.forEach((k, v) -> {
            System.out.println(k + "=" + v);
        });

    }

    @DataProvider
    public Iterator<Object[]> getData() {
        return RunnerUtils.getMapFromCsv("data/login.csv");
    }

    @Test
    public void testGetFileBase64() {
        System.out.println(RunnerUtils.getFileBase64("./media/liveImg.jpg"));
    }

    @Test
    public void testBase64() {
        String strImg = getImageStr("/Users/jibing/workspace/ApiAutoRunner/src/main/resources/media/identityImg.jpg");
        System.out.println(strImg);
//        generateImage(strImg, "/Users/jibing/workspace/ApiAutoRunner/src/main/resources/media/identityImg.jpg");
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}