package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.RequestDTO;
import org.zkmaster.backend.exceptions.*;
import org.zkmaster.backend.services.ZKMainService;

import javax.servlet.http.HttpServletRequest;

/**
 * Provide main ZooKeeperMaster REST API: Default CRUD.
 */
@RestController
@RequestMapping("/api/zkm/data/{host}")
@CrossOrigin(value = {"*"})
public class CRUDController {
    ZKMainService zkMainService;

    @Autowired
    public CRUDController(@Qualifier("ZKMainServiceRWL") ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * CRUD - READ
     * HTTP - GET ALL
     * Meaning: get host-value.(YAML doc in path-tree format).
     */
    @GetMapping("")
    @Log
    public @ResponseBody
    ZKNode getHostValue(@PathVariable String host) throws WrongHostException {
        ZKNode rsl = null;
        if (zkMainService.containsConnection(host)) {
            rsl = zkMainService.getHostValue(host);
        } else if (zkMainService.createConnection(host)) {
            rsl = zkMainService.getHostValue(host);
        }
        return rsl;
    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * expect request body: {@link RequestDTO}
     * {
     * "path": String,
     * "value": String
     * }
     * Meaning: Create node in ZooKeeper. (Node == path && value).
     */
    @PostMapping("")
    @Log
    public @ResponseBody
    boolean createNode(@RequestBody RequestDTO dto,
                       @PathVariable String host) throws NodeExistsException {
        return zkMainService.createNode(host, dto.getPath(), dto.getValue());
    }

    /**
     * CRUD - UPDATE
     * HTTP - PUT
     * expect request body: {@link RequestDTO}
     * {
     * "path": String,
     * "value": String (example: "${newNodeName}&${newNodeValue}")
     * }
     * Meaning: Update node in ZooKeeper. (Node == path && value).
     */
    @PutMapping("")
    @Log
    public @ResponseBody
    boolean updateNode(@RequestBody RequestDTO dto,
                       @PathVariable String host) throws NodeSaveException, NodeRenameException {
        String[] nameAndValue = dto.getValue().split("&");
        return zkMainService.saveNode(host, dto.getPath(), nameAndValue[0], nameAndValue[1]);
    }

    /**
     * CRUD && HTTP - DELETE
     * Delete node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/**")
    @Log
    public @ResponseBody
    boolean deleteNode(@PathVariable String host, HttpServletRequest request) throws NodeDeleteException {
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = fullPath.substring(fullPath.indexOf(host) + host.length());
        return zkMainService.deleteNode(host, path);
    }

}
