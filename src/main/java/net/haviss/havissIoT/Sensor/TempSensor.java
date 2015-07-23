package net.haviss.havissIoT.Sensor;

/**
 * Created by Håvard on 7/23/2015.
 */
public class TempSensor extends IoTSensor<Float> {

    private boolean fahrenheit = false;

    //Constructor
    public TempSensor(String name, String topic, boolean toStore, long timeout, boolean fahrenheit) {
        super(name, topic, toStore, timeout);
        this.fahrenheit = fahrenheit;
    }

    //Constructor - overloaded without timeout
    public TempSensor(String name, String topic, boolean toStore) {
        super(name, topic, toStore);
    }

    //Check if it is fahrenheit
    public boolean isFahrenheit() {
        return this.fahrenheit;
    }

    //Change unit - fahrenheit = true, celsius = false
    public void setFahrenheit(boolean state) {
        this.fahrenheit = state;
    }
}
