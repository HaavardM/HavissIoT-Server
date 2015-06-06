package net.haviss.havissIoT.Communication;

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
    private JSONParser parser;

    //Constructor - loading objects and values
    public ClientThread(Socket socket, SocketCommunication socketCommunication, int clientNum) {
        this.socket = socket;
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum); //Giving the thread an unique name
        this.clientNum = clientNum;
        this.parser = new JSONParser();
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
                    Thread.currentThread().interrupt(); //Interrupt thread
                    break; //Break out of while loop (and thread will stop)
                }
                //Read until line-end - \n
                commandString = input.readLine();
                lastActivity = System.currentTimeMillis();
                if(commandString == null) {
                    connectionClosed = true;
                } else {
                    //Print to console
                    HavissIoT.printMessage(this.threadName + ": " + commandString);
                    JSONObject object = (JSONObject) parser.parse(commandString);

                    if(object.containsKey("user")) {
                        if(object.containsKey("password")) {
                            this.user = HavissIoT.userHandler.getUser((String) object.get("user"), (char[]) object.get("password"));
                        } else {
                            this.user = HavissIoT.userHandler.getUser((String) object.get("user"));
                        }
                    }

                    if(object.containsKey("cmd") && object.containsKey("args")) {
                        result = commandHandler.processCommand((String)object.get("cmd"), (JSONObject) object.get("args"), user);

                    } else {
                        result = Integer.toString(HttpStatus.SC_BAD_REQUEST);
                    }

                    result += '\n';
                    //Send data back to client and flush output buffer
                    output.write(result);
                    output.flush();
                }
            } catch (IOException e) {
                //Exception is expected if connection is lost.
                //Terminate connection and stop thread
                connectionClosed = true;
            } catch (ParseException e) {
                HavissIoT.printMessage(e.getMessage());
            }
        }
    }
}
