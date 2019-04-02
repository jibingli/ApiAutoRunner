package com.testing.apirunner.response;


import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HttpResponse {
    private Response response;

    public HttpResponse(Response response) {
        this.response = response;
    }

    public Object jsonPath(String path) {
        try {

            return response.jsonPath().get(path);
        } catch (JsonPathException e) {
            throw new RuntimeException("Failed to parse the JSON document: " + response.asString() + " by json path " + path);
        }
    }

    public Object xmlPath(String path) {
        try {
            return response.xmlPath().get(path);
        } catch (JsonPathException e) {
            throw new RuntimeException("Failed to parse the JSON document: " + response.asString() + " by xml path " + path);
        }
    }

    public ValidatableResponse then() {
        return response.then();
    }

    public String asString() {
        return response.asString();
    }
}
