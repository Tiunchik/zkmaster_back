package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/zkm/conn")
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
     *     String, String, String ...
     * ]
     * Meaning: Check is this servers still alive?
     *
     * @return map with server status:
     * key - host
     * val - status(still it alive or not)
     */
    @GetMapping("/check")
    @Log
    public @ResponseBody
    Map<String, Boolean> checkHostsHealth(
            @RequestBody List<String> hosts) {
        return zkMainService.checkHostsHealth(hosts);
    }

}
