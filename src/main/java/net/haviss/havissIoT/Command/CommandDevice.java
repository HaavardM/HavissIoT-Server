package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Type.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Haavard on 5/9/2015.
 * Sends command to an IoT device - makes it possible to control things over internet fra clients
 */
public class CommandDevice implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user) {
        //TODO: Create device class and create this command.
        return null;
    }

    @Override
    public String getName() {
        return "device";
    }

    @Override
    public boolean requireArgs() {
        return true;
    }
}
