package com.testing.apirunner.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
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
}