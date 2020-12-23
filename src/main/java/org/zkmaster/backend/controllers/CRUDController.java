package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.NodeDeleteException;
import org.zkmaster.backend.exceptions.NodeExistsException;
import org.zkmaster.backend.exceptions.NodeUpdateException;
import org.zkmaster.backend.exceptions.WrongHostException;
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

//    /**
//     * CRUD - READ
//     * HTTP - GET
//     * url: /zkm/data/{hostUrl}
//     * Read server value.(YAML doc in path-tree format).
//     */
//    @GetMapping("/{hostUrl}/{path}")
//    @Deprecated(since = "мне вот видится, что нам Нафиг не нужен get(oneNode- node children)"
//            + "у нас может быть только getHostValue - фактически это всё что нужно.")
//    public RequestDTO getNode(@PathVariable String hostUrl, @PathVariable String path) {
//        var dto = new RequestDTO(hostUrl, path);
//        ZKNode rsl = zkMainService.getHostValue(dto.getHost());
//        dto.setValue(rsl.getValue());
//        return dto;
//    }

    /**
     * CRUD - CREATE
     * HTTP - POST
     * expect request body: {@link RequestDTO}
     * {
     * "host": String == null,
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
     * "host": String == null,
     * "path": String,
     * "value": String
     * }
     * Meaning: Update node in ZooKeeper. (Node == path && value).
     */
    @PutMapping("")
    @Log
    public @ResponseBody
    boolean updateNode(@RequestBody RequestDTO dto,
                       @PathVariable String host) throws NodeUpdateException {
        return zkMainService.updateNode(host, dto.getPath(), dto.getValue());
    }

    /**
     * CRUD && HTTP - DELETE
     * Update node in ZooKeeper. (Node == path).
     */
    @DeleteMapping("/**")
    @Log
    public @ResponseBody
    boolean deleteNode(@PathVariable String host, HttpServletRequest request) throws NodeDeleteException {
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = fullPath.substring(fullPath.indexOf(host) + host.length());
        return zkMainService.deleteNode(host, path);
    }


    /* Old Version */


//    boolean deleteNode(@RequestBody RequestDTO dto) throws NodeDeleteException {
//        return zkMainService.deleteNode(dto.getHost(), dto.getPath());
////        return zkMainService.deleteNode(host, path);
//    }

//    //    @DeleteMapping("")
//    @DeleteMapping("/{path}")
//    @Log
//    public @ResponseBody
//    boolean deleteNode(@PathVariable String host,
//                       @PathVariable String path
////                       @RequestBody RequestDTO dto
//
//    ) throws NodeDeleteException {
//        return zkMainService.deleteNode(host, path);
////        return zkMainService.deleteNode(dto.getHost(), dto.getPath());
//    }
//    boolean deleteNode(@RequestBody RequestDTO dto) throws NodeDeleteException {
//        return zkMainService.deleteNode(dto.getHost(), dto.getPath());
////        return zkMainService.deleteNode(host, path);
//    }

}
