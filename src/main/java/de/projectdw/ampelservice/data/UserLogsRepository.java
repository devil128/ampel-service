package de.projectdw.ampelservice.data;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository to save and find users.
 */
public interface UserLogsRepository extends MongoRepository<StudentLog, String> {
    List<StudentLog> findAllByTimestampBetweenAndStudent_Id(long from, long to, String id);

    /**
     * @param from timestamp
     * @param to timestamp
     * @param id Student id which is refered to
     * @param pageable pagination object
     * @return requested studentlogs
     */
    List<StudentLog> findAllByTimestampBetweenAndStudent_Id(long from, long to, String id, Pageable pageable);
}
