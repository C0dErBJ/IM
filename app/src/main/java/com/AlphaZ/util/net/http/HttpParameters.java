package com.AlphaZ.util.net.http;


import java.util.HashMap;
import java.util.Map;

/**
 * ProjectName: XSY
 * PackageName: com.AlphaZ.utils
 * User: C0dEr
 * Date: 2016-10-11
 * Time: 13:45
 * Description:
 */
public class HttpParameters {
    private String Url;
    private String Method;
    private Map<String, String> Head;
    private Map<String, Object> Param;


    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    private String ContentType;

    public HttpParameters() {
        Head = new HashMap<>();
        Param = new HashMap<>();
        Method = "POST";

    }


    public String getUrl() {
        return Url;
    }

    public HttpParameters setUrl(String url) {
        Url = url;
        return this;
    }


    public String getMethod() {
        return Method;
    }

    public HttpParameters setMethod(String method) {
        Method = method;
        return this;
    }

    public Map<String, Object> getParam() {
        return Param;
    }

    public HttpParameters setParam(Map<String, Object> param) {
        Param.putAll(param);
        return this;
    }

    public Map<String, String> getHead() {
        return Head;
    }

    public HttpParameters setHead(Map<String, String> head) {
        Head.putAll(head);
        return this;
    }
}
