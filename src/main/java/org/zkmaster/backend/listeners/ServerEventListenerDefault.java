package org.zkmaster.backend.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;
import org.zkmaster.backend.services.ZkMainService;

/**
 * Event Listener - listens all events from "real servers" and process it.
 * @see org.zkmaster.backend.entity.ZKWatcherDefault
 */
@Component
public class ServerEventListenerDefault implements ServerEventListener {
    ZkMainService zkMainService;

    @Autowired
    public ServerEventListenerDefault(ZkMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    @Override
    public void eventProcess(EventServerStateChange serverStateChange) {
        zkMainService.refreshCache(serverStateChange.getHostUrl());
    }

    @Override
    public void serverClose(EventServerClose serverClose) {
        zkMainService.deleteConnectionAndCache(serverClose.getHostUrl());
    }
}
