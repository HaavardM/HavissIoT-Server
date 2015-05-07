package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.IoTClient;

/**
 * Created by Håvard on 5/2/2015.
 */
public class CommandTopics implements CommandCallback {
    @Override
    public void run(String[] parameters) {
        String topics = "";
        for(String s : HavissIoT.client.topics) {
            topics += s;
            topics += '\n';
        }
        //TODO: Return available topics
    }

    @Override
    public String getHelp() {
        //TODO: Return help string
        return "topics\tGet all available topics\n" +
                "USAGE: topics";
    }

    @Override
    public String getName() {
        return "-topics";
    }
}
