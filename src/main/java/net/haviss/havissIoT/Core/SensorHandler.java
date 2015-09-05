package net.haviss.havissIoT.Core;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.IoTSensor;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 9/6/2015.
 */
public class SensorHandler {

    private MongoClient mClient;
    private MongoDatabase db;
    private SingleResultCallback<Void> finishedCallback = new SingleResultCallback<>() {
        @Override
        public void onResult(Object o, Throwable throwable) {
            //TODO: Handle success
        }
    };


    public SensorHandler(MongoClient client) {
        mClient = client;
        db = mClient.getDatabase(Config.database);
    }

    public void addSensor(IoTSensor sensor) {
        MongoCollection col = db.getCollection(Config.sensorsCollection);
        col.deleteMany(eq("name", sensor.getName()), finishedCallback);
        Document newSensor = new Document("name", sensor.getName())
                .append("topic", sensor.getTopic())
                .append("type", sensor.getType())
                .append("value", sensor.getLastValue());
    }
}
