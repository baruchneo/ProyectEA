package co.com.mic.medicalUMD.action.ws;

import co.com.mic.medicalUMD.modelo.Alerta;
import co.com.mic.medicalUMD.pojo.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/registrar")
public interface IWSRegistrarDatos
{
    @GET
    @Path("/saludo")
    Response saludo();

    @POST
    @Path("/sensores")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response registrarSensor(InsertarSensorPOJO insertarSensorPOJO);

    @POST
    @Path("/programarSensor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response programarSensor(InsertarSensorPOJO insertarSensorPOJO);
    //Response programarSensor(ConexionPOJO conexionPOJO);

    @POST
    @Path("/programarMedicamento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response programarMedicamento(InsertarMedicamentoPOJO insertarMedicamentoPOJO);

    @POST
    @Path("/encuesta")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response registrarEncuesta(InsertarEncuestaPOJO insertarEncuestaPOJO);

    //TODO ws registra historial paciente conversacion

    //TODO ws registrar respuesta medico

    //TODO ws pedir ambulancia

    //TODO ws registrar respuesta ambulancia
}
