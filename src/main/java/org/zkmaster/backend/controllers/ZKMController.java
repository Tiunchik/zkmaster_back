package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.services.ZkMainService;

/**
 * Provide main ZooKeeperMaster REST API: Default CRUD.
 */
@RestController
@RequestMapping("/api/zkm/data")
public class ZKMController {

    ZkMainService zkMainService;

    @Autowired
    public ZKMController(ZkMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * CRUD - READ
     * HTTP - GET ALL
     * url: /zkm/data/{hostUrl}
     * Read server value.(YAML doc in path-tree format).
     */
    @GetMapping("/{hostUrl}")
    public void getAllNodes(@RequestAttribute RequestDTO dto) {
        ZKNode rsl = zkMainService.getHostValue(dto.getHost());
    }

    /**
     * CRUD - READ
     * HTTP - GET
     * url: /zkm/data/{hostUrl}
     * Read server value.(YAML doc in path-tree format).
     */
    @GetMapping("/{hostUrl}/{path}")
    @Deprecated(since = "мне вот видится, что нам Нафиг не нужен get(oneNode- node children)"
            + "у нас может быть только getHostValue - фактически это всё что нужно.")
    public void getNode(@RequestAttribute RequestDTO dto) {
//        ZKNode rsl = zkMainService.getHostValue(dto.getHost());
    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * url: /zkm/data/{hostUrl}, {path}, {value}
     * Create node in ZooKeeper. (Node == path && value).
     */
    @PostMapping("/{hostUrl}/{path}/{value}")
    public void createNode(@RequestAttribute RequestDTO dto) {
        boolean rsl = zkMainService.createNode(dto.getHost(), dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - UPDATE
     * HTTP - PUT
     * url: /zkm/data/{hostUrl}, {path}, {value}
     * Update node in ZooKeeper. (Node == path && value).
     */
    @PutMapping("/{hostUrl}/{path}/{value}")
    public void updateNode(@RequestAttribute RequestDTO dto) {
        boolean rsl = zkMainService.updateNode(dto.getHost(), dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - DELETE
     * HTTP - DELETE
     * url: /zkm/data/{hostUrl}, {path}
     * Update node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/{hostUrl}/{path}")
    public void deleteNode(@RequestAttribute RequestDTO dto) {
        boolean rsl = zkMainService.deleteNode(dto.getHost(), dto.getPath());
    }
}
