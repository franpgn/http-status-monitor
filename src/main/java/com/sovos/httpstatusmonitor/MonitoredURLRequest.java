package com.sovos.httpstatusmonitor;

public class MonitoredURLRequest {

    private String url;
    private String urlName;

    public MonitoredURLRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String url_name) {
        this.urlName = url_name;
    }
}
