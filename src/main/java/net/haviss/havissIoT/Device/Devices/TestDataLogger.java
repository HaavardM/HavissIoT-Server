package net.haviss.havissIoT.Device.Devices;

import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Sensors.IoTSensor;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.SensorType;
import net.haviss.havissIoT.Type.SensorUnit;


/**
 * Created by havar on 10.04.2016.
 */
public class TestDataLogger extends IoTDevice {


    public TestDataLogger(String name, String topic) {
        super(name, topic, DeviceType.SensorGrid, DataType.String);
        availableSensors.add(new IoTSensor(name + "_temperature", getTopic() + "/temperature", SensorType.Temperature, DataType.Double, SensorUnit.Celsius));
        availableSensors.add(new IoTSensor(name + "_force", getTopic() + "/force", SensorType.Force, DataType.Double, SensorUnit.Newton));
    }

    @Override
    public void messageArrived(String topic, String message) throws HavissIoTDeviceException {
        for(IoTSensor s : availableSensors) {
            if(s.getTopic().compareTo(topic) == 0) {
                s.updateValue(message);
                return;
            }
        }
        //If reached - the device doesent use that topic - some design error somewhere
    }

    @Override
    public void messageDelivered(String topic) {

    }
}
