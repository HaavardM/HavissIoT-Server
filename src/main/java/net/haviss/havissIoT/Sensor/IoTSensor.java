package net.haviss.havissIoT.Sensor;

import com.mongodb.async.client.MongoCollection;
import net.haviss.havissIoT.HavissIoT;
import org.bson.Document;

/**
 * Created by H�vard on 5/16/2015.
 */
public class IoTSensor {

    /*Variables*/
    private volatile String name;
    private volatile String type;
    private volatile String topic;
    private volatile String lastValue;
    private volatile long lastUpdated;
    private volatile long timeout;
    private volatile boolean storage;
    private volatile boolean isActive = false;

    //Constructor - setting variables
    public IoTSensor(String name, String topic, String type, boolean toStore, long timeout) {
        this.topic = topic;
        this.name = name;
        this.type = type;
        this.storage = toStore;
        this.timeout = timeout;
    }

    //Overloaded - no timeout on sensor
    public IoTSensor(String name, String topic, String type, boolean toStore) {
        this.topic = topic;
        this.name = name;
        this.type = type;
        this.storage = toStore;
        this.timeout = 0;
    }

    //Updates last value
    public void updateValue(String value) {
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

    //Get sensortype
    public String getType() {
        return this.type;
    }

    //Get latest value
    public String getLastValue() {
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



}
