package net.haviss.havissIoT.Communication.APIHandlers;

import com.sun.deploy.net.HttpResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Håvard on 04.03.2016.
 */
public class APIRespondTest implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Hey, it verks! yezz";
        httpExchange.sendResponseHeaders(HttpStatus.SC_OK, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public String getPath() {
        return "/test";
    }
}
