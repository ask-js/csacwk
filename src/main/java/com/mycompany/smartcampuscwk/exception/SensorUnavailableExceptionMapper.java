/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampuscwk.exception;

/**
 *
 * @author A
 */
import com.mycompany.smartcampuscwk.models.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {

        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                403
        );

        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorMessage)
                .build();
    }
}
