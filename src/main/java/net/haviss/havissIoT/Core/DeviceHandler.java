package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Device.DeviceCallback;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by H�vard on 5/11/2015.
 */
public class DeviceHandler {
    /*Objects*/
    private CopyOnWriteArrayList<DeviceCallback> availableDevices = new CopyOnWriteArrayList<>();

    public DeviceHandler() {

    }
}
