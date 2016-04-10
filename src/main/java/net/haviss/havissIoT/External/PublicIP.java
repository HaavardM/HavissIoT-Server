package net.haviss.havissIoT.External;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.haviss.havissIoT.Main;

/**
 * Created by HaavardM on 6/7/2015.
 * Gets the Public IP from an public API
 */
public class PublicIP {

    public static String getPublicIP() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.get("https://api.ipify.org").asString();
            return response.getBody();
        } catch (UnirestException e) {
            Main.printMessage(e.getMessage());
            return "";
        }
    }
}
