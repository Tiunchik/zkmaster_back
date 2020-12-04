package org.zkmaster.backend.events;

import org.springframework.context.ApplicationEvent;

public class EventServerClose extends ApplicationEvent {

    public EventServerClose(Object hostUrl) {
        super(hostUrl);
    }

    public String getHostUrl() {
        return (String) getSource();
    }
}
