package net.haviss;

/**
 * Created by Håvard on 3/27/2015.
 */
public class messageGenerator {
    //Creates a message containing a temperature value in celsius (float)
    public String tempCString(String clientID, float tempC) {
        String message = "@" + clientID + "-TC" + Float.toString(tempC);
        return message;
    }
    //Creates a message containing a temperature value in celsius (int)
    public String tempCString(String clientID, int tempC) {
        String message = "@" + clientID + "-TC" + Integer.toString(tempC);
        return message;
    }
    //Creates a message containing a temperature value in fahrenheit (float)
    public String tempFString(String clientID, float tempF) {
        String message = "@" + clientID + "-TF" + Float.toString(tempF);
        return message;
    }
    //Creates a message containing a temperature value in fahrenheit (float)
    public String tempFString(String clientID, int tempF) {
        String message = "@" + clientID + "-TF" + Integer.toString(tempF);
        return message;
    }
}
