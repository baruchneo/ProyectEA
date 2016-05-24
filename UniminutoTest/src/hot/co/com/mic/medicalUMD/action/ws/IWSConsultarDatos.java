package co.com.mic.medicalUMD.action.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consultar")
public interface IWSConsultarDatos
{
    @POST
    @Path("/consultar/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consultarPacientesxActivar(String datosPacientesxActivar, String usuarioActivar, String passwordActivar);
    public Response consultarRespuestaMovil(String datosRespuestaMovil, String usuarioWS, String passwordWS, String codUnificadoPaciente);
    public Response consultarEncuesta(String datosEncuesta, String usuariosWS, String passwordWS, String codUnificadoPaciente);
}
