package de.projectdw.ampelservice.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NetworkPair {
    String network;
    boolean online;
}
