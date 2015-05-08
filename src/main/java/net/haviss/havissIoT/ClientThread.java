package net.haviss.havissIoT;

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
       while(!Thread.currentThread().isInterrupted()) {
           if(socket.isClosed()) {
               socketCommunication.removeOneClient();
               Thread.currentThread().interrupt();
               break;
           }

       }
    }
}
