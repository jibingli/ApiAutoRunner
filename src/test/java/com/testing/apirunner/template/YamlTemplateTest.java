package com.testing.apirunner.template;

import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.*;

public class YamlTemplateTest {

    @Test
    public void testCreatRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("$..loginName", "13312341234");
        YamlTemplate loginTest = new YamlTemplate("template/template.yaml", map);
        System.out.println(loginTest.creatRequest());
    }
}