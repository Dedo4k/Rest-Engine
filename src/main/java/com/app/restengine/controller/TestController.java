package com.app.restengine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public ResponseEntity<?> get(@RequestParam String id) {
        return ResponseEntity.ok(id);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody HashMap<String, Object> data) {
        return ResponseEntity.ok(data);
    }

    @PutMapping
    public ResponseEntity<?> put() {
        return ResponseEntity.ok(1);
    }

    @DeleteMapping
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(1);
    }
}
