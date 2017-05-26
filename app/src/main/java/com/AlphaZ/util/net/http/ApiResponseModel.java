package com.AlphaZ.util.net.http;

import java.util.List;

/**
 * ProjectName: YouChi
 * PackageName: com.YC.entity
 * User: C0dEr
 * Date: 2016-11-03
 * Time: 15:37
 * Description:
 */
public class ApiResponseModel {
    public int httpCode;
    public List errors;
    public Object data;
    public String message;
    public boolean isSuccess;
}
