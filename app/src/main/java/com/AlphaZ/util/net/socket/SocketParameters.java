package com.AlphaZ.util.net.socket;


/**
 * ProjectName: XSY
 * PackageName: com.AlphaZ.utils
 * User: C0dEr
 * Date: 2016-10-11
 * Time: 13:45
 * Description:
 */
public class SocketParameters {
    private String Url;
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUrl() {
        return Url;
    }

    public SocketParameters setUrl(String url) {
        Url = url;
        return this;
    }


}
