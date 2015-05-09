package net.haviss.havissIoT.Command;

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
        if(parameters[0].compareTo("-add") == 0) {
            for (String s : topics) {
                HavissIoT.client.subscribeToTopic(s, HavissIoT.client.getQOS());
                builder.append(" " + s);
            }
            HavissIoT.printMessage("Subscribing to" + builder.toString());
            return "success";
        } else if(parameters[0].compareTo("-remove") == 0) {
            for(String s : topics) {
                HavissIoT.client.unsubscribeToTopic(s);
                builder.append(" " + s);
            }
            HavissIoT.printMessage("Unsubscribing to" + builder.toString());
            return "success";
        } else if(parameters[0].compareTo("-show") == 0) {
            for(String s : HavissIoT.client.getTopics()) {
                builder.append(s).append(" ");
            }
            HavissIoT.printMessage("Sending topics " + builder.toString());
            return builder.toString();
        } else {
            return "No corresponding argument";
        }
    }

    @Override
    public String getName() {
        return "storage";
    }
}
