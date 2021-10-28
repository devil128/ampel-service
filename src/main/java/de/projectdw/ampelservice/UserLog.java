package de.projectdw.ampelservice;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pings")
public class UserLog {
    String username;
    String timestamp;
    boolean success;

}
