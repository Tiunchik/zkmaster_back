package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.services.ZKMainService;

/**
 * Provide main ZooKeeperMaster REST API: Default CRUD.
 */
@RestController
@RequestMapping("/api/zkm/data")
public class ZKMController {

    ZKMainService zkMainService;

    @Autowired
    public ZKMController(ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * CRUD - READ
     * HTTP - GET ALL
     * url: /zkm/data/host-value
     * expect request body: {@link RequestDTO}
     * {
     *      "host": String,
     *      "path": null,
     *      "value": null
     * }
     * Meaning: get host-value.(YAML doc in path-tree format).
     */
    @GetMapping("/host-value")
    public @ResponseBody
    ZKNode getHostValue(@RequestBody RequestDTO dto) {
        System.err.println(dto);
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
    public void getNode(@RequestAttribute RequestDTO dto) {
//        ZKNode rsl = zkMainService.getHostValue(dto.getHost());
    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * url: /zkm/data/create
     * expect request body: {@link RequestDTO}
     * {
     *      "host": String,
     *      "path": String,
     *      "value": String
     * }
     * Meaning: Create node in ZooKeeper. (Node == path && value).
     */
    @PostMapping("/create")
    public @ResponseBody
    boolean createNode(@RequestBody RequestDTO dto) {
        return zkMainService.createNode(dto.getHost(), dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - UPDATE
     * HTTP - PUT
     * url: /zkm/data/update
     * expect request body: {@link RequestDTO}
     * {
     *      "host": String,
     *      "path": String,
     *      "value": String
     * }
     * Meaning: Update node in ZooKeeper. (Node == path && value).
     */
    @PutMapping("/update")
    public @ResponseBody
    boolean updateNode(@RequestBody RequestDTO dto) {
        return zkMainService.updateNode(dto.getHost(), dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - DELETE
     * HTTP - DELETE
     * url: /zkm/data/delete
     * expect request body: {@link RequestDTO}
     * {
     *      "host": String,
     *      "path": String,
     *      "value": null
     * }
     * Update node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/delete")
    public @ResponseBody
    boolean deleteNode(@RequestBody RequestDTO dto) {
        return zkMainService.deleteNode(dto.getHost(), dto.getPath());
    }
}
