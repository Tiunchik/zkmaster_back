package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.services.transform.TransformService;

import java.util.List;

@Controller
@RequestMapping("/api/zkm/transform/{host}/{type}")
public class TransformController {
    TransformService transformService;
    
    @Autowired
    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }
    
    @GetMapping("")
    @Log
    public @ResponseBody
    List<String> export(@PathVariable String host,
                        @PathVariable String type) throws NodeReadException {
        return transformService.exportHost(host, type);
    }
    
    /**
     * TODO - import into sub-node.
     */
    @PostMapping("")
    @Log
    public @ResponseBody
    boolean importData(@PathVariable String host,
                       @PathVariable String type,
                       @RequestBody List<String> content)
            throws HostProviderNotFoundException, DataImportFailException {
        return transformService.importData(host, type, content);
    }
    
}
