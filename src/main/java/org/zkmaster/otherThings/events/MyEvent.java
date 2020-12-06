package org.zkmaster.otherThings.events;

import org.springframework.context.ApplicationEvent;
import org.zkmaster.backend.entity.ZKWatcherDefault;

public class MyEvent extends ApplicationEvent {

    public MyEvent(ZKWatcherDefault watcher) {
        super(watcher);
    }

}
