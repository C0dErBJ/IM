package com.AlphaZ.entity.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ProjectName: YouChi
 * PackageName: com.AlphaZ.entity
 * User: C0dEr
 * Date: 2016-11-04
 * Time: 10:53
 * Description:
 */
@ApiModel(description = "业务对象")
public class ResponseModel {
    @ApiModelProperty(value = "返回消息", required = true)
    public String message;
    @ApiModelProperty(value = "返回状态，0成功，1失败", required = true)
    public int statusCode;
    @ApiModelProperty(value = "返回的数据", required = true)
    public Object data;

    public ResponseModel(String message, int statusCode, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResponseModel() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
