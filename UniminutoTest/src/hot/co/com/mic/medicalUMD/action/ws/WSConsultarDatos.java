package co.com.mic.medicalUMD.action.ws;


import javax.ws.rs.core.Response;

public class WSConsultarDatos implements IWSConsultarDatos
{

    @Override
    public Response consultarPacientesxActivar(String datosPacientesxActivar, String usuarioActivar, String passwordActivar)
    {
        //TODO copiar a la tabla Usuario portal
        //TODO Verificar que exista usuario y clave

        String pacienteactivo="correcta";

        return Response.status(200).entity(pacienteactivo).build();
    }

    @Override
    public Response consultarRespuestaMovil(String datosRespuestaMovil, String usuarioWS, String passwordWS, String codUnificadoPaciente)
    {
        //TODO copiar a la tabla Historial respuestas
        //TODO Verificar que exista usuario y clave

        String respuesta="correcta";

        return Response.status(200).entity(respuesta).build();
    }

    @Override
    public Response consultarEncuesta(String datosEncuesta, String usuarioWS, String passwordWS, String codUnificadoPaciente)
    {
        //TODO copiar a la Encuesta
        //TODO Verificar que exista usuario y clave

        String user="correcta";

        return Response.status(200).entity(user).build();
    }
}
