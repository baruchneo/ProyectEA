package co.com.mic.medicalUMD.action.ws;

import co.com.mic.medicalUMD.modelo.Alerta;
import co.com.mic.medicalUMD.pojo.ConexionPOJO;
import co.com.mic.medicalUMD.pojo.SensorPOJO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/registrar")
public interface IWSRegistrarDatos
{
    @POST
    @Path("/sensores")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response registrarSensor(SensorPOJO sensorPOJO, ConexionPOJO conexionPOJO);

    @POST
    @Path("/programarSensor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response programarSensor();
}
