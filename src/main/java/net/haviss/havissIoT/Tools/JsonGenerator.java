package net.haviss.havissIoT.Tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensors.IoTSensor;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Håvard on 25.05.2016.
 */
public class JsonGenerator {
    public static JsonArray getAllDevices() {
        JsonArray devices = new JsonArray();
        for(IoTDevice d : Main.deviceHandler.getAllDevices()) {
            JsonObject device = createJsonDevice(d);
            devices.add(device);
        }
        return devices;
    }

    @Nullable
    public static JsonObject createJsonDevice(IoTDevice device) {
        JsonObject jsonDevice = new JsonObject();
        if(device == null) {
            return null;
        }
        jsonDevice.addProperty("name", device.getName());
        jsonDevice.addProperty("topic", device.getTopic());
        jsonDevice.addProperty("datatype", device.getDataType().getValue());
        jsonDevice.addProperty("type", device.getDeviceType().toString());
        jsonDevice.addProperty("qos", device.getQos().getValue());
        JsonArray sensors = new JsonArray();
        for(IoTSensor s : device.getSensors()) {
            JsonObject sensor = new JsonObject();
            sensor.addProperty("name", s.getName());
            sensor.addProperty("topic", s.getTopic());
            sensor.addProperty("datatype", s.getDataType().getValue());
            sensor.addProperty("type", s.getSensorType().toString());
            sensor.addProperty("unit", s.getUnit().toString());
            sensor.addProperty("qos", s.getQos().getValue());
            sensors.add(sensor);
        }
        jsonDevice.add("sensors", sensors);
        return jsonDevice;
    }
}
