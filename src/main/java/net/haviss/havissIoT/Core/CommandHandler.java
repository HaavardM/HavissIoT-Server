package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Command.CommandCallback;
import org.reflections.Reflections;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by HaavardM on 5/7/2015.
 * Handles commands
 */
public class CommandHandler {
    private CopyOnWriteArrayList<CommandCallback> availableCommands = new CopyOnWriteArrayList<>();

    //Constructor - adds all available commands to string
    public CommandHandler() {
        Reflections r = new Reflections("");
        Set<Class<? extends CommandCallback>> classes = r.getSubTypesOf(CommandCallback.class);
        for(Class<? extends CommandCallback> c : classes) {
            try {
                CommandCallback cmd = c.newInstance();
                availableCommands.add(cmd);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    //Processes commandstring and perform corresponding command.
    public String processCommand(String commandString) {
        String reply = "";
        String[] cmd = commandString.toLowerCase().split("\\s+"); //Splits string into seperate arguments
        String command = cmd[0]; //First string is the command
        String[] arguments = Arrays.copyOfRange(cmd, 1, cmd.length); //Rest of the strings are arguments
        boolean success = false;
        for(CommandCallback cb : availableCommands) {
            if(command.compareTo(cb.getName()) == 0) { //Check if command string corresponds to command
                reply = cb.run(arguments); //run the command
                success = true; //set success
            }
        }
        //If no command were found - throw exception
        if(!success) {
            return "Couldn't find command";
        } else {
            return reply;
        }
    }
}
