package org.zkmaster.backend.services.transform;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;

import java.util.List;

/**
 * Interface for Import & Export host-value into different data format and back. Example:
 * export: {@link ZKNode} -> TXT content.
 * import: TXT content -> {@link ZKTransaction} // it straight import into real server.
 *
 * <p> It's work like injection  by transaction into server.
 */
public interface ZKNodeTransformer {

    /**
     * Export host-value into List<String>.
     *
     * @param hostValue host-value for export.
     * @return exported {@param hostValue}.
     */
    List<String> exportHost(ZKNode hostValue);

    /**
     * Import content into {@param transaction}. Fill transaction by import content.
     *
     * @param content        import content.
     * @param transaction filled up by {@param content} transaction, ready to commit.
     */
    void importData(String nodePath,List<String> content , ZKTransaction transaction);

}
