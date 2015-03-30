package net.haviss.havissIoTClientJava;

import java.util.Collections;
import java.util.List;
import net.haviss.havissIoTClientJava.havissIoTLibraries.havissIoTCommand;

/**
 * Created by Håvard on 3/30/2015.
 */
public class Commands {
    public havissIoTCommand temperatureC = new havissIoTCommand("Temperature celsius", "-TC");
    public havissIoTCommand temperatureF = new havissIoTCommand("Temperature celsius", "-TF");
    public havissIoTCommand temperatureK = new havissIoTCommand("Temperature kelvin", "-TK");
}
