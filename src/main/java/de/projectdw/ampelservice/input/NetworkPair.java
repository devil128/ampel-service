package de.projectdw.ampelservice.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Input Type for NetworkPair
 */
public class NetworkPair {
    String network;
    boolean online;
}
