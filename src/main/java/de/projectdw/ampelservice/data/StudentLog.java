package de.projectdw.ampelservice.data;

import de.projectdw.ampelservice.input.NetworkPair;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "student-logs")
@ToString
@NoArgsConstructor
@EqualsAndHashCode

public class StudentLog {
    @Id
    private String id;
    String timestamp;
    boolean success;
    @DBRef(lazy = true)
    List<StudentNetwork> networks = new ArrayList<>();


}
