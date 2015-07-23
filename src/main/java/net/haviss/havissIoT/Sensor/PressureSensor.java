package net.haviss.havissIoT.Sensor;

/**
 * Created by Håvard on 7/23/2015.
 */
public class PressureSensor extends IoTSensor<Float> {

    //Constructor
    public PressureSensor(String name, String topic, boolean toStore) {
        super(name, topic, toStore);
    }
    //Constructor overloaded
    public PressureSensor(String name, String topic, boolean toStore, long timeout) {
        super(name, topic, toStore, timeout);
    }
}
