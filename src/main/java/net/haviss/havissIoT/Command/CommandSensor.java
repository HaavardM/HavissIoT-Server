package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSensor implements CommandCallback {
    @Override
    public String run(JsonObject parameters, User user) {
        String intent;
        boolean isOP = false;
        isOP = user != null && user.isOP();
        if (parameters.has("intent")) {
            intent = parameters.get("intent").getAsString().toUpperCase();
        } else {
            //Return bad request if there is no intent key in JSON
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        if (intent.compareTo("CREATE") == 0) {
            if (isOP) {
                return create(parameters);
            }
            return Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE);

        } else if (intent.compareTo("REMOVE") == 0) {
            if(isOP) {
                return remove(parameters);
            }
            return Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE);
        } else if (intent.compareTo("LIST") == 0) {
            HavissIoT.printMessage("Listing all sensors");
            return new Gson().toJson(HavissIoT.sensorHandler.getSensorsList());
        } else if (intent.compareTo("SAVE") == 0) {
            if(isOP) {
                HavissIoT.sensorHandler.writeToFile();
                return Integer.toString(HttpStatus.SC_OK);
            }
            return Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE);
        } else {
            return Integer.toString(HttpStatus.SC_NOT_FOUND);
        }
    }

    @Override
    public String getName() {
        return "SENSOR";
    }

    private String create(JsonObject parameters) {
        String sensorName;
        String sensorTopic;
        String sensorType;
        Boolean toStore;
        //Must contain a name and topic
        if (parameters.has("name") && parameters.has("topic") && parameters.has("type") && parameters.has("toStore")) {
            try {
                sensorName = parameters.get("name").getAsString();
                sensorTopic = parameters.get("topic").getAsString();
                sensorType = parameters.get("type").getAsString();
                toStore = parameters.get("toStore").getAsBoolean();
            } catch (ClassCastException e) {
                HavissIoT.printMessage(e.getMessage());
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }
        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        //Create new sensor
        HavissIoT.sensorHandler.addSensor(sensorName, sensorTopic, sensorType, toStore);
        HavissIoT.printMessage("Adding sensor " + sensorName);
        return Integer.toString(HttpStatus.SC_OK);
    }

    private String remove(JsonObject parameters) {
        if (parameters.has("name")) {
            HavissIoT.sensorHandler.removeSensorByName(parameters.get("name").getAsString());
            HavissIoT.printMessage("Removing sensor with name " + parameters.get("topic"));
            return Integer.toString(HttpStatus.SC_OK);
        }
        if (parameters.has("topic")) {
            HavissIoT.sensorHandler.removeSensorByTopic(parameters.get("topic").getAsString());
            HavissIoT.printMessage("Removing sensor on topic " + parameters.get("topic"));
            return Integer.toString(HttpStatus.SC_OK);
        }
        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
    }

}
