package net.haviss.havissIoT.Command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Exceptions.HavissIoTSensorException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensor.IoTSensor;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;


/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSensor implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        String intent;
        boolean isOP = user != null && user.isOP();
        if(!parameters.isJsonObject() && Config.debugMode) {
            Main.printMessage("Error with args");
        }
        if (parameters.has("intent")) {
            intent = parameters.get("intent").getAsString().toUpperCase();
            if(Config.debugMode) {
                Main.printMessage(intent);
            }
        } else {
            if(Config.debugMode) {
                Main.printMessage("No intent");
            }
            //Return bad request if there is no intent key in JSON
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }

        //Find correct intent and run corresponding method.
        if (intent.compareTo("ADD") == 0) {
            if (isOP) {
                //Add a new sensor
                return create(parameters);
            }
            return Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE);

        } else if (intent.compareTo("REMOVE") == 0) {
            if(isOP) {
                return remove(parameters);
            }
            return Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE);
        } else if (intent.compareTo("LIST") == 0) {
            Main.printMessage("Listing all sensors");
            return list();
        } else if (intent.compareTo("SAVE") == 0) {
            if(isOP) {
                Main.sensorHandler.writeToFile();
                return Integer.toString(HttpStatus.SC_OK);
            }
            return Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE);
        } else if(intent.compareTo("GET") == 0) {
            return getSensor(parameters);
        }
        else {
            return Integer.toString(HttpStatus.SC_NOT_FOUND);
        }
    }

    @Override
    public String getName() {
        return "SENSOR";
    }

    @Override
    public boolean requireArgs() {
        return true;
    }

    //Create a new sensor and add it to sensor handler based on parameters.
    private String create(JsonObject parameters) {
        String sensorName;
        String sensorTopic;
        String sensorType;
        Boolean toStore;
        long timeout = 0;
        //Must contain a name and topic
        if (parameters.has("name") && parameters.has("topic") && parameters.has("type") && parameters.has("toStore")) {
            try {
                sensorName = parameters.get("name").getAsString();
                sensorTopic = parameters.get("topic").getAsString();
                sensorType = parameters.get("type").getAsString();
                toStore = parameters.get("toStore").getAsBoolean();
                if(parameters.has("timeout")) {
                    timeout = parameters.get("timeout").getAsLong();
                }
            } catch (ClassCastException e) {
                Main.printMessage(e.getMessage());
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }
        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        //Create new sensor
        try {
            Main.sensorHandler.addSensor(sensorName, sensorTopic, sensorType, toStore, timeout);
        } catch (HavissIoTSensorException e) {
            Main.printMessage(e.getMessage());
        }
        Main.printMessage("Adding sensor " + sensorName);
        return Integer.toString(HttpStatus.SC_OK);
    }

    //List all sensor in jsonArray
    private String list() {
        JsonArray jsonArray = new JsonArray();
        for(IoTSensor s : Main.sensorHandler.getSensorsList()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", s.getName());
            object.addProperty("topic", s.getTopic());
            object.addProperty("type", s.getType());
            object.addProperty("lastValue", s.getLastValue());
            object.addProperty("toStore", s.getStorage());
            jsonArray.add(object);
        }
        return jsonArray.toString();
    }

    //Remove a sensor from sensor handler
    private String remove(JsonObject parameters) {
        if (parameters.has("name")) {
            try {
                Main.sensorHandler.removeSensorByName(parameters.get("name").getAsString());
            } catch (HavissIoTSensorException e) {
                Main.printMessage(e.getMessage());
            }
            Main.printMessage("Removing sensor with name " + parameters.get("topic"));
            return Integer.toString(HttpStatus.SC_OK);
        }
        if (parameters.has("topic")) {
            Main.sensorHandler.removeSensorByTopic(parameters.get("topic").getAsString());
            Main.printMessage("Removing sensor on topic " + parameters.get("topic"));
            return Integer.toString(HttpStatus.SC_OK);
        }
        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
    }

    //Get a sensor by name and return sensor information
    private String getSensor(JsonObject parameters) {
        IoTSensor sensor;
        if(parameters.has("name")) {
            String name = parameters.get("name").getAsString();
            sensor = Main.sensorHandler.getSensorByName(name);
        } else if(parameters.has("topic")) {
            String topic = parameters.get("topic").getAsString();
            sensor = Main.sensorHandler.getSensorByTopic(topic);
        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        if(sensor != null) {
            //Create a json object with useful information about the sensor as response
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", sensor.getName());
            jsonObject.addProperty("topic", sensor.getTopic());
            jsonObject.addProperty("type", sensor.getType());
            jsonObject.addProperty("toStore", sensor.getStorage());
            return jsonObject.toString();
        } else {
            return Integer.toString(HttpStatus.SC_NOT_FOUND);
        }
    }
}
