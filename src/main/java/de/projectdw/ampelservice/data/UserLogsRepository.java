package de.projectdw.ampelservice.data;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserLogsRepository extends MongoRepository<StudentLog, String> {
    List<StudentLog> findAllByTimestampBetweenAndStudent_Id(long from, long to, String id);
    List<StudentLog> findAllByTimestampBetweenAndStudent_Id(long from, long to, String id, Pageable pageable);
}
