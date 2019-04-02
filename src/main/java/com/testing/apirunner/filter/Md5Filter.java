package com.testing.apirunner.filter;

import com.testing.apirunner.config.Configuration;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

public class Md5Filter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification req, FilterableResponseSpecification res, FilterContext filterContext) {
        String appKey = (String) Configuration.getInstance().getConfig("$.appKey");
        String token = (String) Configuration.getInstance().getContextMapping("token");
        String md5Str = "";

        Headers headers = req.getHeaders();
        Map<String, String> queryParams = req.getQueryParams();
        Map<String, String> formParams = req.getFormParams();
        if (token == null) {
            token = "";
        }
        if (headers.hasHeaderWithName("token")) {
            req.removeHeader("token");
        }
        req.header("token", token);

        Map<String, Object> map = new TreeMap<>();
        if (!Objects.isNull(queryParams)) {
            map.putAll(queryParams);
        }
        if (!Objects.isNull(formParams)) {
            map.putAll(formParams);
        }

        if (!map.isEmpty()) {
            List<String> md5List = new ArrayList<String>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                md5List.add(entry.getKey() + "=" + entry.getValue());
            }
            String newMd5Str = md5List.get(0);
            for (int i = 1; i < md5List.size(); i++) {
                newMd5Str = newMd5Str + "|" + md5List.get(i);
            }
            md5Str = newMd5Str;
        }
        String signMsg = DigestUtils.md5Hex(appKey + token + md5Str);
        if (headers.hasHeaderWithName("signMsg")) {
            req.removeHeader("signMsg");
        }
        req.header("signMsg", signMsg);

        Response response = filterContext.next(req, res);
        return response;
    }
}
