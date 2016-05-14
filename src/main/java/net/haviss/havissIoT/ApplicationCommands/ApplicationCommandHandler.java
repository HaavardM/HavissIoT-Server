package net.haviss.havissIoT.ApplicationCommands;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by havar on 14.05.2016.
 */
public class ApplicationCommandHandler {
    private List<CommandCallback> commandCallbacks = new ArrayList<>();

    public ApplicationCommandHandler() {
        Reflections reflections = new Reflections();
        Set<Class<? extends CommandCallback>> classes = reflections.getSubTypesOf(CommandCallback.class);
        for (Class<? extends CommandCallback> c : classes) {
            try {
                CommandCallback cmd = c.newInstance();
                commandCallbacks.add(cmd);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String processCommand(String command, String[] parameters) {
        for(CommandCallback c : commandCallbacks) {
            if(command.compareTo(c.getCommandName()) == 0) {
                if(parameters == null) {
                    return c.run();
                }
                else {
                    return c.run(parameters);
                }
            }
        }
        return "Command not found";
    }

    public String processCommand(String command) {
        return processCommand(command, null);
    }

}
