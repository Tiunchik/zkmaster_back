package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;
import java.util.Map;

/**
 * TODO: сделать сервис
 * TODO - Maybe just delegate some API from ZKMainService to OtherService that is inner service?
 */
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
     * url: /api/zkm/health
     * Try create connection by hosts and send connection status
     *
     * @return connection create: success OR fail.
     */
    @PostMapping("/create")
    public void createConnection(@RequestBody RequestDTO dto) {
//        return zkMainService.createConnection(dto.getHost());
        zkMainService.createConnection(dto.getHost());
    }

    /**
     * HTTP - GET
     * url: /api/zkm/health
     * Try create connection by hosts and send connection status
     */
    @GetMapping("/health")
    public Map<String, Boolean> checkHostsHealth(@RequestBody List<String> hosts) {
        return null;
    }

}
