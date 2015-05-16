package net.haviss.havissIoT.Core;

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
    private CopyOnWriteArrayList<Class<? extends DeviceCallback>> deviceTypes = new CopyOnWriteArrayList<>();
    private String threadName = "DeviceThread";
    private Thread deviceThread;

    public DeviceHandler() {
        Reflections r = new Reflections("");
        Set<Class<? extends DeviceCallback>> classes = r.getSubTypesOf(DeviceCallback.class);
        for(Class<?extends DeviceCallback> c : classes) {
            deviceTypes.add(c);
        }
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

    public synchronized void newDevice(String type, String commandTopic) {
        for (Class<? extends DeviceCallback> c : deviceTypes) {
            try {
                if(type.compareTo(c.newInstance().getName()) == 0) {
                    availableDevices.add(c.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

}
