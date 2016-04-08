package net.haviss.havissIoT.Communication.APIHandlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.ws.api.ha.HaInfo;
import net.haviss.havissIoT.HavissIoT;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Håvard on 07.03.2016.
 */
public class DeviceRespond implements APIRespond {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI path = httpExchange.getRequestURI();
        String query = path.getQuery();
        JsonObject responseObject = new JsonObject();
        String[] pStrings = query.split("&");
        HashMap<String, String> param = new HashMap<>();
        for(String s : pStrings) {
            String[] p = s.split("=");
            if(p.length == 2)
                param.put(p[0], p[1]);
            else
                param.put(p[0], null);
        }
        Set<String> keySet = param.keySet();

    }

    @Override
    public String getAPIPath() {
        return "/device";
    }
}
