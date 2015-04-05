package net.haviss.havissIoTClientJava;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Håvard on 4/5/2015.
 */
public class configHandler {
    private Properties properties = new Properties();
    private String propertiesFileName = "";
    private InputStream istream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
    public configHandler(String configName) {
        propertiesFileName = configName;
        getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        if(istream != null) {
            try {
                properties.load(istream);
            }
            catch (IOException e) {
                //TODO: Handle exception
            }
        }


    }
    public String loadPropValue(String prop) {
        return properties.getProperty(prop);
    }
    public void changePropValue(String prop, String value) {
        properties.setProperty(prop, value);
    }
}
