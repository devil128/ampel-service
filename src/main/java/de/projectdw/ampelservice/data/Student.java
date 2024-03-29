package de.projectdw.ampelservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
/**
 * Student object which contains student information and aggregated fields. Transient fields are calculated based on request and not saved.
 *
 */
public class Student {
    @Id
    private String id;
    String username;
    String place;
    @Transient
    boolean isFailed;
    @Transient
    boolean isComplete;
    @Transient
    double maxTimeBetweenLogs;
    @Transient
    double timeOnline;
    @Transient
    int logCount;
    @Transient
    List<StudentLog> logs;
    @Transient
    List<String> activeNetworks;

}
