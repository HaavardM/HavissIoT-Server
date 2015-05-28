package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSensor implements CommandCallback {
    @Override
    public String run(JSONArray parameters) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> arguments = new JSONArray();
        for(int i = 0; i < parameters.size(); i++) {
            arguments.add(parameters.get(i).toString());
        }
        if(arguments.get(0).compareTo("CREATE_NEW") == 0) {
            HavissIoT.sensorHandler.addSensor(arguments.get(1), arguments.get(2), arguments.get(3),Boolean.parseBoolean(arguments.get(4)));
            HavissIoT.printMessage("Adding sensor " + arguments.get(1));
            return new Gson().toJson(HttpStatus.SC_OK);
        } else if(arguments.get(0).compareTo("REMOVE") == 0) {
            HavissIoT.sensorHandler.removeSensor(arguments.get(1));
            HavissIoT.printMessage("Removing sensors " + arguments.get(1));
            return new Gson().toJson(HttpStatus.SC_OK);
        } else if(arguments.get(0).compareTo("LIST_ALL") == 0) {
            HavissIoT.printMessage("Listing all sensors");
            return new Gson().toJson(HavissIoT.sensorHandler.getSensorsList());
        } else if(arguments.get(0).compareTo("STORE_SENSORS") == 0) {
            HavissIoT.sensorHandler.writeToFile();
            return new Gson().toJson(HttpStatus.SC_OK);
        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
    }

    @Override
    public String getName() {
        return "SENSOR";
    }
}
