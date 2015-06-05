package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
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
    public String run(JSONObject parameters, User user) {
        String intent;
        boolean isOP = false;
        isOP = user != null && user.isOP();
        if (parameters.containsKey("intent")) {
            intent = ((String) parameters.get("intent")).toUpperCase();
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

    private String create(JSONObject parameters) {
        String sensorName;
        String sensorTopic;
        String sensorType;
        Boolean toStore;
        //Must contain a name and topic
        if (parameters.containsKey("name") && parameters.containsKey("topic") && parameters.containsKey("type") && parameters.containsKey("toStore")) {
            try {
                sensorName = (String) parameters.get("name");
                sensorTopic = (String) parameters.get("topic");
                sensorType = (String) parameters.get("type");
                toStore = (boolean) parameters.get("toStore");
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

    private String remove(JSONObject parameters) {
        if (parameters.containsKey("name")) {
            HavissIoT.sensorHandler.removeSensorByName((String) parameters.get("name"));
            HavissIoT.printMessage("Removing sensor with name " + parameters.get("topic"));
            return Integer.toString(HttpStatus.SC_OK);
        }
        if (parameters.containsKey("topic")) {
            HavissIoT.sensorHandler.removeSensorByTopic((String) parameters.get("topic"));
            HavissIoT.printMessage("Removing sensor on topic " + parameters.get("topic"));
            return Integer.toString(HttpStatus.SC_OK);
        }
        return Integer.toString(HttpStatus.SC_BAD_REQUEST);
    }

}
