package org.zkmaster.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * TODO: сделать сервис
 */
@Controller
@RequestMapping("/api")
public class ZKHealthCheck {

    /**
     * HTTP - POST
     * url: /api/zkmaster/health
     * Try create connection by hosts and send connection status
     */
    @PostMapping("/zkmaster/health")
    public Map<String, Boolean> checkHealth(@RequestBody List<String> hosts) {
        return null;
    }

}
