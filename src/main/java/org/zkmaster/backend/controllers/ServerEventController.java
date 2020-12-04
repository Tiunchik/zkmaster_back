package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;
import org.zkmaster.backend.services.ZkMainService;

/**
 * Event Listener - lister all events from "real servers" and process it.
 *
 * TODO: Это сервис ^_^ или вообще в отдельную папку
 */
@Service
public class ServerEventController {
    ZkMainService zkMainService;

    @Autowired
    public ServerEventController(ZkMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    @EventListener
    public void eventProcess(EventServerStateChange serverStateChange) {
        zkMainService.refreshCache(serverStateChange.getHostUrl());
    }

    @EventListener
    public void serverClose(EventServerClose serverClose) {
        zkMainService.deleteConnectionAndCache(serverClose.getHostUrl());
    }
}
