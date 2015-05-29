package net.haviss.havissIoT.Core;

import com.google.gson.Gson;
import net.haviss.havissIoT.Command.CommandCallback;
import net.haviss.havissIoT.HavissIoT;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        //Objects and variables
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        JSONObject parameters = null;
        String reply;
        String command = null;
        //Try to parse
        try {
            jsonObject = (JSONObject) parser.parse(commandString);
            //Check if JSON object contains all necessary keys
            if(jsonObject.containsKey("cmd") && jsonObject.containsKey("args")) {
                command = ((String) jsonObject.get("cmd")).toUpperCase();
                parameters = (JSONObject) jsonObject.get("args");
            }
        } catch (ParseException | ClassCastException e) {
            HavissIoT.printMessage(e.getMessage());
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);

        }

        //If it is JSON it can be a command - find correct command and run it
        if(command != null && parameters != null) {
            for (CommandCallback cb : availableCommands) {
                if (command.compareTo(cb.getName()) == 0) { //Check if command string corresponds to command
                    reply = cb.run(parameters); //run the command
                    return reply;
                }
            }
        }

        //Return bad request if nothing nothing worked
        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
    }
}
