package de.projectdw.ampelservice.data;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository to save and load StudentNetwork data.
 */
public interface StudentNetworkRepository extends MongoRepository<StudentNetwork, String> {
}
