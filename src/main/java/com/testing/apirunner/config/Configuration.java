package com.testing.apirunner.config;


import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.testing.apirunner.utils.RunnerUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private static String path = "application.yaml";
    private Map<String, Object> userConfig;
    private DocumentContext documentContext;
    private Map<String, Object> contextMapping = new HashMap<>();
    private static Configuration instance = new Configuration();

    public static Configuration getInstance() {
        return instance;
    }

    private Configuration() {
        this.parseYaml();
    }

    private void parseYaml() {
        InputStream stream = RunnerUtils.getStream(path);
        Yaml yaml = new Yaml();
        JSONObject json = new JSONObject(yaml.load(stream));
        this.documentContext = JsonPath.parse(json);
    }

    public Object getConfig(String findPath) {
        Object data = this.documentContext.read(findPath);
        if (data == null) {
            throw new RuntimeException("Cannot find " + findPath);
        }
        return data;
    }

    public Object getContextMapping(String key) {
        return contextMapping.get(key);
    }

    public void setContextMapping(String key, Object value) {
        this.contextMapping.put(key, value);
    }

    public String getHostname() {
        return (String) this.getConfig("$.hostname");
    }

}
