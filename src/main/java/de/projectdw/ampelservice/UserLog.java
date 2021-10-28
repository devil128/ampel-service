package de.projectdw.ampelservice;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "pings")
public class UserLog {
    @MongoId
    private String id;
    String username;
    String timestamp;
    boolean success;

}
