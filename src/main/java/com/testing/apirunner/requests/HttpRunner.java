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
    private RestAssuredConfig config = RestAssured.config()
            .encoderConfig(encoderConfig()
                    .defaultContentCharset("UTF-8"));
    private static Logger logger = Logger.getLogger(HttpRunner.class);
    private RequestSpecification requestSpecification = given();
    private StringBuilder message = new StringBuilder("");
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
        RestAssuredConfig restConfig = this.getRestConfig();
        if (!Objects.isNull(restConfig)) {
            this.requestSpecification.config(restConfig);
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

        this.message
                .append(method)
                .append(" ")
                .append(url);
        this.preparePath(pathParams)
                .prepareParams(queryParams)
                .prepareData(data)
                .prepareJson(json)
                .prepareFiles(files)
                .prepareHeaders(headers);
        Response response = this.requestSpecification
                .relaxedHTTPSValidation()
                .urlEncodingEnabled(true)
                .when()
                .request(method.toUpperCase(), url);
        String resp = response.asString().replaceAll("\\n", "").replaceAll("    ", "");
        if (resp.length() > 3000) {
            logger.debug(this.message + " | " + resp);
            resp = resp.substring(0, 2000);
        }

        logger.info(this.message + " | time: " + response.getTime() + "ms | response: " + resp);
        //reset requestSpecification
        this.requestSpecification = given();
        return new HttpResponse(response);
    }

    public abstract ArrayList<Filter> getFilter();

    public abstract String getHostname();

    public RestAssuredConfig getRestConfig() {
        return this.config;
    }

    private HttpRunner prepareJson(String json) {
        if (!Objects.isNull(json) && !json.equals("{}")) {
            requestSpecification.contentType(ContentType.JSON).body(json);
            this.message.append(" | json: ").append(json);
        }
        return this;
    }

    /**
     * query params 参数请求拼接
     *
     * @return
     */
    private HttpRunner prepareParams(Map<String, Object> queryParams) {

        if (!Objects.isNull(queryParams) && !queryParams.isEmpty()) {
            requestSpecification.queryParams(queryParams);
            this.message.append(" | query: ").append(queryParams.toString());
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
            pathParams.forEach((k, v) -> {
                String replaceStr = "{" + k + "}";
                String ms = this.message.toString().replace(replaceStr,String.valueOf(v));
                this.message = new StringBuilder(ms);
            });
            requestSpecification.pathParams(pathParams);
            this.message.append(" | path: ").append(pathParams.toString());
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
            this.message.append(" | form: ").append(data.toString());
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
                this.message.append(" | files: ").append(entry.getKey());
            }
        }
        return this;
    }
}
