package com.sovos.httpstatusmonitor;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MonitoredLogRequest {
    @JsonIgnore
    private int response_code;
    @JsonIgnore
    private int response_time;
    private String url;

    public MonitoredLogRequest() {
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_time(int response_time) {
        this.response_time = response_time;
    }

    public int getResponse_time() {
        return response_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
