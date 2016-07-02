package net.haviss.havissIoT.Type;

import sun.management.Sensor;

/**
 * Created by havar on 10.04.2016.
 */
public enum SensorType {
    Temperature(0), Pressure(1), None(2), Force(3), Weight(4), Status(5), Light(6), Movement(7);

    SensorType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return this.value;
    }

    public SensorType parseValue(int value) {
        for (SensorType s : SensorType.values()) {
            if(s.getValue() == value)
                return s;
        }
        return null;
    }
}
