package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Haavard on 5/9/2015.
 * Sends command to an IoT device - makes it possible to control things over internet fra clients
 */
public class CommandDevice implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        //TODO: Create device class and create this command.
        return null;
    }

    @Override
    public String getName() {
        return "device";
    }
}
