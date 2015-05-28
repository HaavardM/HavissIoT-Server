package net.haviss.havissIoT.Command;

import org.json.simple.JSONObject;

/**
 * Created by Håvard on 5/2/2015.
 */
public interface CommandCallback {
    //Code to run when command is called
    String run(JSONObject parameters);

    //Get command name
    String getName();
}
