package com.testing.apirunner.requests;

import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;

import java.util.ArrayList;

public class HttpRunnerImpl extends HttpRunner {
    @Override
    public ArrayList<Filter> getFilter() {
        return null;
    }

    @Override
    public String getHostname() {
        return "https://staging.doku.com";
    }

    @Override
    public RestAssuredConfig getRestConfig() {
        return null;
    }
}
