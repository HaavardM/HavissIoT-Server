package net.haviss.havissIoT.Command;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

/**
 * Created by H�vard on 6/1/2015.
 */
public class CommandConfig implements CommandCallback {
    private boolean isOP;
    @Override
    public String run(JsonObject parameters, User user) {
        isOP = user != null && user.isOP();
        String intent;
        try {
            if (parameters.has("intent")) {
                intent = parameters.get("intent").getAsString();
            } else {
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }

            if(intent.compareTo("NEW_USER") == 0) {
                return this.newUser(parameters);
            } else if(intent.compareTo("REMOVE_USER") == 0) {
                return this.removeUser(parameters);
            }
            return Integer.toString(HttpStatus.SC_NOT_FOUND);

        } catch (ClassCastException e) {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
    }

    private String newUser(JsonObject parameters) {
        if (isOP) {
            User newUser;
            String name;
            char[] password;
            boolean userOP;
            boolean userProtected;
            if (parameters.has("name")) {
                name = parameters.get("name").getAsString();
            } else {
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }
            if (parameters.has("password")) {
                password = parameters.get("password").getAsString().toCharArray();
                newUser = new User(name, password);

            } else {
                newUser = new User(name);
            }
            if (parameters.has("isOP")) {
                userOP = parameters.get("isOP").getAsBoolean();
                newUser.setOP(userOP);
            }
            if(parameters.has("protected")) {
                userProtected = parameters.get("protected").getAsBoolean();
                newUser.setProtected(userProtected);
            }
            if (HavissIoT.userHandler.addUser(newUser)) {
                return Integer.toString(HttpStatus.SC_OK);
            } else {
                return Integer.toString(HttpStatus.SC_CONFLICT);
            }

        } else {
            return Integer.toString(HttpStatus.SC_UNAUTHORIZED);

        }
    }

    private String removeUser(JsonObject parameters) {
        String name;
        if(parameters.has("name")) {
            name = parameters.get("name").getAsString();
            if(HavissIoT.userHandler.removeUser(name)) {
                return Integer.toString(HttpStatus.SC_OK);
            } else {
                return Integer.toString(HttpStatus.SC_CONFLICT);
            }

        } else {
            return Integer.toString(HttpStatus.SC_BAD_REQUEST);
        }
    }

    @Override
    public String getName() {
        return "CONFIG";
    }
}
