package net.haviss.havissIoT.Command;

/**
 * Created by Håvard on 5/2/2015.
 */
public interface CommandCallback {
    //Code to run when command is called
    void run(String[] parameters);

    String getName();

    //Returns a string containing useful information about the command
    String getHelp();
}
