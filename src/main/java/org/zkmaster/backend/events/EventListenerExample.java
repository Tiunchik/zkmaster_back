package org.zkmaster.backend.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerExample {

    @Value("${logging}")
    private String str;

    public String getStr() {
        return str;
    }

    @EventListener(condition = "#{@eventListenerExample.str == 'yes'}")
    public void onApplicationEvent(MyEvent myEvent) {
        System.out.println(myEvent.getSource().toString());
    }



}
