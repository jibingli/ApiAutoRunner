package com.testing.apirunner.model;

import lombok.Data;

import java.util.Map;


@Data
public class RequestData {
    public String api;
    public String method;
    public Map<String, Object> Headers;
    public Map<String, Object> formData;
    public Map<String, Object> queryParams;
    public Map<String, Object> pathParams;
    public Map<String, Object> multiFiles;
    public String jsonBody;
}
