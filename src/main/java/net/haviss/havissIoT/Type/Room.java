package net.haviss.havissIoT.Type;

import net.haviss.havissIoT.Exceptions.HavissIoTDeviceException;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by havar on 21.02.2016.
 */
public class Room {
    private String name;

    public Room(String name) {
        this.name = name;
    }




    public String getName() {
        return this.name;
    }
}
