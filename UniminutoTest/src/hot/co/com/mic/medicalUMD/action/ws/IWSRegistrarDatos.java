package co.com.mic.medicalUMD.action.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/registrar")
public interface IWSRegistrarDatos
{
    @POST
    @Path("/sensores/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String registrarSensor(@PathParam("datosSensor") String datosSensor, @PathParam("usuario")  String usuario, @PathParam("password") String password);
}
