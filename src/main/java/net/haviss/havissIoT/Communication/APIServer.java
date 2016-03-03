package net.haviss.havissIoT.Communication;

import com.sun.net.httpserver.HttpServer;
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
    private InetSocketAddress address = null;
    //</editor-fold>


    //<editor-fold desc="Variables">
    private int port = 80;
    //</editor-fold>

    public APIServer() throws HavissIoTHttpException {
        try {
            server = HttpServer.create(address, port);
        } catch (IOException e) {
            throw new HavissIoTHttpException(e.getMessage());
        }
    }


}
