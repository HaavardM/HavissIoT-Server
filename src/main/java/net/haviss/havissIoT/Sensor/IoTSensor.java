package net.haviss.havissIoT.Sensor;

import com.mongodb.async.client.MongoCollection;
import net.haviss.havissIoT.HavissIoT;
import org.bson.Document;

/**
 * Created by Haavard on 5/16/2015.
 * Class containing information about a sensor
 */
public abstract class IoTSensor<SensorValue> {

    /*Variables*/
    protected volatile String name;
    protected volatile String topic;
    protected volatile SensorValue lastValue;
    protected volatile long lastUpdated;
    protected volatile long timeout;
    private volatile boolean storage;
    private volatile boolean isActive = false;

    public IoTSensor(String name, String topic, boolean toStore) {
        this.name = name;
        this.topic = topic;
        this.storage = toStore;
        this.timeout = 0;
    }
    public IoTSensor(String name, String topic, boolean toStore, long timeout) {
        this.name = name;
        this.topic = topic;
        this.storage = toStore;
        this.timeout = timeout;
    }


    //Updates last value
    public void updateValue(SensorValue value) {
        this.lastValue = value;
        this.isActive = true;

    }

    //Change sensor topic
    public void changeTopic(String topic) {
        this.topic = topic;
    }

    //Change sensor name
    public void changeName(String name) {
        this.name = name;
    }

    //Set state of toStore
    public void setStorage(boolean state) {
        this.storage = state;
    }

    //Get sensor name
    public String getName() {
        return this.name;
    }

    //Get sensor topic
    public String getTopic() {
        return this.topic;
    }

    //Get latest value
    public SensorValue getLastValue() {
        return this.lastValue;
    }

    //Check if sensordata is to be stored
    public boolean getStorage() {
        return this.storage;
    }

    //Set timeout
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    //Checks if sensor is inactive
    public boolean checkActive() {
        //Timeout less than or equal to 0 => no timeout
        if(this.timeout <= 0) {
            this.isActive = true;
            return true;
        } else if(System.currentTimeMillis() - this.lastUpdated > this.timeout) {
            this.isActive = false;
            return false;
        } else {
            this.isActive = true;
            return true;
        }
    }

    //Return value of isActive
    public boolean isActive() {
        return this.isActive;
    }

    public String getType() {
        return "DEFAULT";
    }


}
