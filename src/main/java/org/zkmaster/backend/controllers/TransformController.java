package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.dto.TransformDTO;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.services.transform.TransformService;

import java.util.List;

@Controller
@RequestMapping("/api/zkm/transform/{host}")
public class TransformController {
    TransformService transformService;

    @Autowired
    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }

    @GetMapping("")
    @Log
    public @ResponseBody
    List<String> export(@RequestBody TransformDTO dto,
                        @PathVariable String host) throws NodeReadException {
        return transformService.exportHost(host, dto.getType());
    }

    @PostMapping("")
    @Log
    public @ResponseBody
    boolean importData(@RequestBody TransformDTO dto,
                       @PathVariable String host)
            throws HostProviderNotFoundException, DataImportFailException {
        return transformService.importData(host, dto.getType(), dto.getContent());
    }

}
