package net.haviss.havissIoT.Communication.APIHandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.haviss.havissIoT.HavissIoT;

import java.io.*;

/**
 * Created by Håvard on 07.03.2016.
 */
public class Device implements APIRespond {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
            String s = null;
            StringBuilder requestBuilder = new StringBuilder();
            do {
                s = br.readLine();
                if(s != null)
                    requestBuilder.append(s);
            } while (s != null);
            br.close();
            HavissIoT.printMessage(s.toString());
            OutputStream os = httpExchange.getResponseBody();
            os.write("200".getBytes());
            os.close();
    }

    @Override
    public String getAPIPath() {
        return "/device";
    }
}
