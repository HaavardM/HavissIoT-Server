package net.haviss.havissIoT.Type;

/**
 * Created by havar on 10.04.2016.
 */
public enum DeviceType {
    Analog(0), Toggle(1), SensorGrid(2), RGB(3), Light(4), Audio(5), Temperature(6), None(7), Screen(8);

    DeviceType(int value) {
        this.value = value;
    }
    private int value;

    public int getValue() {
        return value;
    }

    public SensorType parseValue(int value) {
        for (SensorType s : SensorType.values()) {
            if(s.getValue() == value)
                return s;
        }
        return null;
    }
}
