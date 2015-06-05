package net.haviss.havissIoT.Type;

import java.util.Arrays;

/**
 * Created by Håvard on 5/30/2015.
 */
public class User {

    /*Variables*/
    private volatile String name;
    private volatile char[] password;
    private volatile boolean passwordProtected = false;
    private volatile boolean isOP = false;
    private volatile boolean isProtected = false;

    //Constructor
    public User(String name, char[] psw) {
        this.name = name;
        this.password = new char[psw.length];
        this.password = psw;
        this.passwordProtected = true;
    }

    //Overloaded constructor - without password
    public User(String name) {
        this.name = name;
        password = null;
    }

    //Check if password is correct
    public boolean checkPassword(char[] password) {
        return Arrays.equals(this.password, password);
    }

    //Get user name
    public String getName() {
        return this.name;
    }

    //Check if user is password protected
    public boolean isPasswordProtected() {
        return this.passwordProtected;
    }

    //Set new password
    public void setPassword(char[] password) {
        this.password = new char[password.length];
        this.password = password;
    }

    //Changed value of isOP
    public synchronized void setOP(boolean state) {
        this.isOP = state;
    }

    //Get value of isOP
    public boolean isOP() {
        return isOP;
    }

    //Set protected or not.
    public synchronized void setProtected(boolean state) {
        this.isProtected = state;
    }

    //Get value of isProtected
    public boolean isProtected() {
        return this.isProtected;
    }



}
