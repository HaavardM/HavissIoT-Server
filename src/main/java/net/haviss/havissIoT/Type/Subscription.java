package net.haviss.havissIoT.Type;

import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Sensor.IoTSensor;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Håvard on 6/14/2015.
 */
public class Subscription {

    /*Objects*/
    private SocketClient client;
    private CopyOnWriteArrayList<IoTSensor> sensors = new CopyOnWriteArrayList<>();

    //Constructor
    public Subscription(SocketClient client) {
        this.client = client;
    }

    //Add sensor to list
    public void subscribe(IoTSensor sensor) {
        if(sensor != null) {
            this.sensors.add(sensor);
        }
    }

    //Remove sensor from list
    public void unsubscribe(IoTSensor sensor) {
        if(sensor != null) {
            this.sensors.remove(sensor);
        }
    }

    //Get sensors
    public CopyOnWriteArrayList<IoTSensor> getSensors() {
        return this.sensors;
    }

    //Get client
    public SocketClient getClient() {
        return this.client;
    }

    //Check if any sensors are subscribed to
    public boolean isSubscribed() {
        return this.sensors.size() > 0;
    }
}
