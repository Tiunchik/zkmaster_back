package org.zkmaster.backend.exceptions;

import org.zkmaster.backend.entity.dto.InjectionDTO;

/**
 * Meaning: Injection failed.
 * Possible: Any problems with real-serve or something wrong with transaction.
 */
public class InjectionFailException extends Exception {

    public InjectionFailException(InjectionDTO dto) {
        super("ZKM EXCEPTION: InjectionFailException:" + System.lineSeparator()
                + " sourceHost=" + dto.getSourceHost() + System.lineSeparator()
                + " sourceNodePath=" + dto.getSourceNodePath() + System.lineSeparator()
                + " targetHost=" + dto.getTargetHost() + System.lineSeparator()
                + " targetNodePath=" + dto.getSourceNodePath() + System.lineSeparator()
                + " saveOldValues=" + dto.isUpdOldValues() + System.lineSeparator()
                + " injection is failed.");
    }

}
