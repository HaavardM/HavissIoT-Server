package net.haviss.havissIoT.Communication.ServerCommunication;

import com.google.gson.*;
import net.haviss.havissIoT.Tools.Config;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;



/**
 * Created by HaavardM on 5/8/2015.
 * Thread for handling client sockets
 * New thread are created for each client connection - avoids client blocking other clients.
 * IMPORTANT: Data must be terminated with new line - or it will block thread!
 */
public class SocketClient implements Runnable {

    private Socket socket; //Socket connection to client
    private SocketServer socketCommunication; //For terminating connection
    private Thread clientThread; //New thread for client connection
    private String threadName = "ClientThread"; //
    private int clientNum;
    private volatile long lastActivity;
    private User user;
    private boolean connectionClosed = false;
    private BufferedWriter output;
    private BufferedReader input;
    private JsonParser parser;
    private Timer timeOutTimer = null;

    //Constructor - loading objects and values
    public SocketClient(Socket socket, SocketServer socketCommunication, int clientNum) {
        this.socket = socket;
        try {
            this.socket.setTcpNoDelay(true);
        } catch (SocketException e) {
            Main.printMessage(e.getMessage());
        }
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum); //Giving the thread an unique name
        this.clientNum = clientNum;
        this.parser = new JsonParser();
        user = null;
        //Set input and output stream read/writer
        try {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socket.setSoTimeout(Config.readTimeout);
        } catch (IOException e) {
            //Exception is unexpected
            Main.printMessage(e.getMessage());
            input = null;
            output = null;
            connectionClosed = true;
        }
        //Starting thread
        if(clientThread == null) {
             clientThread = new Thread(this, threadName);
            Main.allThreads.add(clientThread);
            clientThread.start();
        }
    }
    @Override
    public void run() {
        Main.printMessage("New client thread started");
        //Load new commandhandler and load I/O-streams
        ServerCommandHandler serverCommandHandler = new ServerCommandHandler();

        //Strings to store command and result from commandhandler
        String commandString;
        String result;
        String command;
        JsonObject arguments;
        JsonObject response = new JsonObject();
        lastActivity = System.currentTimeMillis();

        //KeepAlive == 0 => unlimited

        if(Config.keepAlive > 0) {
            //Timer to check if sockets are unused (and needs to be closed)
            timeOutTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (System.currentTimeMillis() - lastActivity > Config.keepAlive) {
                        connectionClosed = true;
                        Main.printMessage("Client " + Integer.toString(clientNum) + " timed out!");
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
                    if(timeOutTimer != null) {
                        timeOutTimer.stop();
                    }
                    break; //Break out of while loop (and thread will stop)
                }
                if(input == null) {
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                }
                //Read until line-end - \n
                if(input.ready()) {
                    commandString = input.readLine();
                    if(Config.debugMode) {
                        Main.printMessage("commandstring: " + commandString);
                    }
                    lastActivity = System.currentTimeMillis();
                    if(commandString == null) {
                        connectionClosed = true;
                    } else if(commandString.toLowerCase().compareTo("ping") == 0) {
                        if(Config.debugMode)
                            Main.printMessage("Client " + Integer.toString(clientNum) + " pinged");

                    } else if(commandString.compareTo("close") == 0) {
                        connectionClosed = true;
                    } else if(isValidJson(commandString) && parser.parse(commandString).isJsonObject())  {
                        //Print to console
                        Main.printMessage(this.threadName + ": " + commandString);
                        JsonObject object = parser.parse(commandString).getAsJsonObject();

                        if ( object != null && object.has("user")) {
                            if (object.has("password")) {
                                this.user = Main.userHandler.getUser(object.get("user").getAsString(), object.get("password").getAsString().toCharArray());
                            } else {
                                this.user = Main.userHandler.getUser(object.get("user").getAsString());
                            }
                        }

                        if (object != null && object.has("cmd")) {
                            command = object.get("cmd").getAsString().toLowerCase();
                            if(Config.debugMode) {
                                Main.printMessage("cmd: " + command);
                            }
                            if(object.has("args") && object.get("args").isJsonObject()) {
                                arguments = object.get("args").getAsJsonObject();
                                result = serverCommandHandler.processCommand(command, arguments, this.user, this);
                                if(Config.debugMode) {
                                    Main.printMessage("args: " + arguments.toString());
                                }
                            } else {
                                result = serverCommandHandler.processCommand(command, this.user, this);
                                arguments = null;
                            }
                        } else {
                            command = null;
                            arguments = null;
                            result = Integer.toString(HttpStatus.SC_BAD_REQUEST);
                        }
                        //Process response and build json object
                        response = processResult(result, command, arguments);
                        if(Config.debugMode) {
                            Main.printMessage("Replying: " + response.toString());
                        }
                        //Send data back to client and flush output buffer
                        String toSend = response.toString();
                        int length = toSend.length();
                        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        output.write(Integer.toString(length) + "\n");
                        output.write(toSend);
                        output.flush();
                        output.close();
                        input.close();
                        socket.close();
                        connectionClosed = true;
                    } else {
                        Main.printMessage(commandString);
                    }
                }
            } catch (SocketTimeoutException e) {
                Main.printMessage(e.getMessage());
            } catch (JsonParseException e) {
              //TODO: Mabye do something here?
            } catch (IOException e) {
                //Exception is expected if connection is lost.
                //Terminate connection and stop thread
                connectionClosed = true;
            }
        }
        //Close I/O streams
        try {
            input.close();
            if(output != null) {
                output.close();
            }
            Main.printMessage("Client" + Integer.toString(clientNum) + " disconnected");
            socket.close(); //Close socket
            socketCommunication.removeOneClient(clientNum); //Remove one connected client
            if(timeOutTimer != null) {
                timeOutTimer.stop();
            }
        } catch (IOException e) {
            Main.printMessage(e.getMessage());
        }

        //Remove thread when it shutdown
        Main.allThreads.remove(clientThread);
        if(Config.debugMode) {
            Main.printMessage(clientThread.getName() + " is stopping");
        }
    }

    //Send message to client
    public synchronized void writeToSocket(String s) throws IOException {
        if(output != null) {
            output.write(s + "\n");
        }
    }

    //Proccess the result and generate a json object for result
    private JsonObject processResult(String result, String command, JsonObject arguments) {
        JsonObject response = new JsonObject();
        response.remove("r");

        //Check if response is json
        if(isValidJson(result)) {
            if (parser.parse(result).isJsonArray()) {
                response.add("r", parser.parse(result).getAsJsonArray());
            } else if (parser.parse(result).isJsonObject()) {
                response.add("r", parser.parse(result).getAsJsonObject());
            } else if (parser.parse(result).isJsonNull()) {
                response.add("r", parser.parse(result).getAsJsonNull());
            } else if (parser.parse(result).isJsonPrimitive()) {
                response.add("r", parser.parse(result).getAsJsonPrimitive());
            }
        }
        else {
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
        return response;
    }

    private boolean isValidJson(String jsonstring) {
        try {
            new Gson().fromJson(jsonstring, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}
