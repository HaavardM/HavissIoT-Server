package net.haviss.havissIoT.Device.Devices;

import net.haviss.havissIoT.Communication.MQTTQOS;
import net.haviss.havissIoT.Tools.Config;
import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.Location;

/**
 * Created by havar on 06.03.2016.
 */
public class AnalogDevice extends IoTDevice {

    private int value = 0;

    public AnalogDevice(String name, String topic, Location location, MQTTQOS qos) {
        super(name, topic, DeviceType.Analog, DataType.Integer, location, qos);
        try {
            Main.client.subscribeToTopic(getTopic() + "/set", Config.qos);
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public AnalogDevice(String name, String topic) {
        super(name, topic, DeviceType.Analog, DataType.Integer);
        try {
            Main.client.subscribeToTopic(getTopic() + "/value", Config.qos);
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public void setState(int state) {
        if(state >= 0 && state <= 255) {
            try {
                Main.client.publishMessage(getTopic() + "/set", Integer.toString(state));
            } catch (HavissIoTMQTTException e) {
                Main.printMessage(e.getMessage());
            }
        }
    }

    public void updateStatus(int value) {
        this.value = value;
    }


    @Override
    public void messageArrived(String topic, String message) throws HavissIoTDeviceException {
            String subTopic;
            if((subTopic = getSubTopic(topic)) != null) {
                switch (subTopic) {
                    case "/value":
                        //<editor-fold desc="Parse value">
                        try {
                            value = Integer.parseInt(message);
                        } catch (NumberFormatException e) {
                            throw new HavissIoTDeviceException(this, "Parse error on " + topic);
                        }
                        if (value > 255 || value < 0)
                            this.value = value;
                        else
                            throw new HavissIoTDeviceException(this, "Status value not within interval on " + topic);
                        //</editor-fold>
                        break;
                }
            }
    }

    @Override
    public void messageDelivered(String topic) {
        //TODO Handle not delivered if neccesary for device
    }
}
