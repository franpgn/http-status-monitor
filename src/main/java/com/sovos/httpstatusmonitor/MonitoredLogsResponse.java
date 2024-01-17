package com.sovos.httpstatusmonitor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class MonitoredLogsResponse {
    private List<MonitoredLog> monitoredLogs = new ArrayList<MonitoredLog>();

    public MonitoredLogsResponse() {
    }

    public List<MonitoredLog> getMonitoredLogs() {
        return monitoredLogs;
    }

    public void addMonitoredLogs(MonitoredLog monitoredLog) {
        this.monitoredLogs.add(monitoredLog);
    }
}
