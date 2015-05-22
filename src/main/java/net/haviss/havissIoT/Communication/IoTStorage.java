package net.haviss.havissIoT.Communication;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
    private boolean sTConsole = true; //A storage thread writes to the console

    /*Anonymous classes*/
    //Using anonymous classes to enable use of multiple threads in one class
    //Runnable for storage thread
    private final Runnable storageHandler = new Runnable() {
        @Override
        public void run() {
            try {
                HavissIoT.printMessage("Storage thread started");
                sTConsole = false;
                while (!Thread.interrupted()) {
                    while (storagePaused) {
                        synchronized (storageLock) {
                            storageLock.wait();
                        }
                    }
                    if (getToStore().size() > 0) {
                        try {
                            for (String[] s : getToStore()) {
                                getCollection(s[0]);
                                String date = new Date().toString();
                                Document document = new Document("Topic", s[0])
                                        .append("Value", s[1])
                                        .append("Date", date);
                                //Insert document to database
                                dbCollection.insertOne(document);
                            }
                            toStore.clear(); //All data has been stored, clear toStore list
                        } catch (MongoException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //If no data to store - wait for notification
                        synchronized (storageLock) {
                            storageLock.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                //TODO: Handle exception
                e.printStackTrace();
            }
        }
    };

    /*Objects*/

    private Thread storageThread;
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> dbCollection;
    public final Object storageLock = new Object();;

    /*Functions*/

    //Start threads
    private void start() {
        if (storageThread == null) {
            storageThread = new Thread(this.storageHandler, storThreadName);
            storageThread.start();
        }
    }

    //Pause storage thread if needed
    public void pauseStorage() {
        this.storagePaused = true; //Tell thread to pause
    }

    //Resumes storage thread
    public synchronized void resumeStorage() {
        this.storagePaused = false;
        synchronized (storageLock) {
            storageLock.notify(); //Notify thread - resumes thread after wait
        }
    }


    //Constructor - stores new values and connects to server
    public IoTStorage(String serverAddress, int serverPort, String db) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connect(this.serverAddress, this.serverPort);
        this.db = mongoClient.getDatabase(db);
        this.start();
    }

    //Overloaded constructor - with authentication
    public IoTStorage(String serverAddress, int serverPort, String username, String password, String db) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connect(this.serverAddress, this.serverPort, username, password, db);
        this.db = mongoClient.getDatabase(db);
        this.start();
    }

    // Connects to server
    private void connect(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
        this.mongoClient = new MongoClient(serverAddress, serverPort);
    }

    //Overloaded connect function to enableauthenticationn
    private void connect(String address, int port, String username, String password, String db) {
        MongoCredential credential = MongoCredential.createCredential(username, db, password.toCharArray());
        this.mongoClient = new MongoClient(new ServerAddress(serverAddress), Arrays.asList(credential));
    }

    //Get collection from database
    public void getCollection(String collection) {
        this.dbCollection = db.getCollection(collection);
    }

    //Adding values for thread to store in Db
    public synchronized void addValues(String topic, String value) {
        String tempValues[] = {topic, value};
        toStore.add(tempValues);
        synchronized (storageLock) {
            storageLock.notify(); //Resumes thread after wait
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

    //Checks if thread is using the console. 
    public boolean getThreadConsole() {
        return (sTConsole);
    }

    //Stop storage thread
    public void stop() {
        //TODO: Stop thread
        storageThread.interrupt();

    }
}
