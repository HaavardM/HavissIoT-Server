package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;

/**
 * Created by Håvard on 6/9/2015.
 */
public class CommandExit implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        if(user != null && user.isOP()) {
            HavissIoT.exit(0);
        }
        return Integer.toString(HttpStatus.SC_UNAUTHORIZED);
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
