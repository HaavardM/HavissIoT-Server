package net.haviss.havissIoT.Exceptions;

import net.haviss.havissIoT.Device.Device;

/**
 * Created by H�vard on 7/13/2015.
 */
public class HavissIoTDeviceException extends Exception {

    private Device device;

    public HavissIoTDeviceException(Device device, String message) {
        super(message);
        this.device = device;
    }

    public Device getDevice() {
        return this.device;
    }

}
