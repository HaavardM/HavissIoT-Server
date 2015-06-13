package net.haviss.havissIoT.Core;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Command.CommandCallback;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.reflections.Reflections;
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
    public String processCommand(String command, JsonObject parameters, User user, SocketClient client) {
        /*variables*/
        String reply;

        //If it is JSON it can be a command - find correct command and run it
        if(command != null && parameters != null) {
            for (CommandCallback cb : availableCommands) {
                if (command.compareTo(cb.getName()) == 0) { //Check if command string corresponds to command
                    reply = cb.run(parameters, user, client); //run the command
                    return reply;
                }
            }
        }
        //Return bad request if nothing worked
        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
    }

    //Processes commandstring and perform corresponding command.
    public String processCommand(String command,  User user, SocketClient client) {
        /*variables*/
        String reply;

        //If it is JSON it can be a command - find correct command and run it
        if(command != null) {
            for (CommandCallback cb : availableCommands) {
                if ((command.compareTo(cb.getName()) == 0)) { //Check if command string corresponds to command
                    if(!cb.requireArgs()) {
                        reply = cb.run(null, user, client); //run the command without arguments
                        return reply;
                    } else {
                        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                    }
                }
            }
        }
        //Return bad request if nothing worked
        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
    }
}
