package de.projectdw.ampelservice.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentNetworkRepository extends MongoRepository<StudentNetwork, String> {
}
