package com.testing.apirunner.requests;

import com.testing.apirunner.template.YamlTemplate;
import io.restassured.parsing.Parser;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.*;

public class HttpRunnerTest {
    private HttpRunnerImpl httpRunner = new HttpRunnerImpl();

    @Test
    public void testSend() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("$..loginName", "13312341234");
        YamlTemplate loginTest = new YamlTemplate("template/template.yaml", map);
        System.out.println(httpRunner.send(loginTest).then().using().parser("application/vnd.uoml+xml", Parser.HTML).toString());
    }
}