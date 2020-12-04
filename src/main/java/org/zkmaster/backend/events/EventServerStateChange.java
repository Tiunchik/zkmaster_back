package org.zkmaster.backend.events;

import org.springframework.context.ApplicationEvent;

public class EventServerStateChange extends ApplicationEvent {

    public EventServerStateChange(Object hostUrl) {
        super(hostUrl);
    }

    public String getHostUrl() {
        return (String) getSource();
    }
    
}
