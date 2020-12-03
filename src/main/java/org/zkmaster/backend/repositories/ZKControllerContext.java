package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKController;

import java.util.HashMap;
import java.util.Map;

public class ZKControllerContext {
    /**
     * key - server(host) url.
     * val - connection.
     *
     * Should be 3 object:
     * key - val - getAll()
     */
    private final Map<String, ZKController> controllers = new HashMap<>();

    public ZKController get(String hostUrl) {
        return controllers.get(hostUrl);
    }

    public void put(String hostUrl, ZKController controller) {
        controllers.put(hostUrl, controller);
    }
}
