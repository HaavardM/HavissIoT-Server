package net.haviss.havissIoT.Communication;

import com.mongodb.*;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import net.haviss.havissIoT.HavissIoT;
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
    private CopyOnWriteArrayList<String> storedTopics = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<String[]> toStore = new CopyOnWriteArrayList<>(); //Values to store when ready
    private boolean stopThread = false; //Stop thread
    private String storThreadName = "storageThread"; //Storage thread name
    private boolean storagePaused = false;
    private boolean connected = false;


    /*Objects*/

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> dbCollection;
    private SingleResultCallback<Void> finishedCallBack = new SingleResultCallback<Void>() {

        @Override
        public void onResult(Void aVoid, Throwable throwable) {

        }
    };

    /*Functions*/

    //Pause storage thread if needed
    public void pauseStorage() {
        this.storagePaused = true; //Tell thread to pause
    }

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
    public void getCollection(String collection) {
        this.dbCollection = db.getCollection(collection);
    }

    //Adding values for thread to store in Db
    public synchronized void storeValues(String topic, String value) {
        try {
            String date = new Date().toString();
            Document document = new Document("Topic", topic)
                    .append("Value", value)
                    .append("Date", date);
            //Insert document to database
            dbCollection.insertOne(document, new SingleResultCallback<Void>() {
                @Override
                public void onResult(Void aVoid, Throwable throwable) {
                //TODO: Handle result
                }
            });
        } catch (MongoException e) {
            HavissIoT.printMessage("MONGO WRITE ERROR: " + e.getMessage());
        }
    }

    //Add topic to store
    public synchronized void addTopicsToStore(String topic) {
        storedTopics.add(topic);
    }

    //Get all stored topics
    public synchronized CopyOnWriteArrayList<String> getStoredTopics() {
        return storedTopics;
    }

    //Gets the toStore list - synchronized
    public CopyOnWriteArrayList<String[]> getToStore() {
        return toStore;
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
}
