package de.projectdw.ampelservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
public class Student {
    @Id
    private String id;
    String username;
    String place;
    @DBRef(lazy = true)
    List<StudentLog> logs = new ArrayList<>();
    boolean isFailed;
    boolean isComplete;
    double maxTimeBetweenLogs;
    double timeOnline;
    int logCount;
    List<String> activeNetworks;
}
