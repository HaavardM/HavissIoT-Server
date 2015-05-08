package net.haviss.havissIoT;

import java.io.*;
import java.net.Socket;

/**
 * Created by Håvard on 5/8/2015.
 */
public class ClientThread implements Runnable {

    private Socket socket; //Socket connection to client
    private SocketCommunication socketCommunication; //To access
    private Thread clientThread;
    private String threadName = "ClientThread";

    public ClientThread(Socket socket, SocketCommunication socketCommunication, int clientNum) {
        this.socket = socket;
        this.socketCommunication = socketCommunication;
        threadName += Integer.toString(clientNum);
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
                if (output.checkError()) { //TODO: properly check if connection is terminated
                    input.close(); //Close I/O streams
                    output.close();
                    HavissIoT.printMessage("Client disconnected");
                    socket.close(); //Close socket
                    socketCommunication.removeOneClient(); //Remove one connected client
                    Thread.currentThread().interrupt();
                    break;
                }
                while(input.ready()) {
                    commandString = input.readLine();
                    HavissIoT.printMessage(this.threadName + ": " + commandString);
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
