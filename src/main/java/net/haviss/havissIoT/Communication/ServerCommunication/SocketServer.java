package net.haviss.havissIoT.Communication.ServerCommunication;

import net.haviss.havissIoT.Tools.Config;
import net.haviss.havissIoT.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by H�vard on 5/8/2015.
 */
public class SocketServer implements Runnable  {

    private Thread serverThread;
    private String threadName = "ServerThread";
    ServerSocket serverSocket;
    private AtomicInteger connectedClients;
    private boolean[] clientNames;
    private int maxClients;
    private int serverPort;
    private final Object serverLock = new Object();
    private boolean keepRunning = true;

    public SocketServer(int serverPort, int maxClients) {
        this.serverPort = serverPort;
        this.maxClients = maxClients;
        this.connectedClients = new AtomicInteger(0);
        this.clientNames = new boolean[Config.numbOfClients];
        Arrays.fill(this.clientNames, false);
        if(serverThread == null) {
            serverThread = new Thread(this, this.threadName);
            Main.allThreads.add(serverThread);
            serverThread.start();
        }
    }

    @Override
    public void run() {
        try {
            //Socket connection between server and client
            Socket socket = null;
            serverSocket = new ServerSocket(serverPort); //Serversocket
            if(serverSocket != null) {
                Main.printMessage("Serverserver is running");
            }
            Main.printMessage("Listening for socket clients on port " + Config.serverPort);
            //Run as long thread isn't interrupted
            while (keepRunning) {
                //Only accept new connection as long there are available connections
                while (connectedClients.get() < maxClients && keepRunning) {
                    socket = serverSocket.accept();
                    for(int i = 0; i < Config.numbOfClients; i++) {
                        if(!clientNames[i]) {
                            //Start new thread for new client
                            new SocketClient(socket, this, i);
                            connectedClients.incrementAndGet();
                            Main.printMessage("Client " + Integer.toString(i) + " connected");
                            Main.printMessage("Number of clients: " + Integer.toString(connectedClients.get()));
                            break;
                        }
                    }
                }
                //Wait while all clients are used - saves cycles
                synchronized (serverLock) {
                    serverLock.wait();
                }
            }
        }catch (SocketException se) {
            //Do nothing
        } catch (IOException | InterruptedException e) {
            Main.printMessage(e.getMessage());
        }
        //Remove thread from list on finish
        Main.allThreads.remove(serverThread);
        Main.printMessage("SocketServer shutting down");
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

    public void stopThread() throws IOException {
        keepRunning = false;
        notifyThread();
        serverSocket.close();
    }
}
