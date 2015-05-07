package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 5/7/2015.
 */
public class CommandUnsubscribe implements CommandCallback {

    @Override
    public void run(String[] parameters) {
        for(String s : parameters) {
            HavissIoT.client.unsubscribeToTopic(s);
        }
    }

    @Override
    public String getHelp() {
        return "unsubscribe\tUnsubscribes to topic\n" +
                "USAGE: unsubsrine {topic1} {topic2} {topic n}";
    }

    @Override
    public String getName() {
        return "-unsubscribe"; //TODO: Check spelling :P
    }
}
