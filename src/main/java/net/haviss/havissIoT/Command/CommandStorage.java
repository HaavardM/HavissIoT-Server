package net.haviss.havissIoT.Command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Type.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by H�vard on 5/28/2015.
 */
public class CommandStorage implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user) {
        return null;
    }

    @Override
    public String getName() {
        return "STORAGE";
    }

    @Override
    public boolean requireArgs() {
        return true;
    }
}
