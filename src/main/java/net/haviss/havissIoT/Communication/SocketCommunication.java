package net.haviss.havissIoT.Communication;

import net.haviss.havissIoT.HavissIoT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Håvard on 5/8/2015.
 */
public class SocketCommunication implements Runnable  {

    private Thread serverThread;
    private String threadName = "ServerThread";
    private AtomicInteger connectedClients;
    private int maxClients;
    private int serverPort;
    private final Object serverLock = new Object();

    public SocketCommunication(int serverPort, int maxClients) {
        this.serverPort = serverPort;
        this.maxClients = maxClients;
        connectedClients = new AtomicInteger(0);
        start();
    }

    public void start() {
            if(serverThread == null) {
                serverThread = new Thread(this, this.threadName);
                serverThread.start();
            }
        }

    @Override
    public void run() {
        try {

            //Socket connection between server and client
            Socket socket = null;
            ServerSocket serverSocket = new ServerSocket(serverPort); //Serversocket

            //Run as long thread isn't interrupted
            while (!Thread.interrupted()) {

                //Only accept new connection as long there are available connections
                while (connectedClients.get() < maxClients) {
                    socket = serverSocket.accept();
                    //Start new thread for new client
                    new ClientThread(socket, this, connectedClients.incrementAndGet());
                    HavissIoT.printMessage("New client connected");
                    HavissIoT.printMessage("Number of clients: " + Integer.toString(connectedClients.get()));
                }

                //Wait while all clients are used - saves cycles
                synchronized (serverLock) {
                    serverLock.wait();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Method for decrementing number of clients - if client disconnects
    public void removeOneClient() {
        connectedClients.decrementAndGet();
    }

    //Notify server thread if new clients can connect
    public void notifyThread() {
        serverLock.notify();
    }
}
