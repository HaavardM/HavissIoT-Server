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
    private volatile boolean storage;
    private volatile MongoCollection<Document> sensorCollection;

    //Constructor - setting variables
    public IoTSensor(String name, String topic, String type, boolean toStore) {
        this.topic = topic;
        this.name = name;
        this.type = type;
        this.storage = toStore;
        if(storage) {
            this.sensorCollection = HavissIoT.storage.getCollection(this.name);
        }
    }

    //Updates last value
    public void updateValue(String value) {
        this.lastValue = value;
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
        if(this.storage) {
            sensorCollection = HavissIoT.storage.getCollection(this.name);
        }
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

    //Check if sensordata is to be stored
    public boolean getStorage() {
        return this.storage;
    }

    public MongoCollection<Document> getCollection() {
        return this.sensorCollection;
    }



}
