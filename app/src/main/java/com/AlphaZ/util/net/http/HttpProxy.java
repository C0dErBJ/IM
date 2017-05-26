package com.AlphaZ.util.net.http;


import com.AlphaZ.util.string.StringHelper;
import com.AlphaZ.util.valid.ValideHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * ProjectName: XSY
 * PackageName: com.AlphaZ.utils
 * User: C0dEr
 * Date: 2016-10-11
 * Time: 11:28
 * Description:
 */
public class HttpProxy {
    private OkHttpClient client = null;//单例
    private static HttpProxy httpProxy = null;//单例
    private static Map<String, HttpParameters> parameters;
    protected Logger logger = Logger.getLogger(this.getClass());
    public HttpParameters ps;

    private HttpProxy() {

    }

    /**
     * Instance http proxy.
     *
     * @param apiName the api name
     * @return the http proxy
     */
    public static HttpProxy Instance(String apiName) {
        if (httpProxy == null) {
            httpProxy = new HttpProxy();
            httpProxy.client = new OkHttpClient();
            init();
        }
        httpProxy.ps = parameters.get(apiName) == null ? new HttpParameters() : parameters.get(apiName);

        return httpProxy;
    }

    private static void init() {

        try {
            byte[] json = Files.readAllBytes(Paths.get(HttpProxy.class.getResource("/apiConfig.json").toURI()));
            parameters = new ObjectMapper().readValue(new String(json), new TypeReference<Map<String, HttpParameters>>() {
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Do request api response model.
     *
     * @return the api response model
     */
    public ApiResponseModel DoRequest() {
        return DoRequest(null, null);
    }

    public ApiResponseModel DoRequest(DataFormatCallback dataFormatCallback) {
        return DoRequest(null, dataFormatCallback);
    }

    public ApiResponseModel DoRequest(HandleResponseCallback callback) {
        return DoRequest(callback, null);
    }

    /**
     * Do request api response model.
     *
     * @param callback the callback
     * @return the api response model
     */
    public ApiResponseModel DoRequest(HandleResponseCallback callback, DataFormatCallback dataFormatCallback) {
        if (ValideHelper.isNullOrEmpty(ps.getUrl())) {
            return exception("url not correct");
        }
        if (!ValideHelper.isNullOrEmpty(ps.getMethod()) && ps.getMethod().toUpperCase().equals("GET")) {
            return DoGet(ps, callback, dataFormatCallback);
        }
        if (!ValideHelper.isNullOrEmpty(ps.getMethod()) && ps.getMethod().toUpperCase().equals("POST")) {
            return DoPost(ps, callback, dataFormatCallback);
        }
        if (!ValideHelper.isNullOrEmpty(ps.getMethod()) && ps.getMethod().toUpperCase().equals("DELETE")) {
            return DoDelete(ps, callback, dataFormatCallback);
        }
        if (!ValideHelper.isNullOrEmpty(ps.getMethod()) && ps.getMethod().toUpperCase().equals("PUT")) {
            return DoPut(ps, callback, dataFormatCallback);
        }
        return exception("404");
    }


    private ApiResponseModel DoGet(HttpParameters ps, HandleResponseCallback callback, DataFormatCallback dataFormatCallback) {
        try {
            Request.Builder request = new Request.Builder()
                    .url(UrlHandle(ps.getUrl(), ps.getParam()));
            if (!ValideHelper.isNullOrEmpty(ps.getHead())) {
                request.headers(Headers.of(ps.getHead()));
            }
            if (!ValideHelper.isNullOrEmpty(ps.getContentType())) {
                request.addHeader("Content-Type", ps.getContentType());
            }
            request.get();
            Response response = client.newCall(request.build()).execute();
            if (callback != null) {
                return callback.HandResponse(response);
            }
            return HandleResponse(response);
        } catch (Exception ex) {
            return exception(ex.getMessage());
        }
    }

    private ApiResponseModel DoPost(HttpParameters ps, HandleResponseCallback callback, DataFormatCallback dataFormatCallback) {
        try {
            Request.Builder request = new Request.Builder()
                    .url(UrlHandle(ps.getUrl(), ps.getParam().entrySet().stream().filter((k) -> k.getKey().contains("${")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
            if (!ValideHelper.isNullOrEmpty(ps.getHead())) {
                request.headers(Headers.of(ps.getHead()));
            }
            if (!ValideHelper.isNullOrEmpty(ps.getContentType())) {
                request.addHeader("Content-Type", ps.getContentType());
            }
            RequestBody requestBody = HandleBody(ps, dataFormatCallback);
            request.post(requestBody);
            Response response = client.newCall(request.build()).execute();
            if (callback != null) {
                return callback.HandResponse(response);
            }
            return HandleResponse(response);
        } catch (Exception ex) {
            return exception(ex.getMessage());
        }

    }

    private ApiResponseModel DoPut(HttpParameters ps, HandleResponseCallback callback, DataFormatCallback dataFormatCallback) {
        try {
            Request.Builder request = new Request.Builder()
                    .url(UrlHandle(ps.getUrl(), ps.getParam().entrySet().stream().filter((k) -> k.getKey().contains("${")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
            if (!ValideHelper.isNullOrEmpty(ps.getHead())) {
                request.headers(Headers.of(ps.getHead()));
            }
            if (!ValideHelper.isNullOrEmpty(ps.getContentType())) {
                request.addHeader("Content-Type", ps.getContentType());
            }
            RequestBody requestBody = HandleBody(ps, dataFormatCallback);
            request.put(requestBody);
            Response response = client.newCall(request.build()).execute();
            if (callback != null) {
                return callback.HandResponse(response);
            }
            return HandleResponse(response);
        } catch (Exception ex) {
            return exception(ex.getMessage());
        }

    }

    private ApiResponseModel DoDelete(HttpParameters ps, HandleResponseCallback callback, DataFormatCallback dataFormatCallback) {
        try {
            Request.Builder request = new Request.Builder()
                    .url(UrlHandle(ps.getUrl(), ps.getParam().entrySet().stream().filter((k) -> k.getKey().contains("${")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
            if (!ValideHelper.isNullOrEmpty(ps.getHead())) {
                request.headers(Headers.of(ps.getHead()));
            }
            if (!ValideHelper.isNullOrEmpty(ps.getContentType())) {
                request.addHeader("Content-Type", ps.getContentType());
            }
            RequestBody requestBody = HandleBody(ps, dataFormatCallback);
            request.delete(requestBody);
            Response response = client.newCall(request.build()).execute();
            if (callback != null) {
                return callback.HandResponse(response);
            }
            return HandleResponse(response);
        } catch (Exception ex) {
            return exception(ex.getMessage());
        }

    }

    private RequestBody HandleBody(HttpParameters ps, DataFormatCallback callback) {
        RequestBody requestBody = null;
        if (ValideHelper.isNullOrEmpty(ps.getContentType())) {
            if (!ValideHelper.isNullOrEmpty(ps.getParam())) {
                FormBody.Builder formBuilder = new FormBody.Builder();
                try {
                    for (Map.Entry<String, Object> map : ps.getParam().entrySet()) {
                        formBuilder.addEncoded(map.getKey(), new ObjectMapper().writeValueAsString(map.getValue()));
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                requestBody = formBuilder.build();
            }
        } else {
            if (callback != null) {
                requestBody = RequestBody.create(MediaType.parse(ps.getContentType()), callback.DataHandle(ps.getParam()));
            } else {
                try {
                    requestBody = RequestBody.create(MediaType.parse(ps.getContentType()), new ObjectMapper().writeValueAsString(ps.getParam()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return requestBody;
    }

    @SuppressWarnings("unchecked")
    private ApiResponseModel exception(String message) {
        ApiResponseModel model = new ApiResponseModel();
        model.errors = new ArrayList<HashMap<String, String>>();
        Map<String, String> map = new HashMap<>();
        map.put("error-message", message);
        model.errors.add(map);
        return model;
    }


    private String UrlHandle(String url, Map<String, Object> param) {
        if (ValideHelper.isNullOrEmpty(url)) {
            return "";
        }
        String vaildUrl = StringHelper.TrimEnd(StringHelper.TrimEnd(url, "\\"), "/");
        Map<String, Object> replaceParam = param.entrySet().stream()
                .filter((k) -> k.getKey().contains("${")).collect(Collectors.toMap(Map.Entry::getKey, b -> b.getValue() == null ? "" : b.getValue()));
        for (Map.Entry<String, Object> map : replaceParam.entrySet()) {
            vaildUrl = vaildUrl.replace(map.getKey(), map.getValue() != null ? map.getValue().toString() : "");
        }
        Map<String, Object> joinParam = param.entrySet().stream()
                .filter((k) -> !k.getKey().contains("${")).collect(Collectors.toMap(Map.Entry::getKey, b -> b.getValue() == null ? "" : b.getValue()));
        if (vaildUrl.contains("?")) {
            for (Map.Entry<String, Object> map : joinParam.entrySet()) {
                vaildUrl += "&" + map.getKey() + "=" + (map.getValue() != null ? map.getValue().toString() : "");
            }
        } else {
            for (Map.Entry<String, Object> map : joinParam.entrySet()) {
                vaildUrl += "&" + map.getKey() + "=" + (map.getValue() != null ? map.getValue().toString() : "");
            }
            vaildUrl = vaildUrl.replaceFirst("&", "?");
        }
        logger.info(vaildUrl);
        return vaildUrl;
    }

    /**
     * 内置HandleResponse方法，用于解析odl接口
     *
     * @param response
     * @return
     */
    private ApiResponseModel HandleResponse(Response response) {
        ApiResponseModel model = new ApiResponseModel();
        model.httpCode = response.code();
        String responseString = null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.data = responseString;
        return model;
    }

    /**
     * The interface Handle response callback.
     */
    public interface HandleResponseCallback {
        /**
         * Hand response api response model.
         *
         * @param response the response
         * @return the api response model
         */
        ApiResponseModel HandResponse(Response response);
    }

    public interface DataFormatCallback {
        String DataHandle(Object obj);
    }
}
