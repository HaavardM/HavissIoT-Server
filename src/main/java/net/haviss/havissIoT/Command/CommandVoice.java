package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Core.CommandHandler;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


/**
 *Created by HaavardM on 5/16/2015.
 */
public class CommandVoice implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        URIBuilder uriBuilder = new URIBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        CommandHandler commandHandler = new CommandHandler();

        //If there are no parameters there is no text to analyze
        if(parameters.length <= 0) {
            return new Gson().toJson("Too few arguments");
        }

        //Rebuild strings as commandhandler separates whitespaces
        for (String s : parameters) {
            stringBuilder.append(s + " ");
        }

        //Build an URI
        uriBuilder.setScheme("http")
                .setHost("https://api.wit.ai")
                .setPath("/message")
                .setParameter("v", "20141022")
                .setParameter("q", stringBuilder.toString());

        //Reset stringbuilder - new purpose
        stringBuilder = new StringBuilder();

        try {
            HttpClient conn = HttpClientBuilder.create().build();
            HttpRequest request = new HttpGet(uriBuilder.build());
            request.addHeader("Authorization", "Bearer " + Config.witToken);
            HttpResponse response = conn.execute((HttpUriRequest) request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return "voice";
    }
}
