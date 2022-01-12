package de.projectdw.ampelservice.data;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository to save and find students
 */
public interface UserRepository extends MongoRepository<Student, String> {
    // only retrives first field = place and username are expected to be unique
    Student findFirstByUsernameAndPlace(String username, String place);


}
