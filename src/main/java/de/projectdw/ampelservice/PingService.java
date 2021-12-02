package de.projectdw.ampelservice;


import de.projectdw.ampelservice.data.*;
import de.projectdw.ampelservice.input.InputLogData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class PingService {
    @Autowired
    UserLogsRepository userLogsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentNetworkRepository studentNetworkRepository;

    @PostMapping("/ping")
    public String getPing(@RequestBody InputLogData data) {
        log.info("Name: {} and place connected: {}", data.getName(), data.getPlace());
        var name = data.getName();
        var place = data.getPlace();
        StudentLog userlog = new StudentLog();
        userlog.setSuccess(true);
        userlog.setTimestamp(new Date().toInstant().toEpochMilli());
        if (data.getNetworkInterfaces() != null) {
            for (var network : data.getNetworkInterfaces()) {
                var studentNetwork = new StudentNetwork(network);
                studentNetworkRepository.save(studentNetwork);
                userlog.getNetworks().add(studentNetwork);
            }
        }
        Student student = getUser(name, place);
        log.info(String.valueOf(userlog));

        userlog.setStudent(student);
        var log = userLogsRepository.save(userlog);


        return "failed";
    }

    @GetMapping("/clear")
    public String clear() {
        userLogsRepository.deleteAll(userLogsRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());
        return "Worked";

    }


    @GetMapping("/getFailed")
    public List<StudentLog> getFailed() {
        return userLogsRepository.findAll();
    }

    private Student getUser(String user, String place) {
        var res = userRepository.findFirstByUsernameAndPlace(user, place);
        if (res == null) {
            Student newStudent = new Student();
            newStudent.setFailed(false);
            newStudent.setUsername(user);
            newStudent.setPlace(place);
            userRepository.save(newStudent);
            return newStudent;
        }
        return res;
    }

    @PostMapping("/uploadLogs")
    private boolean uploadData(@RequestBody List<InputLogData> inputLogData) {
        // only not successful requests are repeated after finish
        if (inputLogData.size() == 0)
            return true;
        String name = inputLogData.get(0).getName();
        String place = inputLogData.get(0).getPlace();
        Student user = getUser(name, place);
        for (var log : inputLogData) {
            StudentLog studentLog = new StudentLog();
            studentLog.setSuccess(false);
            studentLog.setTimestamp(Long.parseLong(log.getTimestamp()));
            for (var network : log.getNetworkInterfaces()) {
                var studentNetwork = new StudentNetwork(network);
                studentNetworkRepository.save(studentNetwork);
                studentLog.getNetworks().add(studentNetwork);
            }
            studentLog.setStudent(user);
            userLogsRepository.save(studentLog);

        }

        return true;
    }


}
