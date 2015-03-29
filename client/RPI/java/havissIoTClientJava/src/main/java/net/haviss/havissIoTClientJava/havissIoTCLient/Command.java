package net.haviss.havissIoTClientJava;

/**
 * Created by Håvard on 3/29/2015.
 */
//Callback function for the command. Use this to implement your own function to a command.
interface CommandCallback {
    void performCommand();
}
//Command class containing the neccesary information about the command
public class Command {
    //Variables
    private String commandName = "";
    private String commandFlag = "";
    //Command callback - user defined functions run when
    CommandCallback callback = new CommandCallback() {
        @Override
        public void performCommand() {

        }
    };
    //Class constructor
    public Command (String name, String flag, CommandCallback userCallback) {
        //Assigning values to class variables
        commandName = name;
        commandFlag = flag;
        callback = userCallback;
    }


}
