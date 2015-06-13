package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;

/**
 * Created by Håvard on 6/9/2015.
 */
public class CommandExit implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        HavissIoT.exit(0);
        return null;
    }

    @Override
    public String getName() {
        return "EXIT";
    }

    @Override
    public boolean requireArgs() {
        return false;
    }
}
