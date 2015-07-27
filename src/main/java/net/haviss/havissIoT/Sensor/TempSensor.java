package net.haviss.havissIoT.Sensor;

/**
 * Created by Håvard on 7/23/2015.
 */
public class TempSensor extends IoTSensor<Float> {

    //Constructor
    public TempSensor(String name, String topic, boolean toStore, long timeout) {
        super(name, topic, toStore, timeout);
    }

    //Constructor - overloaded without timeout
    public TempSensor(String name, String topic, boolean toStore) {
        super(name, topic, toStore);
    }

    //Return sensor type name
    @Override
    public String getType() {
        return "TEMPERATURE";
    }


}
