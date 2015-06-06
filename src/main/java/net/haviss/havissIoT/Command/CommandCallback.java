package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Type.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by H�vard on 5/2/2015.
 */
public interface CommandCallback {
    //Code to run when command is called
    String run(JsonObject parameters, User user);

    //Get command name
    String getName();
}
