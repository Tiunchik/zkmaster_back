package org.zkmaster.events;

import org.springframework.context.ApplicationEvent;
import org.zkmaster.backend.entity.WatcherBackEnd;

public class MyEvent extends ApplicationEvent {

    public MyEvent(WatcherBackEnd watcher) {
        super(watcher);
    }

}
