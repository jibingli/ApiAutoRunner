package com.testing.apirunner.template;

import org.testng.annotations.Test;

import java.util.HashMap;

public class SwaggerTemplateTest {

    @Test
    public void testCreatRequest() {
        // multi-file form data
        HashMap<String, Object> map = new HashMap<>();
        map.put("idNo", "idNo");
        map.put("realName", "realName");
        map.put("identityImg", "identityImg.jpg");
        map.put("liveImg", "liveImg.jpg");
        new SwaggerTemplate("template/template.swagger", "identityUsingPOST", map).creatRequest();
    }

    @Test
    public void testCreatRequestByBody() {
        // json body
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelCode", "channelCode");
        map.put("invitationCode", "invitationCode");
        map.put("loginName", "loginName");
        map.put("registerAddr", "registerAddr");
        map.put("registerClient", "registerClient");
        map.put("registerCoordinate", "registerCoordinate");
        map.put("vcode", "vcode");
        new SwaggerTemplate("template/template.swagger", "registerUsingPOST", map).creatRequest();
    }

    @Test
    public void testCreatRequestByGetNoParams() {
        // get without queryParams
        HashMap<String, Object> map = new HashMap<>();
        new SwaggerTemplate("template/template.swagger", "listUsingGET_1", map).creatRequest();
    }

    //Test query get

    //Test query postData

}