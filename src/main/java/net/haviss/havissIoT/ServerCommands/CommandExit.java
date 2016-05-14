package net.haviss.havissIoT.ServerCommands;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.ServerCommunication.SocketClient;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;

/**
 * Created by Håvard on 6/9/2015.
 */
public class CommandExit implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        if(user != null && user.isOP()) {
            Main.exit(0);
        }
        return Integer.toString(HttpStatus.SC_UNAUTHORIZED);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public boolean requireArgs() {
        return false;
    }
}
