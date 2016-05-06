package net.haviss.havissIoT.Type;

import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 21.02.2016.
 */
public class Location {
    private String name;

    public Location(String name) {
        this.name = name;
    }




    public String getName() {
        return this.name;
    }
}
