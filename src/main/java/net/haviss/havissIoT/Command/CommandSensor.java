package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;

import java.util.Arrays;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSensor implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        StringBuilder builder = new StringBuilder();
        if(parameters[0].compareTo("CREATE_NEW") == 0) {
            HavissIoT.sensorHandler.addSensor(parameters[1], parameters[2], parameters[3],Boolean.parseBoolean(parameters[4]));
            HavissIoT.printMessage("Adding sensor " + parameters[1]);
            return new Gson().toJson(HttpStatus.SC_OK);
        } else if(parameters[0].compareTo("REMOVE") == 0) {
            HavissIoT.sensorHandler.removeSensor(parameters[1]);
            HavissIoT.printMessage("Removing sensors " + parameters[1]);
            return new Gson().toJson(HttpStatus.SC_OK);
        } else if(parameters[0].compareTo("LIST_ALL") == 0) {
            HavissIoT.printMessage("Listing all sensors");
            return new Gson().toJson(HavissIoT.sensorHandler.getSensorsList());
        } else if(parameters[0].compareTo("STORE_SENSORS") == 0) {
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
