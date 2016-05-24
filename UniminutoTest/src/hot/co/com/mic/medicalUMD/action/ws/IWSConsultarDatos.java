package co.com.mic.medicalUMD.action.ws;

import co.com.mic.medicalUMD.pojo.ConexionPOJO;
import co.com.mic.medicalUMD.pojo.EncuestaPOJO;
import co.com.mic.medicalUMD.pojo.SensorPorProgramarPOJO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consultar")
public interface IWSConsultarDatos
{
    @POST
    @Path("/pacienteporactivar/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consultarPacientesPorActivar(ConexionPOJO conexionPOJO);
    @POST
    @Path("/respuestamovil/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consultarRespuestaMovil(ConexionPOJO conexionPOJO);
    @POST
    @Path("/encuesta/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public EncuestaPOJO consultarEncuesta(ConexionPOJO conexionPOJO);
    @POST
    @Path("/sensorpaciente/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SensorPorProgramarPOJO consultarSensorPorPaciente(ConexionPOJO conexionPOJO);
}
