package net.haviss.havissIoT.Device;

import com.google.gson.Gson;
import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 5/11/2015.
 */
public class RPI implements DeviceCallback {

    /*Variables*/
    private String deviceName;
    private String deviceTopic;

    //Constructor
    public RPI(String deviceName, String commandTopic, String statusTopic) {
        this.deviceName = deviceName;
        this.deviceTopic = commandTopic;
    }

    @Override
    public String run(String[] parameters) {
        return null;
    }

    @Override
    public String getName() {
        return this.deviceName;
    }

    @Override
    public String getDeviceTopic() {
        return this.deviceTopic;
    }


}
