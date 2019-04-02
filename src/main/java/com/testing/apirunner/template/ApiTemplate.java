package com.testing.apirunner.template;

import com.testing.apirunner.model.RequestData;

public class ApiTemplate implements RequestTemplate {
    private RequestData requestData;

    public ApiTemplate(RequestData requestData) {
        this.requestData = requestData;
    }

    @Override
    public RequestData creatRequest() {
        return this.requestData;
    }
}
