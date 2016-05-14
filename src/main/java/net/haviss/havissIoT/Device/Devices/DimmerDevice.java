package net.haviss.havissIoT.Device.Devices;

import net.haviss.havissIoT.Tools.Config;
import net.haviss.havissIoT.Device.Device;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.Location;

/**
 * Created by havar on 06.03.2016.
 */
public class DimmerDevice extends Device {

    private int status = 0;

    public DimmerDevice(String name, String topic, Location location) {
        super(name, topic, DeviceType.Analog, DataType.Integer, location);
        try {
            Main.client.subscribeToTopic(getTopic() + "/status", Config.qos);
        } catch (HavissIoTMQTTException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public DimmerDevice(String name, String topic) {
        super(name, topic, DeviceType.Analog, DataType.Integer);
        try {
            Main.client.subscribeToTopic(getTopic() + "/status", Config.qos);
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
        status = value;
    }


    @Override
    public void messageArrived(String topic, String message) throws HavissIoTDeviceException {
            String subTopic;
            if((subTopic = getSubTopic(topic)) != null) {
                switch (subTopic) {
                    case "/status":
                        //<editor-fold desc="Parse status">
                        try {
                            status = Integer.parseInt(message);
                        } catch (NumberFormatException e) {
                            throw new HavissIoTDeviceException(this, "Parse error on " + topic);
                        }
                        if (status > 255 || status < 0)
                            this.status = status;
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
