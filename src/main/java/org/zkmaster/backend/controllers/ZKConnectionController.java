package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkmaster.backend.entity.RequestDTO;
import org.zkmaster.backend.services.ZkMainService;

import java.util.List;
import java.util.Map;

/**
 * TODO: сделать сервис
 * TODO - Maybe just delegate some API from ZkMainService to OtherService that is inner service?
 */
@Controller
@RequestMapping("/api/zkm/conn")
public class ZKConnectionController {

    ZkMainService zkMainService;

    @Autowired
    public ZKConnectionController(ZkMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    /**
     * HTTP - POST
     * url: /api/zkm/health
     * Try create connection by hosts and send connection status
     *
     * @return connection create: success OR fail.
     */
    @PostMapping("/health")
    public boolean createConnection(@RequestAttribute RequestDTO dto) {
        return zkMainService.createConnection(dto.getHost());
    }

    /**
     * HTTP - POST
     * url: /api/zkm/health
     * Try create connection by hosts and send connection status
     */
    @PostMapping("/health")
    public Map<String, Boolean> checkHostsHealth(@RequestBody List<String> hosts) {
        return null;
    }

}
