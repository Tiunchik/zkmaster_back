package org.zkmaster.backend.listeners;

import org.springframework.context.event.EventListener;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;

public interface ServerEventListener {
    @EventListener
    void eventProcess(EventServerStateChange serverStateChange);

    @EventListener
    void serverClose(EventServerClose serverClose);

}
