package net.haviss.havissIoT.Sensors;

import com.sun.istack.internal.NotNull;
import net.haviss.havissIoT.Type.Room;


/**
 * Created by havar on 06.03.2016.
 */
public class TemperatureSensor extends IoTSensor {



    public TemperatureSensor(@NotNull String name, @NotNull String topic, @NotNull String type) {
        super(name, topic, type);
    }

    public TemperatureSensor(@NotNull String name, @NotNull String topic, @NotNull String type, Room room) {
        super(name, topic, type, room);
    }

    public double getTempC() throws NumberFormatException {
        return Double.parseDouble(getLastValue());
    }

    public double getTempF() throws NumberFormatException {
        return Double.parseDouble(getLastValue()) * 1.8000 + 32.0;

    }

}
