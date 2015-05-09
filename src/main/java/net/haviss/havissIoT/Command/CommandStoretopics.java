package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.IoTClient;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by HaavardM on 5/2/2015.
 * Command to return topics to user
 */
public class CommandStoretopics implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        StringBuilder topicsString = new StringBuilder();
        for(String s : HavissIoT.client.getTopics()) {
            topicsString.append(s).append(" ");
        }
        HavissIoT.printMessage("Sending topics " + topicsString.toString());
        return topicsString.toString();
    }

    @Override
    public String getName() {
        return "-storetopics";
    }
}
