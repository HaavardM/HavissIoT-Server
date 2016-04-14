package net.haviss.havissIoT.Command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Device.Device;
import net.haviss.havissIoT.Device.DeviceCallback;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensors.IoTSensor;
import net.haviss.havissIoT.Type.User;

/**
 * Created by Håvard on 08.04.2016.
 */
public class CommandDevices implements CommandCallback {

    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        JsonObject response = new JsonObject();



        return response.toString();
    }

    @Override
    public String getName() {
        return "devices";
    }

    @Override
    public boolean requireArgs() {
        return false;
    }
}
