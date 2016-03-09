package net.haviss.havissIoT.Device.Devices;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Device.Device;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.Room;

/**
 * Created by havar on 06.03.2016.
 */
public class ToggleDevice extends Device {
    private boolean state = false;
    public ToggleDevice(String name, String topic) {
        super(name, topic);
        try {
            HavissIoT.client.subscribeToTopic(getTopic() + "/*", Config.qos);
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
        }
    }

    public ToggleDevice(String name, String topic, Room room) {
        super(name, topic, room);
        try {
            HavissIoT.client.subscribeToTopic(getTopic() + "/status", Config.qos);
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
        }
    }

    public void toggle() {
        try {
            HavissIoT.client.publishMessage(getTopic(), Boolean.toString(!state));
        } catch (HavissIoTMQTTException e) {
            HavissIoT.printMessage(e.getMessage());
        }
    }

    public void updateState(String value) {
        if(value.toLowerCase() == "false")
            state = false;
        else if(value.toLowerCase() == "true")
            state = true;
        else
            HavissIoT.printMessage("Incorrect message on " + getName());
    }

    public void setState(boolean state) {
        try {
            HavissIoT.client.publishMessage(getTopic() + "/set",Boolean.toString(state));
        } catch (HavissIoTMQTTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, String message) {

    }

    @Override
    public void messageDelivered(String topic) {

    }
}
