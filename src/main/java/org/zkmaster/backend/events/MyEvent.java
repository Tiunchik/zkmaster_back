package org.zkmaster.backend.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.entity.OurDefaultWatcher;

public class MyEvent extends ApplicationEvent {

    public MyEvent(OurDefaultWatcher watcher) {
        super(watcher);
    }

}
