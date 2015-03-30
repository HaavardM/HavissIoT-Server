package net.haviss.havissIoTClientJava.havissIoTLibraries;

/**
 * Created by Håvard on 3/29/2015.
 * This library is for creating commands for the client.
 */
//Callback function for the command. Use this to implement your own function to a command.
interface CommandCallback {
    void performCommand();
}
//Command class containing the neccesary information about the command
public class havissIoTCommand {
    //Variables
    private String commandName = "";
    private String commandFlag = "";
    //Command callback - user defined functions - runs when read from the server
    CommandCallback callback = new CommandCallback() {
        @Override
        public void performCommand() {

        }
    };
    //Class constructor
    public havissIoTCommand (String name, String flag, CommandCallback userCallback) {
        //Assigning values to class variables
        commandName = name;
        commandFlag = flag;
        callback = userCallback;
    }
    //Class constructor if the user doesent need any function
    public havissIoTCommand (String name, String flag) {
        //Assigning values to class variables
        commandName = name;
        commandFlag = flag;
    }
    //Gets name of the command
    public String getCommandName() {
        return commandName;
    }
    //gets the flag for the command
    public String getCommandFlag() {
        return commandFlag;
    }


}
