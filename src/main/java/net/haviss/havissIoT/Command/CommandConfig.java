package net.haviss.havissIoT.Command;

import net.haviss.havissIoT.HavissIoT;
import net.haviss.havissIoT.Type.User;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

/**
 * Created by Håvard on 6/1/2015.
 */
public class CommandConfig implements CommandCallback {
    private boolean isOP;
    @Override
    public String run(JSONObject parameters, User user) {
        isOP = user != null && user.isOP();
        String intent;
        try {
            if (parameters.containsKey("intent")) {
                intent = (String) parameters.get("intent");
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

    private String newUser(JSONObject parameters) {
        if (isOP) {
            User newUser;
            String name;
            char[] password;
            boolean userOP;
            boolean userProtected;
            if (parameters.containsKey("name")) {
                name = (String) parameters.get("name");
            } else {
                return Integer.toString(HttpStatus.SC_BAD_REQUEST);
            }
            if (parameters.containsKey("password")) {
                password = (char[]) parameters.get("password");
                newUser = new User(name, password);

            } else {
                newUser = new User(name);
            }
            if (parameters.containsKey("isOP")) {
                userOP = (boolean) parameters.get("isOP");
                newUser.setOP(userOP);
            }
            if(parameters.containsKey("protected")) {
                userProtected = (boolean) parameters.get("protected");
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

    private String removeUser(JSONObject parameters) {
        String name;
        if(parameters.containsKey("name")) {
            name = (String) parameters.get("name");
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
