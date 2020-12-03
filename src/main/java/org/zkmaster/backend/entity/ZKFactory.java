package org.zkmaster.backend.entity;

import org.apache.zookeeper.ZooKeeper;
import org.zkmaster.backend.repositories.ZKControllerContext;
import org.zkmaster.backend.repositories.ZKDataRepository;

import java.io.IOException;

public class ZKFactory {

    private final ZKControllerContext controllerContext;

    public ZKFactory(ZKControllerContext controllerContext) {
        this.controllerContext = controllerContext;
    }

    public ZKController getOrCreateController(String hostUrl) throws IOException {
        var rsl = controllerContext.get(hostUrl);
        if (rsl == null) {
            var watcher = new WatcherBackEnd(hostUrl);
            var zoo = new ZKServer(new ZooKeeper(hostUrl, 4000, watcher), hostUrl);
            var rep = new ZKDataRepository(zoo);
            var conn = new ZKConnection(zoo);
            rsl = new ZKController(zoo, conn, rep);
            controllerContext.put(hostUrl, rsl);
        }
        return rsl;
    }
}
