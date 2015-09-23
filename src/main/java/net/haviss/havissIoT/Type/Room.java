package net.haviss.havissIoT.Type;

import com.sun.istack.internal.NotNull;



/**
 * Created by Håvard on 9/23/2015.
 */
public class Room {
    private String roomName;
    private User owner;

    //Overloaded constructor
    public Room(@NotNull String name, User owner) {
        roomName = name;
        this.owner = owner;
    }

    //Overloaded constructor
    public Room(@NotNull String name) {
        roomName = name;
        this.owner = null;
    }

    //Returns name of the room
    public String getRoomName() {
        return roomName;
    }

    //Return owner of the room
    public User getOwner() {
        return owner;
    }
}
