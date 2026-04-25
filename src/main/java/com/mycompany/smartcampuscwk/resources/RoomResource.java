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
import com.mycompany.smartcampuscwk.data.DataStorage;
import com.mycompany.smartcampuscwk.exception.RoomNotEmptyException;
import com.mycompany.smartcampuscwk.exception.LinkedResourceNotFoundException;
import java.util.Collection;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Room> getAllRooms() {
        return DataStorage.rooms.values();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {

        Room room = DataStorage.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room with ID " + roomId + " was not found.")
                    .build();
        }

        return Response.ok(room).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

        DataStorage.rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {

        Room room = DataStorage.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
                throw new RoomNotEmptyException("Room cannot be deleted because"
                        + " it still has sensors assigned.");
        }

        DataStorage.rooms.remove(roomId);

        return Response.ok("Room deleted successfully").build();
    }
}
