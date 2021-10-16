package de.projectdw.ampelservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingService {
    @GetMapping("/ping")
    public String getPing() {
        return "failed";
    }
}
