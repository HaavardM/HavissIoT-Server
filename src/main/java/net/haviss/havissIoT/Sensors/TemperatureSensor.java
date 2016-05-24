package net.haviss.havissIoT.Sensors;



import net.haviss.havissIoT.Exceptions.HavissIoTSensorException;
import net.haviss.havissIoT.Type.DataType;
import net.haviss.havissIoT.Type.Location;
import net.haviss.havissIoT.Type.SensorType;
import net.haviss.havissIoT.Type.SensorUnit;
import org.jetbrains.annotations.NotNull;


/**
 * Created by havar on 06.03.2016.
 */
public class TemperatureSensor extends IoTSensor {

    public TemperatureSensor(@NotNull String name, @NotNull String topic, SensorUnit unit) throws HavissIoTSensorException {
        super(name, topic, SensorType.Temperature, DataType.Double, unit);
        if(unit == SensorUnit.Celsius || unit == SensorUnit.Fahrenheit || unit == SensorUnit.Kelvin) {
            throw new HavissIoTSensorException("incorrect data type");
        }
    }

    public TemperatureSensor(@NotNull String name, @NotNull String topic, @NotNull String type, SensorUnit unit, Location room) throws HavissIoTSensorException {
        super(name, topic, SensorType.Temperature, DataType.Double, unit, room);
        if(unit == SensorUnit.Celsius || unit == SensorUnit.Fahrenheit || unit == SensorUnit.Kelvin) {
            throw new HavissIoTSensorException("incorrect data type");
        }
    }

    public double getTempC() throws NumberFormatException {
        return Double.parseDouble(getLastValue());

    }

    public double getTempF() throws NumberFormatException {
        return Double.parseDouble(getLastValue()) * 1.8000 + 32.0;

    }

}
