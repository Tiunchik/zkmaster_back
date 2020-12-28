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
    ZKMainService mainService;

    @Autowired
    public CRUDController(@Qualifier("ZKMainServiceFresh") ZKMainService mainService) {
        this.mainService = mainService;
    }

    /**
     * CRUD - READ
     * HTTP - GET ALL
     * Meaning: get host-value.(YAML doc in path-tree format).
     */
    @GetMapping("")
    @Log
    public @ResponseBody
    ZKNode getHostValue(@PathVariable String host) throws WrongHostAddressException {
        ZKNode rsl = null;
        if (mainService.containsConnection(host)) {
            rsl = mainService.getHostValue(host);
        } else if (mainService.createConnection(host)) {
            rsl = mainService.getHostValue(host);
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
                       @PathVariable String host) throws NodeExistsException, HostNotFoundException {
        return mainService.createNode(host, dto.getPath(), dto.getValue());
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
                       @PathVariable String host) throws NodeSaveException, NodeRenameException, HostNotFoundException {
        String[] nameAndValue = dto.getValue().split("&");
        String rslValue = (nameAndValue.length == 1) ? "" : nameAndValue[1];
        return mainService.saveNode(host, dto.getPath(), nameAndValue[0], rslValue);
    }

    /**
     * CRUD && HTTP - DELETE
     * Delete node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/**")
    @Log
    public @ResponseBody
    boolean deleteNode(@PathVariable String host, HttpServletRequest request) throws NodeDeleteException, HostNotFoundException {
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = fullPath.substring(fullPath.indexOf(host) + host.length());
        return mainService.deleteNode(host, path);
    }

}
