package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.entity.dto.RequestDTO;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.services.MainService;
import org.zkmaster.backend.services.cypto.CryptoService;
import org.zkmaster.backend.services.injection.InjectionService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/zkm")
@CrossOrigin(value = {"*"})
public class APIController {
    private final MainService mainService;
    private final InjectionService injectionService;
    private final CryptoService cryptoService;
    
    @Autowired
    public APIController(@Qualifier("mainServiceDefault") MainService mainService,
                         InjectionService injectionService,
                         CryptoService cryptoService) {
        this.mainService = mainService;
        this.injectionService = injectionService;
        this.cryptoService = cryptoService;
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
    
    @PostMapping("/data/injection")
    @Log
    public @ResponseBody
    void injectFromTo(@RequestBody InjectionDTO dto)
            throws InjectionFailException, HostProviderNotFoundException {
        injectionService.injection(dto.getCreateNodeList(), dto.getUpdateNodeList(), dto.getTargetHost());
    }
    
    /**
     * @param value {@link String} to be encoded.
     * @return encrypted {@param value}.
     *
     * @throws Exception -
     */
    @GetMapping("/crypto/enc/{value}")
    @Log
    public @ResponseBody
    String encodeValue(@PathVariable String value) throws Exception {
        return cryptoService.encode(value);
    }
    
}
