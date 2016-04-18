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
        JsonArray devices = new JsonArray();
        for(Device d : Main.deviceHandler.getAllDevices()) {
            JsonObject device = new JsonObject();
            device.addProperty("name", d.getName());
            device.addProperty("topic", d.getTopic());
            device.addProperty("type", d.getDeviceType().toString());
            device.addProperty("datatype", d.getDataType().toString());
            JsonArray sensors = new JsonArray();
            for (IoTSensor s : d.getSensors()) {
                JsonObject sensor = new JsonObject();
                sensor.addProperty("name", s.getName());
                sensor.addProperty("topic", s.getTopic());
                sensor.addProperty("type", s.getSensorType().toString());
                sensor.addProperty("datatype", s.getDataType().toString());
                sensor.addProperty("unit", s.getUnit().toString());
                sensors.add(sensor);
            }
            device.add("sensors", sensors);
            devices.add(device);
        }
        response.add("devices", devices);


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
