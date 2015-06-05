package net.haviss.havissIoT.Command;

import com.google.gson.JsonArray;
import net.haviss.havissIoT.Type.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Håvard on 5/28/2015.
 */
public class CommandStorage implements CommandCallback {
    @Override
    public String run(JSONObject parameters, User user) {
        return null;
    }

    @Override
    public String getName() {
        return "STORAGE";
    }
}
