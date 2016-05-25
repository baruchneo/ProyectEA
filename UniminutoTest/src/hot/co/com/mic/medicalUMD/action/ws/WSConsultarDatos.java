package co.com.mic.medicalUMD.action.ws;


import co.com.mic.medicalUMD.pojo.ConexionPOJO;
import co.com.mic.medicalUMD.pojo.EncuestaPOJO;
import co.com.mic.medicalUMD.pojo.SensorPOJO;
import co.com.mic.medicalUMD.pojo.SensorPorProgramarPOJO;

import javax.ws.rs.core.Response;

public class WSConsultarDatos implements IWSConsultarDatos
{

    @Override
    public Response consultarPacientesPorActivar(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la tabla Usuario portal
        //TODO Verificar que exista usuario y clave

        String pacienteactivo="correcta";

        return Response.status(200).entity(pacienteactivo).build();
    }

    @Override
    public Response consultarRespuestaMovil(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la tabla Historial respuestas
        //TODO Verificar que exista usuario y clave

        String respuesta="correcta";

        return Response.status(200).entity(respuesta).build();
    }

    @Override
    public EncuestaPOJO consultarEncuesta(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la Encuesta
        //TODO Verificar que exista usuario y clave

        String user="correcta";

        EncuestaPOJO encuestaPOJO = new EncuestaPOJO();

        return new EncuestaPOJO();
    }



    @Override
    public SensorPorProgramarPOJO consultarSensorPorPaciente(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la SensorPorProgramar
        SensorPOJO sensorPOJO = new SensorPOJO();
        sensorPOJO.setCantidadDias(2);
        sensorPOJO.setDescripcionAlerta("Alarma de presion alta");
        //sensorPOJO.setFechaMuestra("2016-05-24 14:36");
        sensorPOJO.setLimiteMaximo(23.2);
        sensorPOJO.setLimiteMinimo(1.0);
        sensorPOJO.setNombreSensor("Presion arterial");
        sensorPOJO.setPeriodicidad(3);

        SensorPorProgramarPOJO sensorPorProgramarPOJO1= new SensorPorProgramarPOJO(sensorPOJO, 1);


        String sensor="correcta";

        return sensorPorProgramarPOJO1;
    }

}