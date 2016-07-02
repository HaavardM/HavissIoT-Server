package net.haviss.havissIoT.Type;

/**
 * Created by havar on 10.04.2016.
 */
public enum DataType {
    Integer(0), Double(1), Float(2), Boolean(3), String(4);

    DataType(int value) {
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

