package net.haviss.havissIoT.Handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensors.IoTSensor;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.Location;
import net.haviss.havissIoT.Type.SensorType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by haavard on 06.03.2016.
 * Handles all available devices for the system.
 */
public class DeviceHandler {

    private CopyOnWriteArrayList<IoTDevice> availableDevices = new CopyOnWriteArrayList<>();

    public DeviceHandler() {
    }

    public IoTDevice getDeviceByName(String name) {
        for(IoTDevice d : availableDevices) {
            if(d.getName().compareTo(name) == 0)
                return d;
        }
        return null;
    }

    public IoTDevice getDeviceByTopic(String topic) {
        for(IoTDevice d : availableDevices) {
            if(d.getTopic().compareTo(topic) == 0)
                return d;
        }
        return null;
    }

    public IoTDevice[] getDevicesByRoom(Location room) {
        List<IoTDevice> returnList = new ArrayList<>();
        for(IoTDevice d : availableDevices) {
            if(d.getRoom() == room)
                returnList.add(d);
        }
        if(returnList.size() > 0)
            return (IoTDevice[])returnList.toArray();
        else
            return null;
    }

    public  IoTDevice[] getAllDevices() {
        IoTDevice[] devices = new IoTDevice[availableDevices.size()];
        availableDevices.toArray(devices);
        return  devices;
    }

    public void deliverMessage(String topic, String message) throws HavissIoTDeviceException {
        getDeviceByTopic(topic).messageArrived(topic, message);
    }

    public void addDevice(IoTDevice device) {
        for (IoTDevice d : availableDevices) {
            if(device.getName().compareTo(d.getName()) + device.getTopic().compareTo(d.getTopic()) == 0) {
                return;
            }
        }
        availableDevices.add(device);
        saveDevicesToFile();
    }

    public void saveDevicesToFile() {
        File targetFile = new File("devices.json");
        try {
            FileWriter writer = new FileWriter(targetFile);
            if(!targetFile.exists()) {
                if (!targetFile.createNewFile()) {
                    return;
                }
            }
            JsonArray devices = new JsonArray();
            for(IoTDevice d : availableDevices) {
                JsonObject device = new JsonObject();
                device.addProperty("name", d.getName());
                device.addProperty("topic", d.getTopic());
                device.addProperty("type", d.getDeviceType().toString());
                device.addProperty("datatype", d.getDataType().toString());
                JsonArray sensors = new JsonArray();
                for (IoTSensor s : d.getSensors()) {
                    JsonObject sensor = new JsonObject();
                    sensor.addProperty("name", s.getName());
                    sensor.addProperty("topic", s.getTopic());
                    sensor.addProperty("type", s.getSensorType().toString());
                    sensor.addProperty("datatype", s.getDataType().toString());
                    sensor.addProperty("unit", s.getUnit().toString());
                    sensors.add(sensor);
                }

                device.add("sensors", sensors);
                devices.add(device);
            }
            writer.write(devices.toString());

        } catch (IOException e) {
            Main.printMessage(e.getMessage());
            return;
        }


    }

    public void loadDevicesFromFile() throws IOException {

        File targetFile = new File("devices.json");
        if(!targetFile.exists())
            return;
        FileReader fileReader = new FileReader(targetFile);
        BufferedReader reader = new BufferedReader(fileReader);
        JsonArray jsonArray = new JsonParser().parse(reader).getAsJsonArray();
        for(JsonElement j : jsonArray) {
            JsonObject device = j.getAsJsonObject();
            String name, topic;
            DeviceType type;
            DataType dataType;
            name = device.get("name").getAsString();
            topic = device.get("topic").getAsString();
            type = DeviceType.valueOf(device.get("type").getAsString());
            dataType = DataType.valueOf(device.get("datatype").getAsString());
            IoTDevice d = IoTDevice.createDevice(name, topic, type, dataType);
            JsonArray sensors = device.get("sensors").getAsJsonArray();
            for(JsonElement s : sensors) {
                JsonObject sensor = s.getAsJsonObject();
                String sensorName, sensorTopic;
                SensorType sensorType;
                DataType sensorDataType;
            }

        }


    }


    }
