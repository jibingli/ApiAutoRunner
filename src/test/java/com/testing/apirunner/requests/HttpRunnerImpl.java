package com.testing.apirunner.requests;

import io.restassured.filter.Filter;

import java.util.ArrayList;

public class HttpRunnerImpl extends HttpRunner {
    @Override
    public ArrayList<Filter> getFilter() {
        return null;
    }

    @Override
    public String getHostname() {
        return "http://localhost:8080";
    }

    @Override
    public String getHttpsProtocol() {
        return "TLS";
    }
}
