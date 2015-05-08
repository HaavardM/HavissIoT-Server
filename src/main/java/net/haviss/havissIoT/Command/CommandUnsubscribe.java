package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 5/7/2015.
 * Command to unsubscribe to topic(s)
 */
public class CommandUnsubscribe implements CommandCallback {

    @Override
    public String run(String[] parameters) {
        StringBuilder builder = new StringBuilder();
        for(String s : parameters) {
            HavissIoT.client.unsubscribeToTopic(s);
            builder.append(" " + s);
        }
        HavissIoT.printMessage("Unsubscribing to" + builder.toString());
        return "success";
    }

    @Override
    public String getName() {
        return "-unsubscribe"; //TODO: Check spelling :P
    }
}
