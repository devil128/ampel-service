package de.projectdw.ampelservice.data;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLogsRepository extends MongoRepository<StudentLog, String> {

}
