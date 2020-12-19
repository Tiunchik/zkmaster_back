package org.zkmaster.backend.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;
import org.zkmaster.backend.services.ZKMainService;

/**
 * Event Listener - listens all events from "real servers" and process it.
 * @see org.zkmaster.backend.entity.ZKWatcherDefault
 */
@Component
public class ServerEventListenerDefault implements ServerEventListener {
    ZKMainService zkMainService;

    @Autowired
    public ServerEventListenerDefault(ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    @Override
    @Log
    public void eventProcess(EventServerStateChange serverStateChange) {
        zkMainService.refreshCache(serverStateChange.getHostUrl());
    }

    @Override
    @Log
    public void serverClose(EventServerClose serverClose) {
        zkMainService.deleteConnectionAndCache(serverClose.getHostUrl());
    }
}
