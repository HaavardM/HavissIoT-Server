package net.haviss.havissIoTClientJava;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Håvard on 4/3/2015.
 * Class gets commands and arguments from string and processes it.
 */
public class havissIoTCommand {
    private HashMap<String, String> commands = new HashMap<>();
    private char argumentIdentifier = ':';
    private char flagIdentifier = '-';
    public void getCommands(String fullstring) {

        List<String> commandStrings = new ArrayList<String>(); //List for storing seperated commandstrings
        //Seperates the commands into seperate strings from the main string.
        //Goes through the complete string and seperates commands (each new flag = new commandstring
        for(int i = 0; i < fullstring.length(); i++) {
            if(fullstring.charAt(i) == flagIdentifier) {
                String tempCommandString = "";
                for(int j = i; j < fullstring.length() && fullstring.charAt(j) != flagIdentifier; j++) {
                    tempCommandString += fullstring.charAt(j);
                    i = j;
                }
                //Then adds the string into a list of unhandeled commandstrings
                commandStrings.add(tempCommandString);
            }
        }
        //Foreach commandstring in the list - seperate flag and argument and store it into a hashmap.
        for(String cmdString : commandStrings) {
            String flag = "";
            String argument = "";
            for(int j = 0; j < cmdString.length(); j++) {
                if(cmdString.charAt(j) == flagIdentifier) {
                    for(int a = j; a < cmdString.length() && cmdString.charAt(a) != argumentIdentifier; a++) {
                        flag += cmdString.charAt(j);
                        j = a;
                    }
                }
                else if(cmdString.charAt(j) == argumentIdentifier) {
                    for(int a = j; a < cmdString.length(); a++) {
                        argument += cmdString.charAt(j);
                        j = a;
                    }
                }
            }
            commands.put(flag, argument);
        }
    }
}
