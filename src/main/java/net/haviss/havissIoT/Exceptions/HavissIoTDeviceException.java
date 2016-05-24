package net.haviss.havissIoT.Exceptions;

import net.haviss.havissIoT.Device.IoTDevice;

/**
 * Created by H�vard on 7/13/2015.
 */
public class HavissIoTDeviceException extends Exception {

    private IoTDevice device;

    public HavissIoTDeviceException(IoTDevice device, String message) {
        super(message);
        this.device = device;
    }

    public IoTDevice getDevice() {
        return this.device;
    }

}
