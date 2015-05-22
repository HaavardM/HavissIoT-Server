package net.haviss.havissIoT.Command;

import com.google.gson.Gson;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Core.CommandHandler;
import net.haviss.havissIoT.HavissIoT;
import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;


/**
 *Created by Håvard on 5/16/2015.
 */
public class CommandVoice implements CommandCallback {
    @Override
    public String run(String[] parameters) {
        URIBuilder uriBuilder = new URIBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        CommandHandler commandHandler = new CommandHandler();

        if(parameters.length <= 0) {
            return new Gson().toJson("Too few arguments");
        }

        for (String s : parameters) {
            stringBuilder.append(s + " ");
        }

        uriBuilder.setScheme("http")
                .setHost("https://api.wit.ai")
                .setPath("/message")
                .setParameter("v", stringBuilder.toString());

        stringBuilder = new StringBuilder();

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) uriBuilder.build().toURL().openConnection();
            conn.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = input.readLine()) != null) {
                stringBuilder.append(line);
            }
            input.close();
            HavissIoT.printMessage(stringBuilder.toString());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            conn = null;
        }
        return null;
    }

    @Override
    public String getName() {
        return "voice";
    }
}
