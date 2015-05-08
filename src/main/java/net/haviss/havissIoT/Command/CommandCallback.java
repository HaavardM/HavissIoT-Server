package net.haviss.havissIoT.Command;

/**
 * Created by Håvard on 5/2/2015.
 */
public interface CommandCallback {
    //Code to run when command is called
    String run(String[] parameters);

    //Get command name
    String getName();
}
