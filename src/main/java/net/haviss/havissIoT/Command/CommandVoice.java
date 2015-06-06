package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Type.User;
import org.json.simple.JSONObject;


/**
 *Created by HaavardM on 5/16/2015.
 */
public class CommandVoice implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user) {

        return null;
    }

    @Override
    public String getName() {
        return "VOICE";
    }
}
