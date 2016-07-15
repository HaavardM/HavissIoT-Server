package net.haviss.havissIoT.Handlers;

import com.google.common.util.concurrent.Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.haviss.havissIoT.Communication.MQTTQOS;
import net.haviss.havissIoT.Device.IoTDevice;
import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Sensors.IoTSensor;
import net.haviss.havissIoT.Tools.JsonGenerator;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.DeviceType;
import net.haviss.havissIoT.Type.Location;
import net.haviss.havissIoT.Type.SensorType;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by haavard on 06.03.2016.
 * Handles all available devices for the system.
 */
public class DeviceHandler {

    private CopyOnWriteArrayList<IoTDevice> availableDevices = new CopyOnWriteArrayList<>();
    private MysqlDataSource dataSource;
    public DeviceHandler() {
        dataSource = new MysqlDataSource();
        dataSource.setUser("havissiot");
        dataSource.setPassword("deeplearning");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("havissiot");
    }


    public IoTDevice getDeviceByName(String name) {
        for (IoTDevice d : availableDevices) {
            if(d.getName() == name)
                return d;
        }
        return null;
    }

    private IoTDevice constructDeviceFromResult(ResultSet rs) throws SQLException {
            while (rs.next()) {
                String deviceName = rs.getString("name");
                String deviceTopic = rs.getString("topic");
                int deviceType = rs.getInt("type");
                int deviceQos = rs.getInt("qos");
                int deviceDataType = rs.getInt("datatype");
                int deviceId = rs.getInt("id");
                MQTTQOS qos = MQTTQOS.fromValue((byte) deviceQos);
                DataType dataType = DataType.parseValue(deviceDataType);
                DeviceType type = DeviceType.parseValue(deviceType);
                return IoTDevice.createDevice(deviceId, deviceName, deviceTopic, type, dataType, qos);
            }
        return null;
    }

    public IoTDevice getDeviceByTopic(String topic) {
        for(IoTDevice d : availableDevices) {
            if(d.getTopic() == topic) {
                return d;
            }
        }
        return null;
    }


    private void loadAllDevicesFromDatabase() {
        try {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, topic, type, datatype, qos FROM devices");
            if(rs.getRow() > 0) {
                availableDevices.clear();
                for (int i = 0; rs.next(); i++) {
                    availableDevices.add(constructDeviceFromResult(rs));
                }
            }
        } catch (SQLException e) {
            Main.printMessage(e.getMessage());
        }
    }

    public IoTDevice[] getAllDevices() {
        return (IoTDevice[]) availableDevices.toArray();
    }


    public void deliverMessage(String topic, String message) throws HavissIoTDeviceException {
        IoTDevice d = getDeviceByTopic(topic);
        if(d != null)
            d.messageArrived(topic, message);
        else
            throw new HavissIoTDeviceException(null, "Device not found");
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
            JsonArray devices = JsonGenerator.getAllDevices();
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
            MQTTQOS deviceQos;
            name = device.get("name").getAsString();
            topic = device.get("topic").getAsString();
            type = DeviceType.valueOf(device.get("type").getAsString());
            dataType = DataType.valueOf(device.get("datatype").getAsString());
            deviceQos = MQTTQOS.fromValue(device.get("qos").getAsByte());
            IoTDevice d = IoTDevice.createDevice(name, topic, type, dataType, deviceQos);
            JsonArray sensors = device.get("sensors").getAsJsonArray();
            for(JsonElement s : sensors) {
                JsonObject sensor = s.getAsJsonObject();
                String sensorName, sensorTopic;
                SensorType sensorType;
                DataType sensorDataType;
                MQTTQOS sensorQos;
                sensorName = sensor.get("name").getAsString();
                sensorTopic = sensor.get("topic").getAsString();
                sensorType = SensorType.valueOf(sensor.get("type").getAsString());
                sensorDataType = DataType.valueOf(sensor.get("datatype").getAsString());
                sensorQos = MQTTQOS.fromValue(sensor.get("qos").getAsByte());
            }

        }


    }


    }
