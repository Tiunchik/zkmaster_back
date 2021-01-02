package org.zkmaster.backend.services.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.repositories.HostContextDefault;

import java.util.List;
import java.util.Map;

@Service
public class TransformServiceDefault implements TransformService {
    private final HostContextDefault ctx;
    private final Map<String, ZKNodeTransformer> transformerStrategy;

    /**
     * TODO - test it!!!!!!
     * * Spring magic!!! Auto-@Autowired
     * Map injection:
     * key - bean name.
     * val - bean(found by type).
     */
    @Autowired
    public TransformServiceDefault(HostContextDefault ctx,
                                   Map<String, ZKNodeTransformer> transformerStrategy) {
        this.ctx = ctx;
        this.transformerStrategy = transformerStrategy;
    }

    /**
     * Pick correct {@link ZKNodeTransformer}.
     *
     * @param type transform type.
     * @return correct {@link ZKNodeTransformer}.
     */
    private ZKNodeTransformer get(String type) {
        ZKNodeTransformer rsl = transformerStrategy.get(type);
        if (rsl == null) {
            System.err.println("Wrong param(type) for TransformStrategy.get(type)");
            System.err.println("Accepted type==\"" + type + "\", Strategy contains type:");
            for (var each : transformerStrategy.keySet()) {
                System.err.println("EXCEPTION ExportStrategy :: key==\"" + each + '\"');
            }
            throw new IllegalStateException("Wrong TransformStrategy type");
        }
        return rsl;
    }


    @Override
    @Log
    public List<String> exportHost(String host, String type) throws NodeReadException {
        return get(type).exportHost(ctx.getActualHostValue(host));
    }

    @Override
    @Log
    public boolean importData(String host, String type, List<String> data)
            throws DataImportFailException, HostProviderNotFoundException {

        ZKTransaction transaction = ctx.getHostProvider(host).transaction();
        get(type).importData(data, transaction);

        return transaction.commit("Import failed: Transaction failed!",
                new DataImportFailException(host, type, data.size()));
    }

}
