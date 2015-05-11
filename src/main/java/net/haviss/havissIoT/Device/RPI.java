package net.haviss.havissIoT.Device;

/**
 * Created by Håvard on 5/11/2015.
 */
public class RPI implements DeviceCallback {

    /*Variables*/
    private String deviceName;
    private String commandTopic;
    private String statusTopic;

    //Constructor
    public RPI(String deviceName, String commandTopic, String statusTopic) {
        this.deviceName = deviceName;
        this.commandTopic = commandTopic;
        this.statusTopic = statusTopic;
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
    public String getCommandTopic() {
        return this.commandTopic;
    }

    @Override
    public String getStatusTopic() {
        return this.statusTopic;
    }


}
