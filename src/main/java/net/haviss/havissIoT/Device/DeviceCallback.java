package net.haviss.havissIoT.Device;

/**
 * Created by Håvard on 5/11/2015.
 * Callback functions for device code
 */
public interface DeviceCallback {

    //Method to run when
    String run(String[] parameters);

    //Get device name
    String getName();

    //Get device topic
    String getCommandTopic();

    String getStatusTopic();


}
