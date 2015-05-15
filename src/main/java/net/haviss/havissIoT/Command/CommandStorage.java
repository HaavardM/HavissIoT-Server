package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;

import java.util.Arrays;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandStorage implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        StringBuilder builder = new StringBuilder();
        String[] topics = Arrays.copyOfRange(parameters, 1, parameters.length);
        if(parameters[0].compareTo("use") == 0) {
            for (String s : topics) {
                HavissIoT.client.subscribeToTopic(s, HavissIoT.client.getQOS());
                builder.append(" " + s);
            }
            HavissIoT.printMessage("Subscribing to" + builder.toString());
            return new Gson().toJson("success");
        } else if(parameters[0].compareTo("remove") == 0) {
            for(String s : topics) {
                HavissIoT.client.unsubscribeToTopic(s);
                builder.append(" " + s);
            }
            HavissIoT.printMessage("Unsubscribing to" + builder.toString());
            return new Gson().toJson("success");
        } else if(parameters[0].compareTo("show") == 0) {
            HavissIoT.printMessage("Sending all topics");
            return new Gson().toJson(HavissIoT.client.getTopics());
        } else {
            return new Gson().toJson("Couldn't resolve argument " + parameters[0]);
        }
    }

    @Override
    public String getName() {
        return "storage";
    }
}
