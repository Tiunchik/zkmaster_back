package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
     * HTTP - POST
     * url: /api/zkm/conn/create
     * expect request body: {@link RequestDTO}
     * {
     *      "host": String,
     *      "path": null,
     *      "value": null
     * }
     * Meaning: Try create connection by hosts and send connection status
     *
     * @return connection create: success OR fail.
     */
    @PostMapping("/create")
    public @ResponseBody
    boolean createConnection(@RequestBody RequestDTO dto) {
        System.err.println("ConnectionController POST: dto = " + dto);

        return zkMainService.createConnection(dto.getHost());
    }

    /**
     * HTTP - GET
     * url: /api/zkm/conn/check
     * expect request: {@link RequestDTO}
     * [
     *     String, String, String ...
     * ]
     *
     * Meaning: Check is this servers still alive?
     * @return map with server status:
     * key - host
     * val - status(still it alive or not)
     */
    @GetMapping("/check")
    public @ResponseBody
    Map<String, Boolean> checkHostsHealth(
            @RequestBody List<String> hosts) {
        System.err.println("ConnectionController POST: hosts = " + hosts);

        return zkMainService.checkHostsHealth(hosts);
    }

}
