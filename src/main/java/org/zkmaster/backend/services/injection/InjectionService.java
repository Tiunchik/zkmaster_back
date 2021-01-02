package org.zkmaster.backend.services.injection;

import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.InjectionFailException;

/**
 * Inject Node from one real-server into other real-serve, by provided params.
 */
public interface InjectionService {

    boolean injectFromTo(InjectionDTO dto) throws InjectionFailException;

}
