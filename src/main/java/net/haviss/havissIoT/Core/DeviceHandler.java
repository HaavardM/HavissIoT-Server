package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.DeviceCommands.DeviceCallback;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Håvard on 5/11/2015.
 */
public class DeviceHandler {
    /*Objects*/
    private CopyOnWriteArrayList<DeviceCallback> availableDevices = new CopyOnWriteArrayList<>();

    public DeviceHandler() {

    }
}
