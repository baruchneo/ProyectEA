package co.com.mic.medicalUMD.action.ws;

import co.com.mic.medicalUMD.modelo.Alerta;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/registrar")
public interface IWSRegistrarDatos
{
    @POST
    @Path("/sensores/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarSensor(Alerta alerta, String usuario, String password);
}
