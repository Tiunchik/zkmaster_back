package org.zkmaster.backend.services.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.DataImportFailException;

import java.util.List;
import java.util.Map;

/**
 * Service for import & export data into different formats.
 *
 * <p>Using {@param type} for pick correct {@link ZKNodeTransformer}.
 * and then use it to transform input data.
 */
@Service
public class TransformStrategy {
    private final Map<String, ZKNodeTransformer> transformerMap;

    /**
     * * Spring magic!!! Auto-@Autowired
     * Map injection:
     * key - bean name.
     * val - bean(found by type).
     */
    @Autowired
    public TransformStrategy(Map<String, ZKNodeTransformer> transformerMap) {
        this.transformerMap = transformerMap;
    }

    /**
     * Transform host-value into List<String>.
     *
     * @param hostValue host-value for export.
     * @param type      type for Export host-value.
     *                  * use for pick correct {@link ZKNodeTransformer}
     * @return exported {@param hostValue}.
     */
    @Log
    public List<String> exportHost(ZKNode hostValue, String type) {
        return get(type).exportHost(hostValue);
    }


    /**
     * Import data into {@param transaction}. Fill transaction by import data.
     *
     * @param transaction target-host transaction, for filling it up then commit.
     * @param host        target-host.
     *                    * use for throw Exception if something was wrong.
     * @param type        type for Export host-value.
     *                    * use for pick correct {@link ZKNodeTransformer}
     * @param data        import data.
     * @return Import success OR Throw Exception.
     * @throws DataImportFailException -
     */
    @Log
    public boolean importData(ZKTransaction transaction, String host,
                              String type, List<String> data) throws DataImportFailException {
        get(type).importData(data, transaction);
        return transaction.commit("Import failed: Transaction failed!",
                new DataImportFailException(host, type, data.size()));
    }

    /**
     * Pick correct {@link ZKNodeTransformer}.
     *
     * @param type transform type.
     * @return correct {@link ZKNodeTransformer}.
     */
    private ZKNodeTransformer get(String type) {
        ZKNodeTransformer rsl = transformerMap.get(type);
        if (rsl == null) {
            System.err.println("Wrong param(type) for TransformStrategy.get(type)");
            System.err.println("Accepted type==\"" + type + "\", Strategy contains type:");
            for (var each : transformerMap.keySet()) {
                System.err.println("EXCEPTION ExportStrategy :: key==\"" + each + '\"');
            }
            throw new IllegalStateException("Wrong TransformStrategy type");
        }
        return rsl;
    }


}
