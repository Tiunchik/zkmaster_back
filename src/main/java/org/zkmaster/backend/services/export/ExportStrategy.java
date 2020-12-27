package org.zkmaster.backend.services.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.devutil.DevLog;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class ExportStrategy {
    @Autowired
    ApplicationContext applicationContext;
    private Map<String, Export> exportMap;

    @PostConstruct
    void init() {
        this.exportMap = Map.of(
                "TXT", applicationContext.getBean("exportClassic", Export.class)
        );
    }

    public Export get(String host) {
        Export rsl = exportMap.get(host);
        if (rsl == null) {
            System.err.println("Wrong Export type! Strategy contains type:");
            for (var each : exportMap.keySet()) {
                DevLog.print("ExportStrategy", "key", each);
            }
            throw new IllegalStateException("Wrong Export type");
        }
        return rsl;
    }

}
