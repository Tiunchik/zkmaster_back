package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.services.ZkMainService;

/**
 * Rest API: Default CRUD + connection
 *
 * TODO: сделать DTO для маппинга с фронта
 */
@RestController
@RequestMapping("/api")
public class ZKController {

    ZkMainService zkMainService;

    @Autowired
    public ZKController(ZkMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * CRUD - READ
     * HTTP - GET
     * url: /zkm/data/{hostUrl}
     * Read server value.(YAML doc in path-tree format).
     */
    @GetMapping("/zkmaster/{hostUrl}")
    public void getAllNodes(String hostUrl) {
        ZKNode rsl = zkMainService.getHostValue(hostUrl);
    }

    /**
     * CRUD - READ
     * HTTP - GET
     * url: /zkm/data/{hostUrl}
     * Read server value.(YAML doc in path-tree format).
     */
    @GetMapping("/zkmaster/{hostUrl}/{path}")
    public void getNode(String hostUrl, String path) {
        ZKNode rsl = zkMainService.getHostValue(hostUrl);
    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * url: /zkm/data/{hostUrl}, {path}, {value}
     * Create node in ZooKeeper. (Node == path && value).
     */
    @PostMapping("/zkmaster/{hostUrl}")
    public void createNode(String hostUrl, String path, String value) {
        boolean rsl = zkMainService.createNode(hostUrl, path, value);
    }

    /**
     * CRUD - UPDATE
     * HTTP - PUT
     * url: /zkm/data/{hostUrl}, {path}, {value}
     * Update node in ZooKeeper. (Node == path && value).
     */
    @PutMapping("/zkmaster/{hostUrl}")
    public void updateNode(String hostUrl, String path, String value) {
        boolean rsl = zkMainService.updateNode(hostUrl, path, value);
    }

    /**
     * CRUD - DELETE
     * HTTP - DELETE
     * url: /zkm/data/{hostUrl}, {path}
     * Update node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/zkmaster/{hostUrl}")
    public void deleteNode(String hostUrl, String path) {
        boolean rsl = zkMainService.deleteNode(hostUrl, path);
    }
}
