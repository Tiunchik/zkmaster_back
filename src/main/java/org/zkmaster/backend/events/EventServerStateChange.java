package org.zkmaster.backend.events;

import org.springframework.context.ApplicationEvent;
import org.zkmaster.backend.entity.ZKWatcherDefault;
import org.zkmaster.backend.listeners.ServerEventListenerDefault;

/**
 * Event throws from {@link ZKWatcherDefault} to {@link ServerEventListenerDefault}.
 * Meaning: Server was updated and we need to re-fresh our view of server value.
 */
public class EventServerStateChange extends ApplicationEvent {

    public EventServerStateChange(Object hostUrl) {
        super(hostUrl);
    }

    public String getHostUrl() {
        return (String) getSource();
    }

}
