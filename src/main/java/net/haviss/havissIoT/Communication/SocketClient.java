package net.haviss.havissIoT.Communication;

import com.google.gson.*;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Core.CommandHandler;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Sensor.IoTSensor;
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
import java.net.SocketTimeoutException;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by HaavardM on 5/8/2015.
 * Thread for handling client sockets
 * New thread are created for each client connection - avoids client blocking other clients.
 * IMPORTANT: Data must be terminated with new line - or it will block thread!
 */
public class SocketClient implements Runnable {

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
    private boolean hasSubscribed = false;
    private int updateSubscriptions = 1000;
    private CopyOnWriteArrayList<IoTSensor> subscribedSensors = new CopyOnWriteArrayList<>();
    private Timer timeOutTimer = null;
    private Timer subscriptionTimer;

    //Constructor - loading objects and values
    public SocketClient(Socket socket, SocketCommunication socketCommunication, int clientNum) {
        this.socket = socket;
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum); //Giving the thread an unique name
        this.clientNum = clientNum;
        this.parser = new JsonParser();
        user = null;
        //Set input and output stream read/writer
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.socket.setSoTimeout(Config.readTimeout);
        } catch (IOException e) {
            //Exception is unexpected
            HavissIoT.printMessage(e.getMessage());
            input = null;
            output = null;
            connectionClosed = true;
        }
        subscriptionTimer = new Timer(updateSubscriptions, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JsonObject sensorValues = new JsonObject();
                JsonObject sensorStatus = new JsonObject();
                JsonObject response = new JsonObject();
                for(IoTSensor s : subscribedSensors) {
                    sensorValues.addProperty(s.getName(), s.getLastValue());
                    sensorStatus.addProperty(s.getName(), s.isActive());
                }
                if(user != null) {
                    response.addProperty("user", user.getName());
                } else {
                    response.add("user", null);
                }
                response.add("values", sensorValues);
                response.add("status", sensorStatus);
                try {
                    output.write(response.toString() + "\n");
                } catch (IOException e1) {
                    HavissIoT.printMessage(e1.getMessage());
                }
            }
        });
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

        //KeepAlive == 0 => unlimited

        if(Config.keepAlive != 0) {
            //Timer to check if sockets are unused (and needs to be closed)
            timeOutTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (System.currentTimeMillis() - lastActivity > Config.keepAlive) {
                        connectionClosed = true;
                        HavissIoT.printMessage("Client " + Integer.toString(clientNum) + " timed out!");
                    }
                }
            });

            //Start timer
            timeOutTimer.start();
        }

        //Thread should run until client disconnect
        while (!Thread.currentThread().isInterrupted()) {

            try {
                if (connectionClosed) {
                    Thread.currentThread().interrupt(); //Interrupt thread
                    break; //Break out of while loop (and thread will stop)
                }
                //Read until line-end - \n
                if(input.ready()) {
                    commandString = input.readLine();
                    lastActivity = System.currentTimeMillis();
                    if(commandString == null) {
                        connectionClosed = true;
                    } else if(parser.parse(commandString).isJsonObject())  {
                        //Print to console
                        HavissIoT.printMessage(this.threadName + ": " + commandString);
                        JsonObject object = parser.parse(commandString).getAsJsonObject();

                        if (object.has("user")) {
                            if (object.has("password")) {
                                this.user = HavissIoT.userHandler.getUser(object.get("name").getAsString(), object.get("password").getAsString().toCharArray());
                            } else {
                                this.user = HavissIoT.userHandler.getUser(object.get("name").getAsString());
                            }
                        }

                        if (object.has("cmd")) {
                            command = object.get("cmd").getAsString();
                            if(object.has("args")) {
                                arguments = object.get("args").getAsJsonObject();
                                result = commandHandler.processCommand(command, arguments, this.user);
                            } else {
                                result = commandHandler.processCommand(command, this.user);
                                arguments = null;
                            }
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
                        response.remove("user");
                        if(user != null) {
                            response.addProperty("user", this.user.getName());
                        } else {
                            response.add("user", null);
                        }
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
            } catch (SocketTimeoutException | JsonParseException e) {
                HavissIoT.printMessage(e.getMessage());
            } catch (IOException e) {
                //Exception is expected if connection is lost.
                //Terminate connection and stop thread
                connectionClosed = true;
            }
        }
        //Close I/O streams
        try {
            input.close();
            output.close();
            HavissIoT.printMessage("Client" + Integer.toString(clientNum) + " disconnected");
            socket.close(); //Close socket
            socketCommunication.removeOneClient(clientNum); //Remove one connected client
            if(timeOutTimer != null) {
                timeOutTimer.stop();
            }
        } catch (IOException e) {
            HavissIoT.printMessage(e.getMessage());
        }

        //Remove thread when it shutdown
        HavissIoT.allThreads.remove(clientThread);
    }

    //Subscribe to new sensor
    public boolean subscribeToSensor(String sensorName) {
        IoTSensor sensor = HavissIoT.sensorHandler.getSensorByName(sensorName);
        if(sensor != null) {
            this.subscribedSensors.add(sensor);
            this.hasSubscribed = true;
            subscriptionTimer.start();
            return true;
        } else {
            if(this.subscribedSensors.size() <= 0) {
                this.hasSubscribed = false;
                if(this.subscriptionTimer.isRunning()) {
                    this.subscriptionTimer.stop();
                }
            }
            return false;
        }
    }

    //Unsubscribe to sensor
    public void unsubscribeToSensor(String sensorName) {
        IoTSensor sensor = HavissIoT.sensorHandler.getSensorByName(sensorName);
        if(sensor != null) {
            this.subscribedSensors.remove(sensor);
            if(this.subscribedSensors.size() <= 0) {
                this.hasSubscribed = false;
                if(this.subscriptionTimer.isRunning()) {
                    this.subscriptionTimer.stop();
                }
            }
        }
    }
}
