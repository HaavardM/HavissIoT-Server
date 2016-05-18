package net.haviss.havissIoT.ServerCommands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.ServerCommunication.SocketClient;
import net.haviss.havissIoT.Device.Device;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensors.Sensor;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;

/**
 * Created by Håvard on 08.04.2016.
 */
public class CommandDevices implements CommandCallback {

    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        if(parameters.has("intent")) {
            String intent = parameters.get("intent").getAsString();
            switch (intent) {
                case "getall": {
                    return getAllDevices();
                }
                case "modify":
                    //TODO Fix
                    return null;

            }
        }
        //TODO: Fix
        return null;
    }

    @Override
    public String getName() {
        return "devices";
    }

    @Override
    public boolean requireArgs() {
        return false;
    }

    private String getAllDevices() {
        JsonObject response = new JsonObject();
        JsonArray devices = new JsonArray();
        for(Device d : Main.deviceHandler.getAllDevices()) {
            JsonObject device = new JsonObject();
            device.addProperty("name", d.getName());
            device.addProperty("topic", d.getTopic());
            device.addProperty("type", d.getDeviceType().toString());
            device.addProperty("datatype", d.getDataType().toString());
            JsonArray sensors = new JsonArray();
            for (Sensor s : d.getSensors()) {
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

    private String modifyDevice(JsonObject parameters) {
        Device d = null;
        if(parameters.has("name")) {
            d = Main.deviceHandler.getDeviceByName(parameters.get("name").getAsString());
        } else if(parameters.has("topic")) {
            d = Main.deviceHandler.getDeviceByTopic(parameters.get("topic").getAsString());
        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        if(d != null) {
            //prank
            if (parameters.has("newname")) {
                String name = parameters.get("newname").getAsString();
                if (name == null) {
                    return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                }
                d.setName(name);
            }
            if (parameters.has("newtopic")) {
                String topic = parameters.get("newtopic").getAsString();
                if (topic == null) {
                    return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                }
                d.setTopic(topic);
            }
            if (parameters.has("newdatatype")) {
                String type = parameters.get("newdatatype").getAsString();
                if (type == null) {
                    return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                }
                DataType dataType;
                try {
                    dataType = DataType.valueOf(type);
                } catch (IllegalArgumentException e) {
                    return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                }
                d.setDataType(dataType);
            }
            if (parameters.has("newdevicetype")) {
                String type = parameters.get("newdevicetype").getAsString();
                if (type == null) {
                    return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                }
                DeviceType deviceType;
                try {
                    deviceType = DeviceType.valueOf(type);
                } catch (IllegalArgumentException e) {
                    return Integer.toString(HttpStatus.SC_BAD_REQUEST);
                }
                d.setDeviceType(deviceType);
            }
        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        return Integer.toString(HttpStatus.SC_OK);
    }
}
