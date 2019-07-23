package com.testing.apirunner.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;


@Data
public class RequestData {
    public String host;
    public String api;
    public String method;
    public Map<String, Object> Headers;
    public Map<String, Object> formData;
    public Map<String, Object> queryParams;
    public Map<String, Object> pathParams;
    public Map<String, Object> multiFiles;
    public String jsonBody;


    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }


    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    public void setJsonBody(JSONObject jsonObject) {
        this.jsonBody = jsonObject.toJSONString();
    }
}
