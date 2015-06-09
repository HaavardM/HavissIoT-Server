package net.haviss.havissIoT.External;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.haviss.havissIoT.HavissIoT;

/**
 * Created by Håvard on 6/7/2015.
 * Gets the location of a public IP address
 */
public class IPLocation {
    public JsonObject findLocation(String IP) {
        try {
            //Return body from external API
            return new JsonParser()
                    .parse(Unirest.get("http://ip-api.com/json")
                            .asString()
                            .getBody())
                    .getAsJsonObject();
        } catch (UnirestException e) {
            HavissIoT.printMessage(e.getMessage());
            //Return null if request error
            return null;
        }
    }
}
