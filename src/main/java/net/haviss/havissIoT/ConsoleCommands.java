package net.haviss.havissIoT;

/**
 * Created by Håvard on 4/18/2015.
 */
public class ConsoleCommands {
    public static void printHelp() {
        System.out.println("Available commands:\n" +
                "Exit\tClose the program\n" +
                "Add\tAdds new topic\n" +
                "Topics\tPrints all subscribed topics\n" +
                "Settings\tPrints current settings\n" +
                "Set {Setting} {value}\n");
    }

}
