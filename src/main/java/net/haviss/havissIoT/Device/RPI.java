package net.haviss.havissIoT.Device;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 5/11/2015.
 */
public class RPI implements DeviceCallback {

    /*Variables*/
    private String deviceName;
    private String commandTopic;

    //Constructor
    public RPI(String deviceName, String commandTopic, String statusTopic) {
        this.deviceName = deviceName;
        this.commandTopic = commandTopic;
    }

    @Override
    public String run(String[] parameters) {
        HavissIoT.client.publishMessage(commandTopic, new Gson().toJson(parameters));
        return new Gson().toJson("success");
    }

    @Override
    public String getName() {
        return this.deviceName;
    }

    @Override
    public String getCommandTopic() {
        return this.commandTopic;
    }


}
