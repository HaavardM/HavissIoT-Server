package net.haviss.havissIoT;

import java.io.*;
import java.net.Socket;

/**
 * Created by H�vard on 5/8/2015.
 */
public class ClientThread implements Runnable {

    private Socket socket; //Socket connection to client
    private SocketCommunication socketCommunication; //For terminating connection
    private Thread clientThread; //New thread for client connection
    private String threadName = "ClientThread"; //
    private boolean connectionClosed = false;

    //Constructor - loading objects and values
    public ClientThread(Socket socket, SocketCommunication socketCommunication, int clientNum) {
        this.socket = socket;
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum); //Giving the thread an unique name
        //Starting thread
        if(clientThread == null) {
             clientThread = new Thread(this, threadName);
            clientThread.start();
        }
    }
    @Override
    public void run() {
        HavissIoT.printMessage("New thread started");
        try {
            //Load new commandhandler and load I/O-streams
            CommandHandler commandHandler = new CommandHandler();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream output = new PrintStream(socket.getOutputStream());
            String commandString = "";
            while (!Thread.currentThread().isInterrupted()) {
                if (connectionClosed) { //TODO: properly check if connection is terminated
                    input.close(); //Close I/O streams
                    output.close();
                    HavissIoT.printMessage("Client disconnected");
                    socket.close(); //Close socket
                    socketCommunication.removeOneClient(); //Remove one connected client
                    Thread.currentThread().interrupt();
                    break;
                }
                if (input.ready()) {
                    commandString = input.readLine();
                    HavissIoT.printMessage(this.threadName + ": " + commandString);
                    if(commandString.compareTo("-exit") == 0) {
                        connectionClosed = true;
                        break;
                    }
                    String result = commandHandler.processCommand(commandString);
                    result += '\n';
                    output.write(result.getBytes());
                    output.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}