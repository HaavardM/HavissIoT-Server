package net.haviss.havissIoT;

import net.haviss.havissIoT.Command.CommandCallback;
import net.haviss.havissIoT.Command.CommandSubscribe;
import net.haviss.havissIoT.Command.CommandTopics;
import net.haviss.havissIoT.Command.CommandUnsubscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Håvard on 5/7/2015.
 * Handles commands
 */
public class CommandHandler {
    private List<CommandCallback> availableCommands = new ArrayList<>();

    //Constructor - adds all available commands to string
    public CommandHandler() {

    }

    //Processes commandstring and perform corresponding command.
    public void processCommand(String commandString) throws Exception {
        String[] cmd = commandString.toLowerCase().split("\\s+"); //Splits string into seperate arguments
        String command = cmd[0]; //First string is the command
        String[] arguments = Arrays.copyOfRange(cmd, 1, cmd.length); //Rest of the strings are arguments
        boolean success = false;
        for(CommandCallback cb : availableCommands) {
            if(command.compareTo(cb.getName()) == 0) { //Check if command string corresponds to command
                cb.run(arguments); //run the command
                success = true; //set success
                break; //no need to continue loop - saves some valuable resources ;) ;)
            }
        }
        //If no command were found - throw exception
        if(!success) {
            throw new Exception("Couldn't find command");
        }
    }

    //Returns all available command in one array
    public CommandCallback[] getAvailableCommands() {
        return (CommandCallback[]) availableCommands.toArray();
    }
}
