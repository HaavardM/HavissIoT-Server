package net.haviss.havissIoT.Type;

/**
 * Created by Håvard on 5/30/2015.
 */
public class User {

    /*Variables*/
    private String name;
    private char[] password;
    private boolean passwordProtected = false;

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
        return this.password.equals(password);
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



}
