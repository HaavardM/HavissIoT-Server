package net.haviss.havissIoT.Communication;

import com.sun.net.httpserver.HttpServer;
import net.haviss.havissIoT.Communication.APIHandlers.APIRespondTest;
import net.haviss.havissIoT.Communication.APIHandlers.Device;
import net.haviss.havissIoT.Exceptions.HavissIoTHttpException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by haavard on 03.03.2016.
 */
public class APIServer {

    //<editor-fold desc="Objects">
    private HttpServer server = null;
    //</editor-fold>


    //<editor-fold desc="Variables">
    private int port = 80;
    //</editor-fold>

    public APIServer() throws HavissIoTHttpException {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            Device d = new Device();
            server.createContext(d.getAPIPath(), d);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new HavissIoTHttpException(e.getMessage());
        }
    }


}
