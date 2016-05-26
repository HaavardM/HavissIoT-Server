package net.haviss.havissIoT.Device.Devices;

import net.haviss.havissIoT.Communication.MQTTQOS;
import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Tools.Config;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.Location;

/**
 * Created by havar on 06.03.2016.
 */
public class ToggleDevice extends IoTDevice {
    private boolean state = false;
    public ToggleDevice(String name, String topic) {
        super(name, topic, DeviceType.Toggle, DataType.Boolean);
        try {
            Main.client.subscribeToTopic(getTopic() + "/*", Config.qos);
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public ToggleDevice(String name, String topic, Location location, MQTTQOS qos) {
        super(name, topic, DeviceType.Toggle, DataType.Boolean, location, qos);
        try {
            Main.client.subscribeToTopic(getTopic() + "/status", Config.qos);
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public void toggle() {
        try {
            Main.client.publishMessage(getTopic(), Boolean.toString(!state));
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public void updateState(String value) {
        if(value.toLowerCase() == "false")
            state = false;
        else if(value.toLowerCase() == "true")
            state = true;
        else
            Main.printMessage("Incorrect message on " + getName());
    }

    public void setState(boolean state) {
        try {
            Main.client.publishMessage(getTopic() + "/set",Boolean.toString(state));
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
