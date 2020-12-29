package org.zkmaster.backend.services.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.DataImportFailException;

import java.util.List;
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

    @Log
    public List<String> exportHost(ZKNode root, String type) {
        return get(type).exportHost(root);
    }

    @Log
    public boolean importData(ZKTransaction transaction, String host,
                              String type, List<String> data) throws DataImportFailException {
        get(type).importData(data, transaction);
        return transaction.commit("Import failed: Transaction failed!",
                new DataImportFailException(host, type, data.size()));
    }

    @Deprecated(since = "it should be private method.")
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
