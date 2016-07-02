package net.haviss.havissIoT.Type;

/**
 * Created by havar on 10.04.2016.
 */
public enum SensorUnit {
    Celsius(0), Fahrenheit(1), PSI(2), Bar(3), Newton(4), Kilogram(5), None(6), Boolean(7), Degree(8), Kelvin(9);

    SensorUnit(int value) {
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
