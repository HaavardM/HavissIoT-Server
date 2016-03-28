package net.haviss.havissIoT.Device;

import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;

/**
 * Created by Håvard on 09.03.2016.
 */
public interface DeviceCallback {
    public void messageArrived(String topic, String message) throws HavissIoTDeviceException;

    public void messageDelivered(String topic);


}
