package net.haviss.havissIoT.Actuators;

/**
 * Created by Håvard on 8/28/2015.
 */
public interface ActuatorCallback<ParamType> {

    //Runs actuator specific code
    void run(ParamType parameter);

    //Returns actuator topic
    String getTopic();

    //Returns actuator name
    String getName();
}
