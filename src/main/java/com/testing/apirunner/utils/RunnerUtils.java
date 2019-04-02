package com.testing.apirunner.utils;

import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.*;

public class RunnerUtils {
    public static InputStream getStream(String path) {
        InputStream inputStream = RunnerUtils.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new RuntimeException(path + " 不存在!");
        }
        return inputStream;
    }

    /**
     * 获取file类型文件
     *
     * @param path 文件相对resources的路径
     * @return
     */
    public static File getFile(String path) {
        try {
            String imagePath = Objects.requireNonNull(RunnerUtils.class.getClassLoader().getResource(path).getPath());
            return new File(imagePath);
        } catch (NullPointerException e) {
            throw new RuntimeException("该文件不存在： " + path);
        }
    }

    /**
     * 获取resources media目录下的文件
     *
     * @param name 文件名
     * @return
     */
    public static File getMedia(String name) {
        return getFile("./media/" + name);
    }

    /**
     * 将文件base64加密后返回
     *
     * @param path path
     * @return
     */
    public static String getFileBase64(String path) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = getStream(path);
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

    /**
     * 将字符串BASE64 加密
     *
     * @param data data
     * @return
     */
    public static String getBase64ForString(String data) {
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.getBytes());
    }

    /**
     * 获取文件内容
     *
     * @param path path
     * @return
     */
    public static String getFileString(String path) {
        File file = getFile(path);
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new RuntimeException("该文件不存在： " + path);
        }

    }

    /**
     * from csv 获取二维数组
     * for data provider
     *
     * @param path path
     * @return
     */
    public static Object[][] getListFromCsv(String path) {
        List<Object[]> records = new ArrayList<Object[]>();
        String record;
        //设定UTF-8字符集，使用带缓冲区的字符输入流BufferedReader读取文件内容
        BufferedReader file = null;
        try {
            file = new BufferedReader(new InputStreamReader(getStream(path), "UTF-8"));

            //忽略读取CSV文件的标题行（第一行）
            file.readLine();
            //遍历读取文件中除第一行外的其他所有内容并存储在名为records的ArrayList中，每一行records中存储的对象为一个String数组
            while ((record = file.readLine()) != null) {
                String[] fields = record.replace(" ", "").split(",");
                records.add(fields);
            }
            //关闭文件对象
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(path + " is not exist!!");
        }
        //将存储测试数据的List转换为一个Object的二维数组
        Object[][] results = new Object[records.size()][];
        //设置二位数组每行的值，每行是一个Object对象
        for (int i = 0; i < records.size(); i++) {
            results[i] = records.get(i);
        }
        return results;
    }

    public static Iterator<Object[]> getMapFromCsv(String path) {
        List<Object> records = new ArrayList<Object>();
        try {
            BufferedReader file = new BufferedReader(new InputStreamReader(getStream(path), "UTF-8"));

            //忽略读取CSV文件的标题行（第一行）
            String nameLine = file.readLine();
            String[] nameList = nameLine.split(",");
            String record;
            //遍历读取文件中除第一行外的其他所有内容并存储在名为records的ArrayList中，每一行records中存储的对象为一个String数组
            while ((record = file.readLine()) != null) {
                HashMap<Object, Object> map = new HashMap<>();
                String[] fields = record.split(",");
                for (int i = 0; i < nameList.length; i++) {
                    map.put(nameList[i].trim(), fields[i].trim());
                }
                records.add(map);
            }
            //关闭文件对象
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(path + " is not exist!!");
        }


        List<Object[]> users = new ArrayList<Object[]>();
        records.forEach(k -> {
            users.add(new Object[]{k});
        });
        return users.iterator();
    }
}
