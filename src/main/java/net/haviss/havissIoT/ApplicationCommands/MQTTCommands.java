package net.haviss.havissIoT.ApplicationCommands;

import net.haviss.havissIoT.Exceptions.HavissIoTMQTTException;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Tools.Config;

/**
 * Created by Håvard on 18.05.2016.
 */
public class MQTTCommands implements CommandCallback {

    @Override
    public String run() {
        return null;
    }

    @Override
    public String run(String[] parameters) {
        if(parameters.length > 0) {
            switch (parameters[0]) {
                case "connect":
                    Main.client.connect(Config.brokerAddress, Config.brokerPort);
                    return "Connecting to MQTT broker";
                case "disconnect":
                    if(!Main.client.isConnected()) {
                        return "Client not connected";
                    } else {
                        Main.client.disconnect();
                        return "MQTT client disconnected";
                    }
                case "publish":
                    if(!Main.client.isConnected()) {
                        return "Client not connected";
                    } else if(parameters.length == 3) {
                        try {
                            Main.client.publishMessage(parameters[1], parameters[2]);
                            return "Message published";
                        } catch (HavissIoTMQTTException e) {
                            return e.getMessage();
                        }
                    } else {
                        return "publish command need topic and message";
                    }
                default:
                    return parameters[0] + " not found";
            }
        }
        else {
            return "Command need paramater";
        }
    }

    @Override
    public String getCommandName() {
        return "mqtt";
    }
}
