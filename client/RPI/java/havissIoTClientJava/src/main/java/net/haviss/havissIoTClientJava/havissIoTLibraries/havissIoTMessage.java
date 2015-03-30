package net.haviss.havissIoTClientJava.havissIoTLibraries;

import java.util.Objects;

/**
 * Created by Håvard on 3/27/2015.
 * This library generates messages to publish.
 */
public class havissIoTMessage {
    private String fullMessageString = ""; //complete string with clientID and all commands/information
    private String clientID = ""; //Client id
    private String commandString = ""; //Other commands and data/information
    //Add clientID to beginning message string
    public void addClientID(String cID) {
        clientID = cID;
        fullMessageString = "@" +"\"" + clientID + "\"" + fullMessageString;
    }
    //Add command to message string
    public void addCommand(havissIoTCommand command, String messageData) {
        fullMessageString += command.getCommandFlag() + "\"" + messageData + "\"";
        commandString = messageData;
    }
    //Add a custom flag to message
    public void addCustomFlag(String flag, String messageData) {
        fullMessageString += flag + "\"" + messageData + "\"";
        commandString = messageData;
    }
    public String getMessageString() {
        return fullMessageString;
    }
    public byte[] getMessageByte() {
        return fullMessageString.getBytes();
    }

    public void loadByteMessage(byte[] msg) {
        int clientIDStop = 0;
        //Loading message string from bytes
        fullMessageString = msg.toString(); //TODO: Change this - needs proper encoding
        //Gets the clientID
        for(int i = 0; i < fullMessageString.length(); i++) {
            //If character equals clientID identifier (@) - read clietID
            if(fullMessageString.charAt(i) == '@') {
                //If clientID is formatted with apostrophe
                if(fullMessageString.charAt(i + 1) == '\"') {
                    for(int j = i + 2; fullMessageString.charAt(j) != '\"'; j++) {
                        clientID += fullMessageString.charAt(j);
                        if(fullMessageString.charAt(j+1) == '\"') {
                            clientIDStop = j + 1;
                        }
                    }
                } else {
                    for(int j = ++i; fullMessageString.charAt(j) != '-'; j++) {
                        clientID += fullMessageString.charAt(j);
                        if(fullMessageString.charAt(j) == '-') {
                            clientIDStop = j;
                        }
                    }
                }
            }
        }
        //Get commandstring - contains all commands. The commandstring can be passed to the commandhandler for processing.
        //The rest of the messagestring should be a commandstring. Anything else will be treated as command arguments
        for(int i = clientIDStop + 1; i < fullMessageString.length(); i++ ) {
            commandString += fullMessageString.charAt(i);
        }

    }
    public String getCommandString() {
        return commandString;
    }
}
