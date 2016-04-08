package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Device.DeviceCallback;
import net.haviss.havissIoT.Type.User;

/**
 * Created by Håvard on 08.04.2016.
 */
public class CommandDevices implements CommandCallback {

    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        JsonObject response = new JsonObject();
        response.addProperty("user", user.getName());


        return null;
    }

    @Override
    public String getName() {
        return "DEVICE";
    }

    @Override
    public boolean requireArgs() {
        return false;
    }
}
