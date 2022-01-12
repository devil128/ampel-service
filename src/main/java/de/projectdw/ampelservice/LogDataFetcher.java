package de.projectdw.ampelservice;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import de.projectdw.ampelservice.data.Student;
import de.projectdw.ampelservice.data.StudentLog;
import de.projectdw.ampelservice.data.UserLogsRepository;
import de.projectdw.ampelservice.data.UserRepository;
import graphql.execution.DataFetcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
/**
 * Data fetcher for logs
 */
public class LogDataFetcher {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserLogsRepository userLogsRepository;

    /**
     * fetches logs for a specific user in a timeframe and enables pagination
     */
    @DgsQuery(field = "logs")
    public DataFetcherResult<List<StudentLog>> getLogs(@InputArgument String username, @InputArgument String place, @InputArgument String from, @InputArgument String to, @InputArgument Integer page, @InputArgument Integer pageSize) throws Exception {
        Student student = userRepository.findFirstByUsernameAndPlace(username, place);
        if (student == null)
            throw new Exception("Cant access not created user");
        if (from == null || from.equals("")) {
            from = "10";
        }

        if (to == null || to.equals("")) {
            to = new Date().getTime() + "";
        }
        var logs = userLogsRepository.findAllByTimestampBetweenAndStudent_Id(Long.parseLong(from), Long.parseLong(to), student.getId(), PageRequest.of(page, pageSize));
        return DataFetcherResult.<List<StudentLog>>newResult().data(logs).localContext(Arrays.asList(from, to)).build();
    }
}
