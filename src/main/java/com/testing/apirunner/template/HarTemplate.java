package com.testing.apirunner.template;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.testing.apirunner.model.RequestData;

import java.util.ArrayList;
import java.util.Map;

public class HarTemplate implements RequestTemplate {
    private String harPath;
    private String api;
    private Map<String, Object> map;
    private String URLRegex = "([http:|https:].*)/";

    public HarTemplate(String harPath, String api, Map<String, Object> map) {
        this.harPath = harPath;
        this.api = api;
        this.map = map;
    }

    @Override
    public RequestData creatRequest() {
        //todo: 待解析
        DocumentContext documentContext = JsonPath.parse(HarTemplate.class.getResourceAsStream(this.harPath));
        ArrayList<Map> request = documentContext.read("$..request");
        for (Map<String, Object> reqMap : request) {
            String url = (String) map.get("url");
        }
        System.out.println(request);
        return null;
    }

    public void getApiFromUrl() {

    }

}
