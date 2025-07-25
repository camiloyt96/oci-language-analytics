package com.ociaiapp.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/Health")
    public String health(){
        return "Backend esta corriendo";
    }

    @GetMapping("/")
        public String home(){
        return "OCI Language Analytics API - Listo!";
    }
}
