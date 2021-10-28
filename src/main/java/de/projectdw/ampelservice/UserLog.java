package de.projectdw.ampelservice;

import lombok.Data;

@Data
public class UserLog {
    String username;
    String timestamp;
    boolean success;

}
