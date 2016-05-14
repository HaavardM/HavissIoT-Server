package net.haviss.havissIoT.ApplicationCommands;

import net.haviss.havissIoT.Main;

/**
 * Created by havar on 15.05.2016.
 */
public class ReloadCommunicationCommand implements CommandCallback {
    @Override
    public String run() {
        Main.reloadCommunicationObjects();
        return "Reloading and reconnection communication parts of system";
    }

    @Override
    public String run(String[] parameters) {
        return run();
    }

    @Override
    public String getCommandName() {
        return "restartComm";
    }
}
