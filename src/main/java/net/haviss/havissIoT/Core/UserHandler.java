package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Config;
import net.haviss.havissIoT.Type.User;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by HaavardM on 5/30/2015.
 * Handles the different users of the system
 */
public class UserHandler {
    /*Variables*/
    private CopyOnWriteArrayList<User> availableUsers = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<User> availableOPs = new CopyOnWriteArrayList<>();

    public UserHandler() {
        if(Config.debugMode) {
            addUser(new User("root", "root".toCharArray()));
            getUser("root", "root".toCharArray()).setOP(true);
        }
        addUser(new User("guest"));
        getUser("guest").setProtected(true);
    }

    //Overloaded function for use without a password
    public boolean addUser(User user) {
        //Check if username is already used
        for(User s : availableUsers) {
            if(s.getName().compareTo(user.getName()) == 0) {
                return false;
            }
        }
        availableUsers.add(user);
        if(user.isOP()) {
            availableOPs.add(user);
        }
        return true;
    }

    //Get user - if no password authentication
    public User getUser(String name) {
        for(User u : this.availableUsers) {
            if(name.compareTo(u.getName()) == 0) {
                //Check if password is correct
                if(!u.isPasswordProtected()) {
                    //Password is correct - return user
                    return u;
                }
                else return null;
            }
        }
        //No corresponding user found
        return null;
    }

    //Get user - with password
    public User getUser(String name, char[] password) {
        for(User u : this.availableUsers) {
            if(name.compareTo(u.getName()) == 0) {
                //if user not password protected
                if(!u.isPasswordProtected()) {
                    return u;
                } else {
                    //Check if password is correct
                    if(u.checkPassword(password)) {
                        return u;
                    } else {
                        //Password is incorrect
                        return null;
                    }
                }
            }
        }
        //No user found
        return null;
    }

    public boolean removeUser(String name) {
        for(User u : this.availableUsers) {
            if(u.getName().compareTo(name) == 0) {
                if (!u.isProtected()) {
                    availableUsers.remove(u);
                    if (availableOPs.contains(u)) {
                        availableOPs.remove(u);
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }
    //Get all users
    public CopyOnWriteArrayList<User> getUsers() {
        return this.availableUsers;
    }
}
