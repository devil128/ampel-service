package de.projectdw.ampelservice;

import com.netflix.graphql.dgs.*;
import de.projectdw.ampelservice.data.Student;
import de.projectdw.ampelservice.data.StudentLog;
import de.projectdw.ampelservice.data.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;


import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class UserLogDatafetcher {
    @Autowired
    UserRepository userRepository;

    @DgsQuery(field = "users")
    public List<Student> getUsers(@InputArgument String filter, @InputArgument String from, @InputArgument String to) {
        var users = userRepository.findAll();
        if (from == null)
            from = Instant.ofEpochSecond(10).toEpochMilli() + "";
        if (to == null)
            to = System.currentTimeMillis() + "";
        String finalFrom = from;
        String finalTo = to;

        for (var user : users) {
            user.setMaxTimeBetweenLogs(maxTimeBetweenLogs(user, finalFrom, finalTo));
            user.setFailed(isFailed(user, finalFrom, finalTo));
            user.setComplete(user.getMaxTimeBetweenLogs() < 10000);
        }
        if (filter == null) {
            return users;
        }

        return users.stream().filter(str -> str.getUsername().startsWith(filter)).collect(Collectors.toList());
    }

    public double maxTimeBetweenLogs(Student student, String tsFrom, String tsTo) {
        long from = Long.parseLong(tsFrom);
        long to = Long.parseLong(tsTo);
        var logsInTimeframe = student.getLogs().stream()
                .filter(log -> log.getTimestamp() != null)
                .filter(log -> Long.parseLong(log.getTimestamp()) > from)
                .filter(log -> Long.parseLong(log.getTimestamp()) < to)
                .sorted(Comparator.comparing(studentLog -> Long.parseLong(studentLog.getTimestamp())))
                .collect(Collectors.toList());

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


}
