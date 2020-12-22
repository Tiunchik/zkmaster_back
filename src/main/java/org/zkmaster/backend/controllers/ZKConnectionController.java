package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/zkm")
public class ZKConnectionController {

    ZKMainService zkMainService;

    @Autowired
    public ZKConnectionController(ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * HTTP - GET
     * url: /api/zkm/conn/check
     * expect request: {@link RequestDTO}
     * [
     * String, String, String ...
     * ]
     * Meaning: Check is this servers still alive?
     *
     * @return map with server status:
     * key - host
     * val - status(still it alive or not)
     */
    @GetMapping("/conn/check")
    @Log
    public @ResponseBody
    Map<String, Boolean> checkHostsHealth(
            @RequestBody List<String> hosts) {
        return zkMainService.checkHostsHealth(hosts);
    }


    /**
     * Meaning: Rename node in ZooKeeper.
     * HTTP - POST
     * expect request body: {@link RequestDTO}
     * {
     * "host": String == null.
     * "path": String -- absolute path of renaming node.
     * "value": String -- new (name || absolute path) of renaming node.
     * }
     * *** (absolute path) - is prefer!!!!
     * *** At this moment it work with (name) it's not so comfortable...
     */
    @PostMapping("/rename/{host}")
    @Log
    public @ResponseBody
    boolean rename(@RequestBody RequestDTO dto,
                   @PathVariable String host) {
        System.err.println("NOT SUPPORTED API IS USED!!!");
        return true;
    }

}
