package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.TransformDTO;
import org.zkmaster.backend.exceptions.ImportFailException;
import org.zkmaster.backend.services.ZKMainService;

import java.util.List;

@Controller
@RequestMapping("/api/zkm/transform/{host}")
public class TransformController {
    ZKMainService zkMainService;

    @Autowired
    public TransformController(@Qualifier("ZKMainServiceRWL") ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    @GetMapping("")
    @Log
    public @ResponseBody
    List<String> export(@RequestBody TransformDTO dto,
                        @PathVariable String host) {
        return zkMainService.exportHost(host, dto.getType());
    }

    @PostMapping("")
    @Log
    public @ResponseBody
    boolean importData(@RequestBody TransformDTO dto,
                       @PathVariable String host) throws ImportFailException {
        return zkMainService.importData(host, dto.getType(), dto.getContent());
    }

}
