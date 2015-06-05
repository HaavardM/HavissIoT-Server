package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Core.CommandHandler;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.jws.soap.SOAPBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


/**
 *Created by HaavardM on 5/16/2015.
 */
public class CommandVoice implements CommandCallback {
    @Override
    public String run(JSONObject parameters, User user) {

        return null;
    }

    @Override
    public String getName() {
        return "VOICE";
    }
}
