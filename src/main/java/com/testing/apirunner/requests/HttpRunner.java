package com.testing.apirunner.requests;

import com.testing.apirunner.model.RequestData;
import com.testing.apirunner.response.HttpResponse;
import com.testing.apirunner.template.RequestTemplate;
import com.testing.apirunner.utils.RunnerUtils;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public abstract class HttpRunner {
    private RestAssuredConfig config = RestAssured.config().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
    private static Logger logger = Logger.getLogger(HttpRunner.class);
    private RequestSpecification requestSpecification = given();
    private String message = "";
    private List<Filter> filterList;

    /**
     * 设置UTF-8编码，取消url encoding，发送HTTP请求
     */
    public HttpResponse send(RequestTemplate template) {
        this.filterList = getFilter();
        if (this.filterList != null && !this.filterList.isEmpty()) {
            requestSpecification.filters(filterList);
        }

        String hostname = getHostname();
        if (hostname == null) {
            throw new RuntimeException("Hostname must be provided!!");
        }
        RequestData requestData = template.creatRequest();
        String url = hostname + requestData.getApi();
        String method = requestData.getMethod();
        Map<String, Object> headers = requestData.getHeaders();
        Map<String, Object> queryParams = requestData.getQueryParams();
        Map<String, Object> data = requestData.getFormData();
        Map<String, Object> files = requestData.getMultiFiles();
        String json = requestData.getJsonBody();
        Map<String, Object> pathParams = requestData.getPathParams();

        this.message = method + " " + url;
        this.preparePath(pathParams)
                .prepareParams(queryParams)
                .prepareData(data)
                .prepareJson(json)
                .prepareFiles(files)
                .prepareHeaders(headers);
        Response response = requestSpecification.config(this.config).urlEncodingEnabled(true).request(method.toUpperCase(), url);
        String resp = response.asString().replaceAll("\\n", "").replaceAll("    ", "");
        if (resp.length() > 2000) {
            logger.debug(this.message + " | " + resp);
            resp = resp.substring(0, 1000);
        }

        logger.info(this.message + " | time: " + response.getTime() + "ms | response: " + resp);
        //reset requestSpecification
        this.requestSpecification = given();
        return new HttpResponse(response);
    }

    public abstract ArrayList<Filter> getFilter();

    public abstract String getHostname();

    private HttpRunner prepareJson(String json) {
        if (!Objects.isNull(json) && !json.equals("{}")) {
            requestSpecification.contentType(ContentType.JSON).body(json);
            this.message += " | json: " + json;
        }
        return this;
    }

    /**
     * query params参数请求拼接
     *
     * @return
     */
    private HttpRunner prepareParams(Map<String, Object> queryParams) {
        ;
        if (!Objects.isNull(queryParams) && !queryParams.isEmpty()) {
            requestSpecification.queryParams(queryParams);
            this.message += " | query: " + queryParams.toString();
        }
        return this;

    }

    /**
     * cookies params参数请求拼接
     *
     * @return
     */
    private HttpRunner prepareCookies(Map<String, Object> cookies) {
        ;
        if (!Objects.isNull(cookies) && !cookies.isEmpty()) {
            requestSpecification.cookies(cookies);
            this.message += " | cookies: " + cookies.toString();
        }
        return this;

    }

    /**
     * path params参数请求拼接
     *
     * @return
     */
    private HttpRunner preparePath(Map<String, Object> pathParams) {
        if (!Objects.isNull(pathParams) && !pathParams.isEmpty()) {
            requestSpecification.pathParams(pathParams);
            this.message += " | path: " + pathParams.toString();
        }
        return this;

    }

    /**
     * form params 参数请求拼接
     *
     * @return
     */
    private HttpRunner prepareData(Map<String, Object> data) {
        if (!Objects.isNull(data) && !data.isEmpty()) {
            requestSpecification.formParams(data);
            this.message += " | form: " + data.toString();
        }
        return this;
    }

    /**
     * headers 参数请求拼接
     *
     * @return
     */
    private HttpRunner prepareHeaders(Map<String, Object> headers) {
        if (!Objects.isNull(headers)) {
            requestSpecification.headers(headers);
        }
        return this;
    }

    /**
     * multi-part 参数请求拼接
     *
     * @return
     */
    private HttpRunner prepareFiles(Map<String, Object> files) {
        if (!Objects.isNull(files) && !files.isEmpty()) {
            for (Map.Entry<String, Object> entry : files.entrySet()) {
                File media = RunnerUtils.getMedia((String) entry.getValue());
                requestSpecification.multiPart(entry.getKey(), media);
                this.message += " | files: " + entry.getKey();
            }
        }
        return this;
    }
}
