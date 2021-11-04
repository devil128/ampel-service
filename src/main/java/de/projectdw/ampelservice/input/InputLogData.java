package de.projectdw.ampelservice.input;

import de.projectdw.ampelservice.data.StudentNetwork;
import lombok.Data;

import java.util.List;

@Data
public class InputLogData {
    String name;
    String place;
    String timestamp;
    List<NetworkPair> networkInterfaces;
}
