/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampuscwk.resources;

/**
 *
 * @author A
 */
import com.mycompany.smartcampuscwk.models.Room;
import com.mycompany.smartcampuscwk.models.Sensor;
import com.mycompany.smartcampuscwk.data.DataStorage;
import com.mycompany.smartcampuscwk.models.SensorReading;
import com.mycompany.smartcampuscwk.exception.SensorUnavailableException;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        Sensor sensor = DataStorage.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor with ID " + sensorId + " was not found")
                    .build();
        }

        List<SensorReading> sensorReadings = DataStorage.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
            DataStorage.readings.put(sensorId, sensorReadings);
        }

        return Response.ok(sensorReadings).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStorage.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor with ID " + sensorId + " was not found")
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is physically disconnected and cannot accept new readings.");
        }

        List<SensorReading> sensorReadings = DataStorage.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
            DataStorage.readings.put(sensorId, sensorReadings);
        }

        sensorReadings.add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }

}
