package net.haviss.havissIoT.Communication;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.HavissIoT;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Håvard on 5/16/2015.
 */
public class WitCommunication {
    private HttpURLConnection httpClient;
    private String witAddress = Config.witAddress;

    public String sendVoice(String text) throws Exception{
        URL url = new URL(witAddress);
        httpClient = (HttpURLConnection) url.openConnection();
        httpClient.setRequestMethod("GET");
        httpClient.setRequestProperty("q", text);
        return null; //TODO: Get response

    }
}
