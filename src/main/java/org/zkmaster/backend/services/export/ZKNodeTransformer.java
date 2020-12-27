package org.zkmaster.backend.services.export;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;

import java.util.List;

/**
 * Interface for Import & Export the host-value into different data format and back. Example:
 * export: {@link ZKNode} -> TXT content.
 * import: TXT content -> {@link ZKTransaction} // it straight import into real server.
 */
public interface ZKNodeTransformer {

    /**
     * Transform host-value into List<String>
     *
     * @param hostValue -
     * @return exported {@param hostValue}.
     */
    List<String> exportHost(ZKNode hostValue);

    /**
     * Import data into {@param transaction}. Fill transaction by imported data.
     *
     * @param data        import data.
     * @param transaction filled up by {@param data} transaction, ready to commit.
     */
    void importData(List<String> data, ZKTransaction transaction);

}
