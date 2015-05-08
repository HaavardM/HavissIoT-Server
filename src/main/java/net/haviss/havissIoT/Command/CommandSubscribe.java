package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 5/2/2015.
 * Command to subscribe to topic(s)
 */
public class CommandSubscribe implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        StringBuilder builder = new StringBuilder();
        for(String s : parameters) {
            HavissIoT.client.subscribeToTopic(s, HavissIoT.client.getQOS());
            builder.append(" " + s);
        }
        HavissIoT.printMessage("Subscribing to" + builder.toString());
        return "success";
    }

    @Override
    public String getName() {
        return "-subscribe";
    }
}
