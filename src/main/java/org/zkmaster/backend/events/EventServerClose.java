package org.zkmaster.backend.events;

public class EventServerClose {
    private final String hostUrl;

    public EventServerClose(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getHostUrl() {
        return hostUrl;
    }
}
