package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSensor implements CommandCallback {
    @Override
    public String run(JSONObject parameters) {
        String subCmd;
        if(parameters.containsKey("intent")) {
            subCmd = ((String) parameters.get("intent")).toUpperCase();
        } else {
            //Return bad request if there is no intent key in JSON
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
        if (subCmd.compareTo("CREATE_NEW") == 0) {
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

        } else if (subCmd.compareTo("REMOVE") == 0) {
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
        } else if (subCmd.compareTo("LIST_ALL") == 0) {
            HavissIoT.printMessage("Listing all sensors");
            return new Gson().toJson(HavissIoT.sensorHandler.getSensorsList());
        } else if (subCmd.compareTo("STORE_SENSORS") == 0) {
            HavissIoT.sensorHandler.writeToFile();
            return Integer.toString(HttpStatus.SC_OK);
        } else {
            return Integer.toString(HttpStatus.SC_NOT_FOUND);
        }
    }

    @Override
    public String getName() {
        return "SENSOR";
    }
}
