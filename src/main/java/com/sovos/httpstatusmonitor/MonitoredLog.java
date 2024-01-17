package com.sovos.httpstatusmonitor;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class MonitoredLog {
    private int response_code;
    private int response_time;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E, dd MMM yyyy HH:mm:ss z", timezone = "GMT-3")
    private Timestamp created_at;

    public MonitoredLog() {
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public int getResponse_time() {
        return response_time;
    }

    public void setResponse_time(int response_time) {
        this.response_time = response_time;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
