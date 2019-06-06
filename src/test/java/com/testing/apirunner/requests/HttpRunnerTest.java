package com.testing.apirunner.requests;

import com.testing.apirunner.model.RequestData;
import com.testing.apirunner.template.ApiTemplate;
import com.testing.apirunner.template.RequestTemplate;
import com.testing.apirunner.template.YamlTemplate;
import com.testing.apirunner.utils.TimeHandler;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class HttpRunnerTest {
    private HttpRunnerImpl httpRunner = new HttpRunnerImpl();

    @Test(enabled = false)
    public void testSend() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("$..loginName", "13312341234");
        YamlTemplate loginTest = new YamlTemplate("template/template.yaml", map);
        System.out.println(httpRunner.send(loginTest).then().using().parser("application/vnd.uoml+xml", Parser.HTML).toString());
    }

    @Test
    public void testRest() {
        RequestSpecification requestSpecification = given();
        requestSpecification.relaxedHTTPSValidation("TLS");
        System.out.println(requestSpecification
//                .relaxedHTTPSValidation("TLS")
                .when()
                .request("GET", "https://staging.doku.com/VASimulator/PermataAction_payment.doku?instCode=123&virtualAccountNumber=8856046201037981&traceNumber=123456&date=0605111111&amount=20200&ccy=IDR&channel=channelcod")
                .then()
                .log().all()
                .using().parser("application/vnd.uoml+xml", Parser.HTML)
                .toString());
    }

    @Test
    public void testRest2(){
        RequestData requestData = new RequestData();
        requestData.setMethod("get");
        requestData.setApi("/{VASimulator}/PermataAction_payment.doku");
        requestData.setPathParams(new HashMap<String, Object>(){{
            put("VASimulator","VASimulator");
        }});
        requestData.setQueryParams(new HashMap<String, Object>() {{
            put("instCode", "123");
            put("traceNumber", "123456");
            put("date", "06051111");
            put("virtualAccountNumber", "8856046201037981");
        }});
        RequestTemplate template = new ApiTemplate(requestData);
        httpRunner.send(template).then().log().all();
    }
}