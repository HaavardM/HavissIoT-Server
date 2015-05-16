package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;

import java.util.Arrays;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSensor implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        StringBuilder builder = new StringBuilder();
        String[] topics = Arrays.copyOfRange(parameters, 1, parameters.length);
        if(parameters[0].compareTo("CREATE_NEW") == 0) {
            for (String s : topics) {
                HavissIoT.client.subscribeToTopic(s, HavissIoT.client.getQOS());
                builder.append(" " + s);
            }
            HavissIoT.printMessage("Subscribing to" + builder.toString());
            return new Gson().toJson("success");
        } else if(parameters[0].compareTo("REMOVE") == 0) {
            for(String s : topics) {
                HavissIoT.client.unsubscribeToTopic(s);
                builder.append(" " + s);
            }
            HavissIoT.printMessage("Unsubscribing to" + builder.toString());
            return new Gson().toJson("success");
        } else if(parameters[0].compareTo("LIST_ALL") == 0) {
            HavissIoT.printMessage("Listing all sensors");
            return new Gson().toJson(HavissIoT.sensorHandler.getSensorsList());
        } else {
            return new Gson().toJson("Couldn't resolve argument " + parameters[0]);
        }
    }

    @Override
    public String getName() {
        return "SENSOR";
    }
}
