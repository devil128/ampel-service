package de.projectdw.ampelservice;

import com.netflix.graphql.dgs.*;
import de.projectdw.ampelservice.data.Student;
import de.projectdw.ampelservice.data.StudentLog;
import de.projectdw.ampelservice.data.StudentNetwork;
import de.projectdw.ampelservice.data.UserRepository;
import de.projectdw.ampelservice.input.NetworkPair;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;


import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@DgsComponent
public class UserLogDatafetcher {
    @Autowired
    UserRepository userRepository;

    @DgsQuery(field = "users")

    public List<Student> getUsers(@InputArgument String filter, @InputArgument String from, @InputArgument String to) {
        var users = userRepository.findAll();
        if (filter != null) {
            users = users.stream().filter(str -> str.getUsername().startsWith(filter)).collect(Collectors.toList());
        }
        List<Student> result = new ArrayList<>();
        users.parallelStream().forEach((elm) -> {
            result.add(generateData(elm, from, to));
        });

        return result;
    }

    @DgsQuery(field = "user")
    public Student getStudentData(@InputArgument String username, @InputArgument String place, @InputArgument String from, @InputArgument String to) {
        Student student = userRepository.findFirstByUsernameAndPlace(username, place);
        return generateData(student, from, to);
    }

    private Student generateData(Student user, String from, String to) {
        if (from == null)
            from = Instant.ofEpochSecond(10).toEpochMilli() + "";
        if (to == null)
            to = System.currentTimeMillis() + "";
        String finalFrom = from;
        String finalTo = to;


        var logs = getLogsInTimeframe(user, finalFrom, finalTo);
        user.setMaxTimeBetweenLogs(maxTimeBetweenLogs(logs));
        user.setTimeOnline(timeOnline(logs));
        user.setFailed(isFailed(user, finalFrom, finalTo));
        user.setComplete(user.getMaxTimeBetweenLogs() < 10000);
        user.setActiveNetworks(activeNetworks(logs));
        user.setLogCount(logs.size());
        return user;
    }

    public double timeOnline(List<StudentLog> logsInTimeframe) {
        int count = 0;
        for (var log : logsInTimeframe) {
            if (log.isSuccess())
                count++;
        }

        return count * 5.0;
    }

    private List<String> activeNetworks(List<StudentLog> logsInTimeframe) {
        Set<String> activeNetworks = new HashSet<>();
        for (var log : logsInTimeframe) {
            for (var networks : log.getNetworks()) {
                if (networks.isOnline())
                    activeNetworks.add(networks.getNetwork());
            }
        }
        return new ArrayList<>(activeNetworks);
    }

    public double maxTimeBetweenLogs(List<StudentLog> logsInTimeframe) {
        double maxTimeInBetween = 100000000d;
        for (int i = 0; i < logsInTimeframe.size() - 1; i++) {
            var firstElement = logsInTimeframe.get(i);
            var secondElement = logsInTimeframe.get(i + 1);
            long firstTs = Long.parseLong(firstElement.getTimestamp());
            long secondTs = Long.parseLong(secondElement.getTimestamp());
            if (secondTs - firstTs < maxTimeInBetween)
                maxTimeInBetween = secondTs - firstTs;
        }

        return maxTimeInBetween;
    }

    public boolean isFailed(Student student, String tsFrom, String tsTo) {

        long from = Long.parseLong(tsFrom);
        long to = Long.parseLong(tsTo);
        return student.getLogs().stream()
                .filter(log -> log.getTimestamp() != null)
                .filter(log -> Long.parseLong(log.getTimestamp()) > from)
                .filter(log -> Long.parseLong(log.getTimestamp()) < to)
                .anyMatch(StudentLog::isSuccess);
    }

    @DgsSubscription(field = "users")
    public Publisher<List<Student>> streamUsers(@InputArgument String filter,
                                                @InputArgument String from,
                                                @InputArgument String to,
                                                DgsDataFetchingEnvironment dfe) {
        if (from == null)
            from = Instant.ofEpochSecond(10).toEpochMilli() + "";
        if (to == null)
            to = System.currentTimeMillis() + "";
        String finalFrom = from;
        String finalTo = to;
        return Flux.interval(Duration.ofSeconds(5)).map(ts -> getUsers(filter, finalFrom, finalTo));
    }

    private List<StudentLog> getLogsInTimeframe(Student student, String tsFrom, String tsTo) {
        long from = Long.parseLong(tsFrom);
        long to = Long.parseLong(tsTo);
        var logsInTimeframe = student.getLogs().stream()
                .filter(log -> log.getTimestamp() != null)
                .filter(log -> Long.parseLong(log.getTimestamp()) > from)
                .filter(log -> Long.parseLong(log.getTimestamp()) < to)
                .sorted(Comparator.comparing(studentLog -> Long.parseLong(studentLog.getTimestamp())))
                .collect(Collectors.toList());

        return logsInTimeframe;
    }


}
