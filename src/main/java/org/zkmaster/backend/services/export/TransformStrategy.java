package org.zkmaster.backend.services.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransformStrategy {
    private final Map<String, ZKNodeTransformer> exportMap;

    /**
     * Map injection:
     * key - bean name.
     * val - bean(found by type).
     */
    @Autowired
    public TransformStrategy(Map<String, ZKNodeTransformer> exportMap) {
        this.exportMap = exportMap;
    }

    public ZKNodeTransformer get(String type) {
        ZKNodeTransformer rsl = exportMap.get(type);
        if (rsl == null) {
            System.err.println("Wrong param(type) for TransformStrategy.get(type)");
            System.err.println("Accepted type==\"" + type + "\", Strategy contains type:");
            for (var each : exportMap.keySet()) {
                System.err.println("EXCEPTION ExportStrategy :: key==\"" + each + '\"');
            }
            throw new IllegalStateException("Wrong TransformStrategy type");
        }
        return rsl;
    }

}
