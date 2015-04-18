package net.haviss.havissIoT;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Håvard on 4/18/2015.
 */
public class Config {

    //Properties object
    private Properties properties = new Properties();

    //Constructor
    public Config(String configName) {
        try {
            properties.load(new BufferedReader(new InputStreamReader(Config.class.getClass().getResourceAsStream(configName))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get property value
    public String getProperty(String prop) {
        return properties.getProperty(prop);
    }

    //Set new property
    public void writeProperty(String prop, String value) {
        properties.setProperty(prop, value);
    }
}
