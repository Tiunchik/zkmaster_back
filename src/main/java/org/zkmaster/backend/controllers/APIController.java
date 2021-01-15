package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.InjectionDTONew;
import org.zkmaster.backend.entity.dto.RequestDTO;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.services.MainService;
import org.zkmaster.backend.services.injection.InjectionService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api/zkm")
public class APIController {
    MainService mainService;
    InjectionService injectionService;
    
    @Autowired
    public APIController(@Qualifier("mainServiceDefault") MainService mainService,
                         InjectionService injectionService) {
        this.mainService = mainService;
        this.injectionService = injectionService;
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

//    @PostMapping("/data/injection")
//    @Log
//    public @ResponseBody
//    boolean injectFromTo(@RequestBody InjectionDTO dto) throws InjectionFailException {
//        return injectionService.injectFromTo(dto);
//    }
    
    @PostMapping("/data/injection")
    @Log
    public @ResponseBody
    void injectFromTo(@RequestBody InjectionDTONew dto)
            throws InjectionFailException, HostProviderNotFoundException {
        injectionService.injection(dto.getCreateNodeList(), dto.getUpdateNodeList(), dto.getTargetHost());
    }
    
    @PostMapping("/data/injection/test")
    @Log
    public @ResponseBody
    boolean setTest(@RequestBody Set<String> nodePaths) throws InjectionFailException {
        nodePaths.forEach(System.err::println);
        return true;
    }
    
    
}
