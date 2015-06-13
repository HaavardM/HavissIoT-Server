package net.haviss.havissIoT.Command;


import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Type.User;

/**
 * Created by H�vard on 5/28/2015.
 */
public class CommandStorage implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
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
