package org.zkmaster.backend.services.transform;

import org.springframework.stereotype.Service;
import org.zkmaster.backend.controllers.TransformController;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.node.NodeReadException;

import java.util.List;

/**
 * Service for import & export data into different formats.
 *
 * <p>Using {@param type} for pick correct {@link ZKNodeTransformer}.
 * and then use it to transform input data.
 *
 * @see TransformController
 */
@Service
public interface TransformService {
    
    /**
     * Export host-value by provided params.
     * For: {@link TransformController}.
     *
     * @param host host for export.
     * @param type type for Export host-value.
     *             * use for pick correct {@link ZKNodeTransformer}
     * @return Export success OR throw Exception.
     *
     * @throws NodeReadException -
     */
    List<String> exportHost(String host, String nodePath, String type) throws NodeReadException;
    
    
    /**
     * Import content into {@param transaction}. Fill transaction by import content.
     *
     * @param host    target-host.
     *                * use for throw Exception if something was wrong.
     * @param type    type for Export host-value.
     *                * use for pick correct {@link ZKNodeTransformer}
     * @param content import content.
     * @return Import success OR Throw Exception.
     *
     * @throws HostProviderNotFoundException -
     * @throws DataImportFailException       -
     */
    boolean importData(String host, String nodePath, String type, List<String> content)
            throws DataImportFailException, HostProviderNotFoundException;

}
