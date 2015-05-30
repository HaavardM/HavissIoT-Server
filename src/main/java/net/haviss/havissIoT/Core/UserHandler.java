package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Type.User;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Håvard on 5/30/2015.
 */
public class UserHandler {
    /*Variables*/
    private CopyOnWriteArrayList<User> availableUsers = new CopyOnWriteArrayList<>();

    public UserHandler() {

    }

    public boolean addUser(String name, char[] password) {
        for(User s : availableUsers) {
            if(s.getName().compareTo(name) == 0) {
                return false;
            }
        }
        availableUsers.add(new User(name, password));
        return true;
    }

    //Overloaded function for use without a password
    public boolean addUser(String name) {
        //Check if username is already used
        for(User s: availableUsers) {
            if(s.getName().compareTo(name) == 0) {
                return false;
            }
        }
        availableUsers.add(new User(name));
        return true;
    }
}
