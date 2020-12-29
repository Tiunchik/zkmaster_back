package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.TransformDTO;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.services.MainService;

import java.util.List;

@Controller
@RequestMapping("/api/zkm/transform/{host}")
public class TransformController {
    MainService mainService;

    @Autowired
    public TransformController(@Qualifier("mainServiceFresh") MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("")
    @Log
    public @ResponseBody
    List<String> export(@RequestBody TransformDTO dto,
                        @PathVariable String host) throws NodeReadException {
        return mainService.exportHost(host, dto.getType());
    }

    @PostMapping("")
    @Log
    public @ResponseBody
    boolean importData(@RequestBody TransformDTO dto,
                       @PathVariable String host) throws HostProviderNotFoundException, DataImportFailException {
        return mainService.importData(host, dto.getType(), dto.getContent());
    }

}
