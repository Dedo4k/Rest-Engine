package com.app.restengine.controller;

import com.app.restengine.domain.EngineInfo;
import com.app.restengine.domain.ServiceConfiguration;
import com.app.restengine.dto.ServiceConfigurationCreationDTO;
import com.app.restengine.dto.ServiceConfigurationIdDTO;
import com.app.restengine.dto.mapper.ServiceConfigurationMapper;
import com.app.restengine.service.RestEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/engine")
public class RestEngineController {

    private final RestEngineService restEngine;

    @Autowired
    public RestEngineController(RestEngineService restEngine) {
        this.restEngine = restEngine;
    }

    @PostMapping("/run")
    public ResponseEntity<ServiceConfigurationIdDTO> runService(@RequestBody ServiceConfigurationCreationDTO serviceConfigurationDTO) {
        ServiceConfiguration serviceConfiguration = restEngine.runService(ServiceConfigurationMapper.fromDTO(serviceConfigurationDTO));
        return ResponseEntity.ok(ServiceConfigurationMapper.toIdDTO(serviceConfiguration));
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<Integer> stopService(@PathVariable int id) {
        return ResponseEntity.ok(restEngine.stopService(id));
    }

    @GetMapping("/info")
    public ResponseEntity<EngineInfo> getInfo() {
        return ResponseEntity.ok(restEngine.getInfo());
    }
}
