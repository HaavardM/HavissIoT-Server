package net.haviss.havissIoT.Core;

import net.haviss.havissIoT.Type.Room;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by H�vard on 9/23/2015.
 */
public class RoomHandler {
    private CopyOnWriteArrayList<Room> availableRooms = new CopyOnWriteArrayList<>();

    public Room getRoomByName(String name) {
        for(Room r : availableRooms) {
            if(r.getName().compareTo(name) == 0) {
                return r;
            }
        }
        return null;
    }
}
