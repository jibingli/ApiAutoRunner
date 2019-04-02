package com.testing.apirunner.template;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.testing.apirunner.model.RequestData;
import com.testing.apirunner.utils.RunnerUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class YamlTemplate implements RequestTemplate {
    private String yamlPath;
    private HashMap<String, Object> map;

    public YamlTemplate(String yamlPath, HashMap<String, Object> map) {
        this.yamlPath = yamlPath;
        this.map = map;
    }

    public YamlTemplate(String yamlPath) {
        this.yamlPath = yamlPath;
    }

    @Override
    public RequestData creatRequest() {
        Yaml yaml = new Yaml();
        JSONObject jsonObject = new JSONObject(yaml.load(RunnerUtils.getStream(yamlPath)));
        DocumentContext documentContext = JsonPath.parse(jsonObject);
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
