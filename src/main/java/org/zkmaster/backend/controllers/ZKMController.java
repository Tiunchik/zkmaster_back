package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.services.ZKMainService;

/**
 * Provide main ZooKeeperMaster REST API: Default CRUD.
 */
@RestController
@RequestMapping("/api/zkm")
public class ZKMController {

    ZKMainService zkMainService;

    @Autowired
    public ZKMController(ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * CRUD - READ
     * HTTP - GET ALL
     * url: /zkm/data
     * expect request body: {@link RequestDTO}
     * {
     * "host": String,
     * "path": null,
     * "value": null
     * }
     * Meaning: get host-value.(YAML doc in path-tree format).
     */
    @GetMapping("/{hostUrl}")
    @Log
    public @ResponseBody
    ZKNode getHostValue(@RequestBody RequestDTO dto, @PathVariable String hostUrl) {
        return zkMainService.getHostValue(dto.getHost());
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
    public RequestDTO getNode(@PathVariable String hostUrl, @PathVariable String path) {
        var dto = new RequestDTO(hostUrl, path);
        ZKNode rsl = zkMainService.getHostValue(dto.getHost());
        dto.setValue(rsl.getValue());
        return dto;
    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * url: /zkm/data
     * expect request body: {@link RequestDTO}
     * {
     * "host": String,
     * "path": String,
     * "value": String
     * }
     * Meaning: Create node in ZooKeeper. (Node == path && value).
     */
    @PostMapping("/{hostUrl}")
    @Log
    public @ResponseBody
    boolean createNode(@RequestBody RequestDTO dto, @PathVariable String hostUrl) {
        System.err.println("ZKMController POST: dto = " + dto);
        return zkMainService.createNode(dto.getHost(), dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - UPDATE
     * HTTP - PUT
     * url: /zkm/data
     * expect request body: {@link RequestDTO}
     * {
     * "host": String,
     * "path": String,
     * "value": String
     * }
     * Meaning: Update node in ZooKeeper. (Node == path && value).
     */
    @PutMapping("/{hostUrl}")
    @Log
    public @ResponseBody
    boolean updateNode(@RequestBody RequestDTO dto, @PathVariable String hostUrl) {
        System.err.println("ZKMController PUT: dto = " + dto);
        return zkMainService.updateNode(dto.getHost(), dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - DELETE
     * HTTP - DELETE
     * url: /zkm/data
     * expect request body: {@link RequestDTO}
     * {
     * "host": String,
     * "path": String,
     * "value": null
     * }
     * Update node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/{hostUrl}/{path}")
    @Log
    public @ResponseBody
    boolean deleteNode(@PathVariable String hostUrl, @PathVariable String path) {
        var dto = new RequestDTO(hostUrl, path);
        System.err.println("ZKMController DELETE: dto = " + dto);
        return zkMainService.deleteNode(dto.getHost(), dto.getPath());
    }

}
