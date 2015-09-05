package net.haviss.havissIoT.Storage;

import com.mongodb.*;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import org.bson.Document;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by HaavardM on 4/3/2015.
 * This class connects to a mongodb database and handles all storage.
 */
public class IoTStorage  {

    /*Variables*/
    private String serverAddress = ""; //Database address
    private int serverPort = 27017; //Database port

    private String userCollectionName = "users";
    private String sensorCollectionName = "sensors";
    /*Objects*/
    private MongoClient mongoClient;
    private MongoDatabase db;
    private SingleResultCallback<Void> finishedCallBack = new SingleResultCallback<Void>() {

        @Override
        public void onResult(Void aVoid, Throwable throwable) {
            //TODO: Handle onResult
        }
    };

    /*Functions*/

    //Constructor - stores new values and connects to server
    public IoTStorage(String serverAddress, int serverPort, String db) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connect(this.serverAddress, this.serverPort);
        this.db = mongoClient.getDatabase(db);
    }

    // Connects to server
    private void connect(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
        this.mongoClient = MongoClients.create(new ConnectionString("mongodb://" + address));
    }

    //Get collection from database
    public MongoCollection<Document> getCollection(String collection) {
        return this.db.getCollection(collection);
    }

    //Gets all available collections from database
    public ArrayList<String> getCollectioNames() {
        final ArrayList<String> collections = new ArrayList<>();
        db.listCollectionNames().forEach(new Block<String>() {
            @Override
            public void apply(String s) {
                collections.add(s);
            }
        }, finishedCallBack);
        return collections;
    }

    //Gets all available database names from server
    public ArrayList<String> getDatabaseNames() {
        final ArrayList<String> databases = new ArrayList<>();
        this.mongoClient.listDatabaseNames().forEach(new Block<String>() {
            @Override
            public void apply(String s) {
                databases.add(s);
            }
        }, finishedCallBack);
        return databases;
    }

    //change database
    public void changeDatabase(String name) {
        this.db = this.mongoClient.getDatabase(name);
    }

    //Disconnect from mongodb
    public synchronized void disconnect() {
        this.mongoClient.close();
        this.mongoClient = null;
    }

    public

}
