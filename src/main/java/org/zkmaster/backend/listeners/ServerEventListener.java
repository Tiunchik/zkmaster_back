package org.zkmaster.backend.listeners;

import org.springframework.context.event.EventListener;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;
import org.zkmaster.backend.exceptions.node.NodeReadException;

public interface ServerEventListener {
    @EventListener
    void eventProcess(EventServerStateChange serverStateChange) throws NodeReadException;

    @EventListener
    void serverClose(EventServerClose serverClose);

}
