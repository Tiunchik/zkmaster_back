package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.TransformDTO;
import org.zkmaster.backend.exceptions.HostNotFoundException;
import org.zkmaster.backend.exceptions.ImportFailException;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;

@Controller
@RequestMapping("/api/zkm/transform/{host}")
public class TransformController {
    ZKMainService mainService;

    @Autowired
    public TransformController(@Qualifier("ZKMainServiceFresh") ZKMainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("")
    @Log
    public @ResponseBody
    List<String> export(@RequestBody TransformDTO dto,
                        @PathVariable String host) {
        return mainService.exportHost(host, dto.getType());
    }

    @PostMapping("")
    @Log
    public @ResponseBody
    boolean importData(@RequestBody TransformDTO dto,
                       @PathVariable String host) throws ImportFailException, HostNotFoundException {
        return mainService.importData(host, dto.getType(), dto.getContent());
    }

}
