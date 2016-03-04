package net.haviss.havissIoT.Communication.APIHandlers;

import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Created by Håvard on 04.03.2016.
 */
public interface APIRespond extends HttpHandler {
    public String getPath(); //Get URL path for API respond
}
