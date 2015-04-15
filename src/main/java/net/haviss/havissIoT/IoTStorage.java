package net.haviss.havissIoT;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Håvard on 4/3/2015.
 * This class connects to a mongodb database and handles all storage.
 */
public class IoTStorage implements Runnable {
    //Variables
    private String serverAddress = "";
    private int serverPort = 27017;
    private CopyOnWriteArrayList<String[]> toStore = new CopyOnWriteArrayList<>();
    private boolean stopThread = false;
    private String threadName = "storageThread";
    private boolean threadPaused = false;
    private boolean threadIsBusy = true;
    //Objects
    public Thread t;
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> dbCollection;
    public final Object lock = new Object();
    //Functions:
    @Override
    public void run() {
        try {
            System.out.println("Storage thread started");
            System.out.println("Thread name:\t" + t.getName());
            threadIsBusy = false;
            while (!Thread.interrupted()) {
                while (threadPaused) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                if(getToStore().size() > 0) {
                    try {
                        for (String[] s : getToStore()) {
                            getCollection(s[0]);
                            String date = new Date().toString();
                            Document document = new Document("Topic", s[0])
                                    .append("Value", s[1])
                                    .append("Date", date);
                            dbCollection.insertOne(document);
                        }
                        toStore.clear(); //All data has been stored, clear toStore list
                    } catch (MongoException e) {
                        e.printStackTrace();
                    }
                } else {
                    //If no data to store - wait for notification
                    synchronized (lock) {
                        lock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }
    }
    //To start thread
    public void start() {
        if(t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
    //Pause thread if needed
    public void pauseThread() {
        this.threadPaused = true; //Tell thread to pause
    }
    public void resumeThread() {
        this.threadPaused = false;
        synchronized (lock) {
            lock.notify(); //Notify thread - resumes thread after wait
        }
    }
    //Constructor - stores new values and connects to server
    public IoTStorage(String serverAddress, int serverPort, String db) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connect(this.serverAddress, this.serverPort);
        this.db = mongoClient.getDatabase(db);
    }
    //Overloaded constructor - with authentication
    public IoTStorage(String serverAddress, int serverPort, String username, String password, String db) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.connect(this.serverAddress, this.serverPort, username, password, db);
        this.db = mongoClient.getDatabase(db);
    }
    // Connects to server
    private void connect(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
        this.mongoClient = new MongoClient(serverAddress, serverPort);
    }
//Overloaded connect funtion to enable autentication
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
    }
        //Gets the toStore list - synchronized
    public  CopyOnWriteArrayList<String[]> getToStore() {
        return toStore;
    }
    public  boolean isThreadBusy() {
        return this.threadIsBusy;
    }
    public  void setThreadState  (boolean state) {
        this.threadIsBusy = state;
    }
    public void stop() {
       //TODO: Stop thread
    }
}
