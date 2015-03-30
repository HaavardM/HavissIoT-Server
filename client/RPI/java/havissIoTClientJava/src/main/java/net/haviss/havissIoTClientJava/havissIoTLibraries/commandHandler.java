package net.haviss.havissIoTClientJava.havissIoTLibraries;

import java.util.Collections;
import java.util.List;

/**
 * Created by Håvard on 3/30/2015.
 */
public class commandHandler {
    private List<havissIoTCommand> commandList = Collections.emptyList();

    public void checkForCommand(String cmdString) {
        String foundFlag = "";
        for(int i = 0; i < cmdString.length(); i++) {
            //Command found
            if(cmdString.charAt(i) == '-') {
                //Get flag
                for(int j = i; j != '\"'; j++) {
                    foundFlag += cmdString.charAt(j); //Add flag to foundFlag string
                    if(cmdString.charAt(j + 1) == '\"') {
                        i = j; // No need to scan the flag all over again in main for loop. Skips to the end of the flag and continues from there
                    }
                }
                //TODO: Get argument
                //TODO: Get other flags and other arguments


            }
        }
    }
}
