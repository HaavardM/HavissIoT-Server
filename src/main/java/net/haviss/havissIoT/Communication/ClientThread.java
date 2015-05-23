package net.haviss.havissIoT.Communication;

import com.google.gson.Gson;
import net.haviss.havissIoT.Core.CommandHandler;
import net.haviss.havissIoT.HavissIoT;
import org.apache.http.HttpStatus;

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
    private boolean connectionClosed = false;
    private BufferedWriter output;
    private BufferedReader input;

    //Constructor - loading objects and values
    public ClientThread(Socket socket, SocketCommunication socketCommunication, int clientNum) {
        this.socket = socket;
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum); //Giving the thread an unique name
        this.clientNum = clientNum;

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
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            //Exception is unexpected
            e.printStackTrace();
            input = null;
            output = null;
            connectionClosed = true;
        }

        //Strings to store command and result from commandhandler
        String commandString;
        String result;

        //Thread should run until client disconnect
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (connectionClosed) {
                    //Close I/O streams
                    input.close();
                    output.close();
                    HavissIoT.printMessage("Client" + Integer.toString(clientNum) + " disconnected");
                    socket.close(); //Close socket
                    socketCommunication.removeOneClient(); //Remove one connected client
                    Thread.currentThread().interrupt(); //Interrupt thread
                    break; //Break out of while loop (and thread will stop)
                }
                //Read until line-end - \n
                commandString = input.readLine();
                if(commandString == null) {
                    connectionClosed = true;
                } else {
                    //Print to console
                    HavissIoT.printMessage(this.threadName + ": " + commandString);

                    //if exit - close connection
                    if (commandString.compareTo("exit") == 0) {
                        connectionClosed = true;
                        result = new Gson().toJson(HttpStatus.SC_SERVICE_UNAVAILABLE);
                    } else {
                        result = commandHandler.processCommand(commandString);
                    }

                    //Send data back to client and flush output buffer
                    output.write(result);
                    output.flush();
                }
            } catch (IOException e) {
                //Exception is expected if connection is lost.
                //Terminate connection and stop thread
                connectionClosed = true;
            }
        }
    }
}
