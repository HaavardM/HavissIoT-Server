package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 5/7/2015.
 * Command that displays help
 */
public class CommandHelp implements CommandCallback {
    @Override
    public void run(String[] parameters) {
        String helpString = "";
        for(CommandCallback cb : HavissIoT.commandHandler.getAvailableCommands()) {
            helpString += (cb.getHelp() + "\n");
        }
        System.out.println(helpString);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "help\tShows how to use the program\n" +
                "USAGE: help";
    }
}
