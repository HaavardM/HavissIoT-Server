package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Type.User;

/**
 *Created by HaavardM on 5/16/2015.
 */
public class CommandVoice implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {

        return null;
    }

    @Override
    public String getName() {
        return "VOICE";
    }

    @Override
    public boolean requireArgs() {
        return true;
    }
}
