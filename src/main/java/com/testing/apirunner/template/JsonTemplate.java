package com.testing.apirunner.template;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.testing.apirunner.model.RequestData;
import com.testing.apirunner.utils.RunnerUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonTemplate implements RequestTemplate {
    public String path;
    public HashMap<String, Object> map;

    public JsonTemplate(String path, HashMap<String, Object> map) {
        this.path = path;
        this.map = map;
    }

    @Override
    public RequestData creatRequest() {
        DocumentContext documentContext = JsonPath.parse(RunnerUtils.getStream(this.path));
        if (map != null) {
            map.forEach((key, value) -> {
                if (!key.startsWith("$")) {
                    key = "$.." + key;
                }
                ArrayList<Object> data = documentContext.read(key);
                if (data.size() == 0) {
                    throw new RuntimeException("path error: " + key);
                }
                documentContext.set(key, value);
            });
        }
        RequestData requestData = JSONObject.parseObject(documentContext.jsonString(), RequestData.class);
        return requestData;
    }
}
