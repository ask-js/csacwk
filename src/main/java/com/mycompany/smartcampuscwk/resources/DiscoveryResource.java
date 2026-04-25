/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampuscwk.resources;

/**
 *
 * @author A
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> resources = new HashMap<>();
        
        // API info:
        response.put("api name", "Smart Campus Sensor & Room Management API");
        response.put("version", "v1");
        
        // Contact Info:
        response.put("contact", "w2107727@westminster.ac.uk");
        
        // Resources:
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);
        
        return response;

    }
}
