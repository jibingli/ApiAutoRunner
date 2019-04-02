package com.testing.apirunner.template;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.testing.apirunner.model.RequestData;
import com.testing.apirunner.utils.RunnerUtils;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;
import org.apache.log4j.Logger;

import java.util.*;

public class SwaggerTemplate implements RequestTemplate {
    private String path;
    private String operationId;
    private Map<String, Object> map;
    private static Logger logger = Logger.getLogger(SwaggerTemplate.class);

    public SwaggerTemplate(String path, String operationId, HashMap<String, Object> map) {
        this.path = path;
        this.operationId = operationId;
        this.map = map;
    }

    public RequestData creatRequest() {
        //todo  未解析完成:(

        return getData();

    }

    public RequestData getData() {
        String method = this.operationId.split("Using")[1].toLowerCase();
        if (method.contains("_")) {
            method = method.split("_")[0];
        }
        RequestData requestData = new RequestData();
        requestData.setMethod(method);
        HashMap<String, Object> headers = new HashMap<>();
        HashMap<String, Object> queryParams = new HashMap<>();
        HashMap<String, Object> formData = new HashMap<>();
        HashMap<String, Object> jsonBody = new HashMap<>();
        HashMap<String, Object> multiFile = new HashMap<>();

        String data = RunnerUtils.getFileString(this.path);
        Swagger swagger = new SwaggerParser().parse(data);
        Map<String, Path> paths = swagger.getPaths();
        try {
            lable:
            {
                for (Map.Entry<String, Path> entry : paths.entrySet()) {
                    String api = entry.getKey();
                    Path value = entry.getValue();
                    requestData.setApi(api);
                    List<Operation> operations = value.getOperations();
                    for (Operation operation : operations) {
                        if (operation.getOperationId().equals(this.operationId)) {
//                            List<String> consumes = operation.getConsumes();
                            List<Parameter> parameters = operation.getParameters();
//                            String consume = consumes.get(0);
                            for (Parameter parameter : parameters) {
                                String parameterName = parameter.getName();
                                String parameterIn = parameter.getIn();
                                boolean parameterRequired = parameter.getRequired();
                                //如果是body 需要遍历definition
                                if (parameter instanceof BodyParameter) {
                                    String definitionName = parameterName.substring(0, 1).toUpperCase() + parameterName.substring(1);
                                    Map<String, Model> definitions = swagger.getDefinitions();

                                    continue;
                                }
                                //not body
                                //required params must be provide!
                                if (parameterRequired && !map.containsKey(parameterName)) {
                                    throw new RuntimeException(parameterName + " is Required!");
                                } else if (!parameterRequired && !map.containsKey(parameterName)) {
                                    logger.warn("warning: " + parameterName + " is not provided!");
                                    // no value, loop continue
                                    continue;
                                }
                                // if value is provided:
                                Object valueFromMap = map.get(parameterName);
                                if (parameter instanceof HeaderParameter) {
                                    headers.put(parameterName, valueFromMap);
                                } else if (parameter instanceof QueryParameter) {
                                    if (method.equals("post")) {
                                        formData.put(parameterName, valueFromMap);
                                    } else {
                                        //TODO: post on url
                                        // qurey get -> queryParams
                                        queryParams.put(parameterName, valueFromMap);
                                    }
                                } else if (parameter instanceof FormParameter) {
                                    // file type
                                    multiFile.put(parameterName, RunnerUtils.getMedia((String) valueFromMap));
                                } else {
                                    logger.warn(api + " " + parameter + " is invalid!!");
                                }
                            }
                            break lable;
                        }
                    }
                }

                //can not find operationId, error!
                throw new RuntimeException("can not find this operationId: " + operationId);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        requestData.setFormData(formData);
        requestData.setHeaders(headers);
        requestData.setJsonBody(jsonBody.toString());
        requestData.setMultiFiles(multiFile);
        requestData.setQueryParams(queryParams);
        return requestData;
    }

    public void getJsonBody(String definitionName, Map<String, Object> definitions, Map<String, Object> jsonBody) {
        if (!definitions.containsKey(definitionName)) {
            throw new RuntimeException("cannot find definition: " + definitionName);
        }
        // todo definition model为list的情况
        ModelImpl definitionModel = (ModelImpl) definitions.get(definitionName);
        //properties
        Map<String, Property> bodyProperties = definitionModel.getProperties();
        //required
        List<String> modelRequired = definitionModel.getRequired();
        // 遍历body参数 if required then must be provided;
        Property property = bodyProperties.get("");
        bodyProperties.forEach((name, value) -> {
            String type = value.getType();


        });
//        while (iterator.hasNext()) {
//            String name = iterator.next();
//
//            if (map.containsKey(name)) {
//                jsonBody.put(name, map.get(name));
//                continue;
//            }
//            if (modelRequired != null && modelRequired.contains(name)) {
//                logger.error("jsonBody " + name + " is required!!");
//                throw new RuntimeException("jsonBody " + name + " is required!!");
//            }
//
//        }
    }
}
