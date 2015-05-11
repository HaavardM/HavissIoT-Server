package net.haviss.havissIoT;

import net.haviss.havissIoT.Device.DeviceCallback;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Håvard on 5/11/2015.
 */
public class DeviceHandler implements Runnable {
    /*Objects*/
    private CopyOnWriteArrayList<DeviceCallback> availableDevices = new CopyOnWriteArrayList<>();
    private String threadName = "DeviceThread";
    private Thread deviceThread;

    public DeviceHandler() {

        //TODO: Create a device loader
        if(deviceThread == null) {
            deviceThread = new Thread(this, threadName);
            deviceThread.start();
        }
    }
    @Override
    public void run() {
        //TODO: Do something useful in thread - handle available devices in an non blocking way.
    }


}
