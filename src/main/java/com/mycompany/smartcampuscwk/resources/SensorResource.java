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
import java.util.ArrayList;
import com.mycompany.smartcampuscwk.exception.LinkedResourceNotFoundException;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {

        if (type == null || type.isEmpty()) {
            return DataStorage.sensors.values();
        }

        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : DataStorage.sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }

    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {

        Sensor sensor = DataStorage.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor with ID " + sensorId + " was not found.")
                    .build();
        }

        return Response.ok(sensor).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor sensor) {

        Room room = DataStorage.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException(
            "Cannot register sensor because room ID " + sensor.getRoomId() + " does not exist.");
        }

        DataStorage.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReading(@PathParam("sensorId") String sensorId){
        return new SensorReadingResource(sensorId);
    }
}