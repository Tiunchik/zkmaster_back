package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.CopyDTO;
import org.zkmaster.backend.entity.dto.RequestDTO;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/zkm")
public class APIController {
    ZKMainService mainService;

    @Autowired
    public APIController(@Qualifier("ZKMainServiceFresh") ZKMainService mainService) {
        this.mainService = mainService;
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
        return mainService.checkHostsHealth(hosts);
    }

    @PostMapping("/data/copy")
    @Log
    public @ResponseBody
    boolean copyFromHostToOther(@RequestBody CopyDTO dto) {
//        return zkMainService.checkHostsHealth(hosts);
        throw new UnsupportedOperationException();
    }

}
