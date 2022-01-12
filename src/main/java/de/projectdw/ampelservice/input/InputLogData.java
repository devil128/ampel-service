package de.projectdw.ampelservice.input;

import de.projectdw.ampelservice.data.StudentNetwork;
import lombok.Data;

import java.util.List;

@Data
/**
 * Input data defined in the graphql schema
 */
public class InputLogData {
    String name;
    String place;
    String timestamp;
    List<NetworkPair> networkInterfaces;
}
