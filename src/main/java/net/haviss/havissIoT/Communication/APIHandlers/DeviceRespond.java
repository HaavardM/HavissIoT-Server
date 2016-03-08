package net.haviss.havissIoT.Communication.APIHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.haviss.havissIoT.HavissIoT;

import java.io.*;

/**
 * Created by Håvard on 07.03.2016.
 */
public class DeviceRespond implements APIRespond {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

            HavissIoT.printMessage(httpExchange.getRequestURI().toString());
            httpExchange.sendResponseHeaders(200, "Heisann".length());
            OutputStream os = httpExchange.getResponseBody();
            os.write("Heisann".getBytes());
            os.close();
    }

    @Override
    public String getAPIPath() {
        return "/device";
    }
}
