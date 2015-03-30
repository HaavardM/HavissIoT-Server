package net.haviss.havissIoTClientJava.havissIoTLibraries;

import java.util.Objects;

/**
 * Created by Håvard on 3/27/2015.
 * This library generates messages to publish.
 */
public class havissIoTMessage {
    private String messageString = "";
    //Add clientID to beginning message string
    public void addClientID(String clientID) {
        messageString = "@" +"\"" + clientID + "\"" + messageString;
    }
    //Add command to message string
    public void addCommand(havissIoTCommand command, String messageData) {
        messageString += command.getCommandFlag() + "\"" + messageData + "\"";
    }
    //Add a custom flag to message
    public void addCustomFlag(String flag, String messageData) {
        messageString += flag + "\"" + messageData + "\"";
    }
}
