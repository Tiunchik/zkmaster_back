package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.services.ZkMainService;

/**
 * Rest API: Default CRUD + connection
 */
@Controller
public class RestController {
    ZkMainService zkMainService;

    @Autowired
    public RestController(ZkMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * HTTP - PUT
     * url: /zkm/connection/{hostUrl}
     * Try create connection by {hostUrl}
     */
    public void connect(String hostUrl) {
        boolean rsl = zkMainService.createConnection(hostUrl);
    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * url: /zkm/data/{hostUrl}, {path}, {value}
     * Create node in ZooKeeper. (Node == path && value).
     */
    public void createNode(String hostUrl, String path, String value) {
        boolean rsl = zkMainService.createNode(hostUrl, path, value);
    }

    /**
     * CRUD - READ
     * HTTP - GET
     * url: /zkm/data/{hostUrl}
     * Read server value.(YAML doc in path-tree format).
     */
    public void read(String hostUrl) {
        ZKNode rsl = zkMainService.getHostValue(hostUrl);
    }

    /**
     * CRUD - UPDATE
     * HTTP - PUT
     * url: /zkm/data/{hostUrl}, {path}, {value}
     * Update node in ZooKeeper. (Node == path && value).
     */
    public void update(String hostUrl, String path, String value) {
        boolean rsl = zkMainService.updateNode(hostUrl, path, value);
    }

    /**
     * CRUD - DELETE
     * HTTP - DELETE
     * url: /zkm/data/{hostUrl}, {path}
     * Update node in ZooKeeper. (Node == path).
     */
    public void delete(String hostUrl, String path) {
        boolean rsl = zkMainService.deleteNode(hostUrl, path);
    }

}
