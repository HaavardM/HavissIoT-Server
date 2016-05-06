package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Type.Location;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by H�vard on 9/23/2015.
 */
public class RoomHandler {
    private CopyOnWriteArrayList<Location> availableRooms = new CopyOnWriteArrayList<>();

    public Location getRoomByName(String name) {
        for(Location l : availableRooms) {
            if(l.getName().compareTo(name) == 0) {
                return l;
            }
        }
        return null;
    }
}
