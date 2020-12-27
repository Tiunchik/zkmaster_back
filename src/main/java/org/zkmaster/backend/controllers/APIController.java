package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.RequestDTO;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/zkm")
public class APIController {
    ZKMainService zkMainService;

    @Autowired
    public APIController(@Qualifier("ZKMainServiceRWL") ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * HTTP - GET
     * Meaning: Check is this servers still alive?
     * expect request: {@link RequestDTO}
     * [
     * String, String, String ...
     * ]
     *
     * @return map with server status:
     * key - host
     * val - status(still it alive or not)
     */
    @GetMapping("/conn/check")
    @Log
    public @ResponseBody
    Map<String, Boolean> checkHostsHealth(@RequestBody List<String> hosts) {
        return zkMainService.checkHostsHealth(hosts);
    }

//    /**
//     * Meaning: Rename node in ZooKeeper.
//     * HTTP - POST
//     * expect request body: {@link RequestDTO}
//     * {
//     * "path": String -- absolute path of renaming node.
//     * "value": String -- new name of renaming node.
//     * }
//     */
//    @PostMapping("/rename/{host}")
//    @Log
//    @Deprecated
//    public @ResponseBody
//    boolean rename(@RequestBody RequestDTO dto,
//                   @PathVariable String host) throws NodeRenameException {
//        System.err.println("API IN BETA-TEST!!!");
////        zkMainService.renameNode(host, dto.getPath(), dto.getValue());
//        return false;
//    }

}
