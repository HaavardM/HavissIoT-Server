package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.IoTClient;

/**
 * Created by Håvard on 5/2/2015.
 */
public class CommandSubscribe implements CommandCallback {
    @Override
    public void run(String[] parameters) {
        for(String s : parameters) {
            IoTClient.subscribeToTopic(s, IoTClient.getQOS());
            IoTClient.topics.add(s);
        }
    }

    @Override
    public String getHelp() {
        //TODO: Return help string
        return null;
    }

    @Override
    public String getName() {
        return "subscribe";
    }
}
