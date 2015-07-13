package net.haviss.havissIoT.Device;

/**
 * Created by Håvard on 5/11/2015.
 * Callback functions for device code
 */
public interface DeviceCallback<P> {

    //Method to run when
    String set(P parameters);

    //Get device name
    String getName();

    //Get device topic
    String getDeviceTopic();

}
