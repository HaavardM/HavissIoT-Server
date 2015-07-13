package net.haviss.havissIoT.Tools;

/**
 * Created by Håvard on 7/13/2015.
 */
public class Topic {
    public String topic = "";

    public Topic(String house, String room, String device) {
        topic += house;
        topic += "/";
        topic += room;
        topic += "/";
        topic += device;
    }
    public String toString() {
        return topic;
    }
}
