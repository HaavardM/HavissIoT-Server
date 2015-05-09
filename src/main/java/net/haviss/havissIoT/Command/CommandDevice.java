package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Haavard on 5/9/2015.
 * Sends command to an IoT device - makes it possible to control things over internet fra clients
 */
public class CommandDevice implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        if(parameters.length != 2) {
            return "Unexpected number of parameters";
        } else {
            HavissIoT.client.publishMessage(parameters[0], parameters[1]);
            HavissIoT.printMessage("Performed action on device " + parameters[0] + ": " + parameters[1]);
            //TODO: Check if device replys success
            return "success";
        }
    }

    @Override
    public String getName() {
        return "device";
    }
}
