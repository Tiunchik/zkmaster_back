package org.zkmaster.backend.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKWatcherDefault;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.services.MainService;

/**
 * Event Listener - listens all events from "real servers" and process it.
 * @see ZKWatcherDefault
 */
@Component
public class ServerEventListenerDefault implements ServerEventListener {
    MainService mainService;

    @Autowired
    public ServerEventListenerDefault(@Qualifier("mainServiceDefault") MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @Log
    public void eventProcess(EventServerStateChange serverStateChange) throws NodeReadException {
        mainService.refreshCache(serverStateChange.getHostUrl());
    }

    @Override
    @Log
    public void serverClose(EventServerClose serverClose) {
        mainService.deleteConnectionAndCache(serverClose.getHostUrl());
    }

}
