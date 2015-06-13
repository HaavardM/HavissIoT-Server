package net.haviss.havissIoT.Core;

import com.google.gson.JsonObject;
import net.haviss.havissIoT.Communication.SocketClient;
import net.haviss.havissIoT.Sensor.IoTSensor;
import net.haviss.havissIoT.Type.Subscription;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Håvard on 6/14/2015.
 */
public class SubscriptionHandler {
    private CopyOnWriteArrayList<Subscription> subscriptions = new CopyOnWriteArrayList<>();
    private Timer timer;
    private int refreshRate;
    private boolean isRunning = false;

    public SubscriptionHandler(int time) {
        this.refreshRate = time;
        this.timer = new Timer(this.refreshRate, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Subscription sub : subscriptions) {
                    JsonObject sensorValues = new JsonObject();
                    JsonObject sensorStatus = new JsonObject();
                    JsonObject response = new JsonObject();
                    for(IoTSensor sensor : sub.getSensors() ) {
                        sensorValues.addProperty(sensor.getName(), sensor.getLastValue());
                        sensorStatus.addProperty(sensor.getName(), sensor.isActive());
                    }
                    response.add("values", sensorValues);
                    response.add("status", sensorStatus);
                    sub.getClient().writeToSocket(response.toString() + "\n");
                }
            }
        });
    }
    //Add subscription
    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
        if(this.subscriptions.size() > 0 && !isRunning) {
            this.start();
        }
    }

    //Remove subscription
    public void removeSubscription(Subscription subscription) {
        this.subscriptions.remove(subscription);
        if(this.subscriptions.size() <= 0) {
            this.stop();
        }
    }

    //Start subscription handler
    public void start() {
        this.timer.start();
        this.isRunning = true;
    }

    public void stop() {
        this.timer.stop();
        this.isRunning = false;

    }

}
