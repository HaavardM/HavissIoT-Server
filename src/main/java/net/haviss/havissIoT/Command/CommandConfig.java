package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;

/**
 * Created by H�vard on 6/1/2015.
 */
public class CommandConfig implements CommandCallback {
    private boolean isOP;
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        isOP = user != null && user.isOP();
        String intent;
        try {
            if (parameters.has("intent")) {
                intent = parameters.get("intent").getAsString().toUpperCase();
            } else {
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }

            if(intent.compareTo("GET") == 0) {
                return getConfig();
            }
            return Integer.toString(HttpStatus.SC_NOT_FOUND);

        } catch (ClassCastException e) {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
    }

    private String getConfig() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("brokerAddress", Config.brokerAddress);
        jsonObject.addProperty("brokerPort", Config.brokerPort);
        jsonObject.addProperty("qos", Config.qos);
        jsonObject.addProperty("clientID", Config.clientID);
        jsonObject.addProperty("cmdTopic", Config.cmdTopic);
        jsonObject.addProperty("statusTopic", Config.statusTopic);
        jsonObject.addProperty("databaseAddress", Config.databaseAddress);
        jsonObject.addProperty("databasePort", Config.databasePort);
        jsonObject.addProperty("database", Config.database);
        jsonObject.addProperty("sensorInfoCollection", Config.sensorInfoCollection);
        jsonObject.addProperty("numbOfClients", Config.numbOfClients);
        jsonObject.addProperty("keepAlive", Config.keepAlive);
        jsonObject.addProperty("readTimeout", Config.readTimeout);
        jsonObject.addProperty("refreshSensorTime", Config.refreshSensorTime);
        jsonObject.addProperty("offlineMode", Config.offlineMode);
        jsonObject.addProperty("debugMode", Config.debugMode);
        return  jsonObject.toString();
    }

    @Override
    public String getName() {
        return "CONFIG";
    }

    @Override
    public boolean requireArgs() {
        return true;
    }
}
