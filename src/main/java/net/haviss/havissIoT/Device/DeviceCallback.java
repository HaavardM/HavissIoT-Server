package net.haviss.havissIoT.Device;

/**
 * Created by Håvard on 09.03.2016.
 */
public interface DeviceCallback {
    public void messageArrived(String topic, String message);

    public void messageDelivered(String topic);


}
