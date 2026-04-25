/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampuscwk.models;

/**
 *
 * @author A
 */
public class SensorReading {

    private String id;
    private long timeStamp;
    private double value;

    public SensorReading() {
    }

    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timeStamp = timestamp;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(long timestamp) {
        this.timeStamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
