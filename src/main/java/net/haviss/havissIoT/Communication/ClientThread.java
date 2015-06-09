package net.haviss.havissIoT.Communication;

import com.google.gson.*;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Core.CommandHandler;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


/**
 * Created by HaavardM on 5/8/2015.
 * Thread for handling client sockets
 * New thread are created for each client connection - avoids client blocking other clients.
 * IMPORTANT: Data must be terminated with new line - or it will block thread!
 */
public class ClientThread implements Runnable {

    private Socket socket; //Socket connection to client
    private SocketCommunication socketCommunication; //For terminating connection
    private Thread clientThread; //New thread for client connection
    private String threadName = "ClientThread"; //
    private int clientNum;
    private volatile long lastActivity;
    private User user;
    private boolean connectionClosed = false;
    private BufferedWriter output;
    private BufferedReader input;
    private JsonParser parser;

    //Constructor - loading objects and values
    public ClientThread(Socket socket, SocketCommunication socketCommunication, int clientNum) {
        this.socket = socket;
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum); //Giving the thread an unique name
        this.clientNum = clientNum;
        this.parser = new JsonParser();
        user = HavissIoT.userHandler.getUser("guest");
        if(user == null) {
            HavissIoT.printMessage("ERROR - no guest user - stopping application");
            System.exit(1);
        }
        //Set input and output stream read/writer
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            //Exception is unexpected
            HavissIoT.printMessage(e.getMessage());
            input = null;
            output = null;
            connectionClosed = true;
        }
        //Starting thread
        if(clientThread == null) {
             clientThread = new Thread(this, threadName);
            HavissIoT.allThreads.add(clientThread);
            clientThread.start();
        }
    }
    @Override
    public void run() {
        HavissIoT.printMessage("New client thread started");
        //Load new commandhandler and load I/O-streams
        CommandHandler commandHandler = new CommandHandler();

        //Strings to store command and result from commandhandler
        String commandString;
        String result;
        String command;
        JsonObject arguments;
        JsonObject response = new JsonObject();
        response.addProperty("user", user.getName());
        lastActivity = System.currentTimeMillis();

        //Timer to check if sockets are unused (and needs to be closed)
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(System.currentTimeMillis() - lastActivity > Config.keepAlive) {
                    connectionClosed = true;
                    HavissIoT.printMessage("Client " + Integer.toString(clientNum) + " timed out!");
                }
            }
        });

        //Start timer
        timer.start();

        //Thread should run until client disconnect
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (connectionClosed) {
                    //Close I/O streams
                    input.close();
                    output.close();
                    HavissIoT.printMessage("Client" + Integer.toString(clientNum) + " disconnected");
                    socket.close(); //Close socket
                    socketCommunication.removeOneClient(clientNum); //Remove one connected client
                    timer.stop();
                    Thread.currentThread().interrupt(); //Interrupt thread
                    break; //Break out of while loop (and thread will stop)
                }
                //Read until line-end - \n
                if(input.ready()) {
                    commandString = input.readLine();
                    lastActivity = System.currentTimeMillis();
                    if(commandString == null) {
                        connectionClosed = true;
                    } else if(commandString.compareTo("k") != 0)  {
                        //Print to console
                        HavissIoT.printMessage(this.threadName + ": " + commandString);
                        JsonObject object = parser.parse(commandString).getAsJsonObject();

                        if (object.has("user")) {
                            if (object.has("password")) {
                                this.user = HavissIoT.userHandler.getUser(object.get("name").getAsString(), object.get("password").getAsString().toCharArray());
                            } else {
                                this.user = HavissIoT.userHandler.getUser(object.get("name").getAsString());
                            }
                            response.remove("user");
                            response.addProperty("user", this.user.getName());
                        }

                        if (object.has("cmd") && object.has("args")) {
                            command = object.get("cmd").getAsString();
                            arguments = object.get("args").getAsJsonObject();
                            result = commandHandler.processCommand(command, arguments, user);
                        } else {
                            command = null;
                            arguments = null;
                            result = Integer.toString(HttpStatus.SC_BAD_REQUEST);
                        }
                        response.remove("r");

                        //Check if response is json
                        if (parser.parse(result).isJsonArray()) {
                            response.add("r", parser.parse(result).getAsJsonArray());
                        } else if (parser.parse(result).isJsonObject()) {
                            response.add("r", parser.parse(result).getAsJsonObject());
                        } else if (parser.parse(result).isJsonNull()) {
                            response.add("r", parser.parse(result).getAsJsonNull());
                        } else if (parser.parse(result).isJsonPrimitive()) {
                            response.add("r", parser.parse(result).getAsJsonPrimitive());
                        } else {
                            response.addProperty("r", result);
                        }
                        //Update the response json object
                        response.remove("cmd");
                        response.addProperty("cmd", command);
                        response.remove("args");
                        response.add("args", arguments);

                        //Send data back to client and flush output buffer
                        output.write(response.toString());
                        output.write("\n");
                        output.flush();
                    }
                }
            } catch (IOException e) {
                //Exception is expected if connection is lost.
                //Terminate connection and stop thread
                connectionClosed = true;
            } catch (JsonParseException e) {
                HavissIoT.printMessage(e.getMessage());
            }
        }
        //Remove thread when it shutdown
        HavissIoT.allThreads.remove(clientThread);
    }
}
