package org.zkmaster.backend.services.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.utils.ZKNodes;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.repositories.HostContextDefault;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class TransformServiceDefault implements TransformService {
    private final HostContextDefault ctx;
    private final Map<String, ZKNodeTransformer> transformerStrategy;
    
    /**
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
            var errMsg = new StringJoiner(System.lineSeparator());
            errMsg.add("Wrong param(type) for TransformStrategy.get(type)");
            errMsg.add("Accepted type==\"" + type + "\", Strategy contains type:");
            for (var each : transformerStrategy.keySet()) {
                errMsg.add("EXCEPTION ExportStrategy :: key==\"" + each + '\"');
            }
            System.err.println(errMsg.toString());
            throw new IllegalStateException("Wrong TransformStrategy type");
        }
        return rsl;
    }
    
    
    @Override
    @Log
    public List<String> exportHost(String host, String nodePath, String type) throws NodeReadException {
        return get(type).exportHost(ZKNodes.getSubNode(ctx.getActualHostValue(host), nodePath));
    }
    
    @Override
    @Log
    public boolean importData(String host, String nodePath, String type, List<String> content)
            throws DataImportFailException, HostProviderNotFoundException {
        
        ZKTransaction transaction = ctx.getHostProvider(host).transaction();
        get(type).importData(nodePath, content, transaction);
        
        return transaction.commit("Import failed: Transaction failed!",
                new DataImportFailException(host, type, content.size()));
    }
    
}
