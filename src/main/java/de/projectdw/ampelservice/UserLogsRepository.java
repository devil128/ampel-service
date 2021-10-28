package de.projectdw.ampelservice;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLogsRepository extends MongoRepository<UserLog, String> {

}
