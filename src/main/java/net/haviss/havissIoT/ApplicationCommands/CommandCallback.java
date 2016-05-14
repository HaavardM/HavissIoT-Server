package net.haviss.havissIoT.ApplicationCommands;

/**
 * Created by havar on 14.05.2016.
 */
public interface CommandCallback {
    public String run();

    public String run(String[] parameters);

    public String getCommandName();
}
