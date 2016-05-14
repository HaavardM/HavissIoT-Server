package net.haviss.havissIoT.ServerCommands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.ServerCommunication.SocketClient;
import net.haviss.havissIoT.Main;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;


/**
 * Created by Haavard on 6/24/2015.
 */
public class CommandUser implements CommandCallback {
    private boolean isOP;
    @Override
    public String run(JsonObject parameters, User user, SocketClient client) {
        isOP = user != null && user.isOP();
        String intent;
        if(parameters.has("intent")) {
            intent = parameters.get("intent").getAsString().toLowerCase();
            if (intent.compareTo("add") == 0) {
                return newUser(parameters);
            } else if(intent.compareTo("remove") == 0) {
                return removeUser(parameters);
            } else if(intent.compareTo("list") == 0) {
                return listUsers(parameters);
            }
        }
        return Integer.toString(HttpStatus.SC_NOT_FOUND);
    }

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public boolean requireArgs() {
        return true;
    }

    private String newUser(JsonObject parameters) {
        if (isOP) {
            String name = null;
            String password = null;
            boolean OP = false;
            boolean isProtected = false;
            User newUser;
            if (parameters.has("name")) {
                name = parameters.get("name").getAsString();
            }
            if (parameters.has("password")) {
                password = parameters.get("password").getAsString();
            }
            if (parameters.has("isOP")) {
                OP = parameters.get("isOP").getAsBoolean();
            }
            if (parameters.has("isProtected")) {
                isProtected = parameters.get("isProtected").getAsBoolean();
            }
            if (name != null && name.length() > 0) {
                if (password != null && password.length() > 0) {
                    newUser = new User(name, password.toCharArray());
                    newUser.setOP(OP);
                } else {
                    newUser = new User(name);
                }
                newUser.setProtected(isProtected);
                if(Main.userHandler.addUser(newUser)) {
                    return Integer.toString(HttpStatus.SC_OK);
                } else {
                    return Integer.toString(HttpStatus.SC_CONFLICT);
                }
            } else {
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }

        } else {
            return Integer.toString(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    private String removeUser(JsonObject parameters) {
        if(isOP) {
            if (parameters.has("name")) {
                String name = parameters.get("name").getAsString();
                if (Main.userHandler.removeUser(name)) {
                    return Integer.toString(HttpStatus.SC_OK);
                } else {
                    return Integer.toString(HttpStatus.SC_NOT_FOUND);
                }
            } else {
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }
        } else {
            return Integer.toString(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    private String listUsers(JsonObject parameters) {
        JsonArray users = new JsonArray();
        for(User u : Main.userHandler.getUsers()) {
            JsonObject user = new JsonObject();
            user.addProperty("name", u.getName());
            user.addProperty("isOP", u.isOP());
            user.addProperty("isProtected", u.isProtected());
            user.addProperty("isPasswordProtected", u.isPasswordProtected());
            users.add(user);
        }
        return users.toString();
    }
}
