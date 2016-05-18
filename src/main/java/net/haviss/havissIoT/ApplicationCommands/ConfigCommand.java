package net.haviss.havissIoT.ApplicationCommands;

import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Tools.Config;

import java.io.IOException;

/**
 * Created by havar on 14.05.2016.
 */
public class ConfigCommand implements CommandCallback {
    @Override
    public String run() {
        return "command need running parameters in format: \"config name\" \"new property\"";
    }

    @Override
    public String run(String[] parameters) {
        if(parameters == null) {
            return "command need running parameters in format: \"config name\" \"new value\"";
        }
        if(parameters.length != 2) {
            return "command need running parameters in format: \"config name\" \"new value\"";
        }
        try {
            if(Config.setProperty(parameters[0], parameters[1])) {
                return "Config set - if system is dependent on the property a restart might be necessary.";
            }
            else {
                return "Failed to set config - property might not exist";
            }
        } catch (IOException e) {
            return "Exception in config: " + e.getMessage();
        }
    }

    @Override
    public String getCommandName() {
        return "config";
    }
}
