package org.zkmaster.backend.events;

public class EventServerStateChange {
    private final String hostUrl;

    public EventServerStateChange(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getHostUrl() {
        return hostUrl;
    }
}
