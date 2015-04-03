package net.haviss.havissIoTServer;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.Arrays;

/**
 * Created by Håvard on 4/3/2015.
 * This class connects to a mongodb database and handles all storage.
 */
public class havissIoTStorage {

    //Variables
/*-------------------------------------------------------------------------------------------------------------*/
    private String serverAddress = "";
    private int serverPort = 27017;
    //Objects
/*-------------------------------------------------------------------------------------------------------------*/
    private MongoClient mongoClient;
    public DBCollection dbCollection;
    //Functions:
/*-------------------------------------------------------------------------------------------------------------*/
    //Connects to server
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
}
