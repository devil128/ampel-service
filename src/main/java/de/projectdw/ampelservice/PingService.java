package de.projectdw.ampelservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class PingService {
    @Autowired
    UserLogsRepository userLogsRepository;

    @GetMapping("/ping")
    public String getPing(@RequestParam(required = true) String name) {
        UserLog log = new UserLog();
        log.setSuccess(true);
        log.setTimestamp(new Date().toInstant().toEpochMilli() + "");
        log.setUsername(name);
        userLogsRepository.save(log);
        return "failed";
    }

    @GetMapping("/clear")
    public String clear(){
        userLogsRepository.deleteAll(userLogsRepository.findAll());
        return "Worked";

    }


    @GetMapping("/getFailed")
    public List<UserLog> getFailed() {
        return userLogsRepository.findAll();
    }
}
