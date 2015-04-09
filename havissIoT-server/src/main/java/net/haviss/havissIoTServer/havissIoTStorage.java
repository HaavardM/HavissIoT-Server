package net.haviss.havissIoTServer;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Håvard on 4/3/2015.
 * This class connects to a mongodb database and handles all storage.
 */
public class havissIoTStorage implements Runnable {
    //Variables
    private String serverAddress = "";
    private int serverPort = 27017;
    private List<String> valuesToAdd = new ArrayList<String>();

    //Objects
    private MongoClient mongoClient;
    public DBCollection dbCollection;

    //Functions:
    @Override
    public void run() {
        connect(serverAddress, serverPort);
        //TODO: Handle code to be excecuted in new thread
    }
    //Constructor - stores new values and connects to server
    public havissIoTStorage(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connect(this.serverAddress, this.serverPort);
    }
    //Overloaded constructor - with authentication
    public havissIoTStorage(String serverAddress, int serverPort, String username, String password, String db) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connect(this.serverAddress, this.serverPort, username, password, db);
    }
    // Connects to server
    public void connect(String address, int port) {
        serverAddress = address;
        serverPort = port;
        mongoClient = new MongoClient(serverAddress, serverPort);
    }
    //Overloaded connect funtion to enable autentication
    public void connect(String address, int port, String username, String password, String db) {
        MongoCredential credential = MongoCredential.createCredential(username, db, password.toCharArray());
        mongoClient = new MongoClient(new ServerAddress(serverAddress), Arrays.asList(credential));
    }
    public void getCollection(String collection) {
        dbCollection = dbCollection.getCollection(collection);
    }
    public void addValue(String value) {
        valuesToAdd.add(value); //Adding values to list so the thread can process it.
    }
}
