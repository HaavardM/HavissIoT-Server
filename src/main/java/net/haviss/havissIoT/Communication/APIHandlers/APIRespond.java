package net.haviss.havissIoT.Communication.APIHandlers;

import com.sun.net.httpserver.HttpHandler;

/**
 * Created by Håvard on 07.03.2016.
 */
public interface APIRespond extends HttpHandler {
    public String getAPIPath();
}
