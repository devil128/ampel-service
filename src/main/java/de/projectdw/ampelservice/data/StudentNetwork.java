package de.projectdw.ampelservice.data;

import de.projectdw.ampelservice.input.NetworkPair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("student-networks")
/**
 * Object which contains all networks of the student active at the time of a StudentLog.
 */
public class StudentNetwork {
    @Id
    private String id;
    String network;
    boolean online;

    public StudentNetwork(NetworkPair networkPair) {
        this.network = networkPair.getNetwork();
        this.online = networkPair.isOnline();
    }

}
