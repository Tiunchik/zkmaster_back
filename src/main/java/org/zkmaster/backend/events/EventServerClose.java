package org.zkmaster.backend.events;

import org.springframework.context.ApplicationEvent;
import org.zkmaster.backend.entity.ZKWatcherDefault;
import org.zkmaster.backend.listeners.ServerEventListenerDefault;

/**
 * Event throws from {@link ZKWatcherDefault} to {@link ServerEventListenerDefault}.
 * Meaning: Real server is close and it to all ZKWatchers.
 */
public class EventServerClose extends ApplicationEvent {

    public EventServerClose(Object hostUrl) {
        super(hostUrl);
    }

    public String getHostUrl() {
        return (String) getSource();
    }
}
