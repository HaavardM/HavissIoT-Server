package net.haviss.havissIoT.Communication;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.HavissIoT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by H�vard on 5/8/2015.
 */
public class SocketServer implements Runnable  {

    private Thread serverThread;
    private String threadName = "ServerThread";
    private AtomicInteger connectedClients;
    private boolean[] clientNames;
    private int maxClients;
    private int serverPort;
    private final Object serverLock = new Object();

    public SocketServer(int serverPort, int maxClients) {
        this.serverPort = serverPort;
        this.maxClients = maxClients;
        this.connectedClients = new AtomicInteger(0);
        this.clientNames = new boolean[Config.numbOfClients];
        Arrays.fill(this.clientNames, false);
        if(serverThread == null) {
            serverThread = new Thread(this, this.threadName);
            HavissIoT.allThreads.add(serverThread);
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
                    for(int i = 0; i < Config.numbOfClients; i++) {
                        if(!clientNames[i]) {
                            //Start new thread for new client
                            new SocketClient(socket, this, i);
                            connectedClients.incrementAndGet();
                            HavissIoT.printMessage("Client " + Integer.toString(i) + " connected");
                            HavissIoT.printMessage("Number of clients: " + Integer.toString(connectedClients.get()));
                            break;
                        }
                    }
                }

                //Wait while all clients are used - saves cycles
                synchronized (serverLock) {
                    serverLock.wait();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //Remove thread from list on finish
        HavissIoT.allThreads.remove(serverThread);
    }

    //Method for decrementing number of clients - if client disconnects
    public void removeOneClient(int number) {
        this.connectedClients.decrementAndGet();
        this.clientNames[number] = false;
    }

    //Notify server thread if new clients can connect
    public synchronized void notifyThread() {
        synchronized (serverLock) {
            serverLock.notify();
        }
    }
}
