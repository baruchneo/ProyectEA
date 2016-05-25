package co.com.mic.medicalUMD.action.ws;


import co.com.mic.medicalUMD.pojo.*;

import javax.ws.rs.core.Response;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

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
    public Response consultarEncuesta(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la Encuesta
        //TODO Verificar que exista usuario y clave

        EncuestaPOJO encuestaPOJO = new EncuestaPOJO();
        encuestaPOJO.setFechaRadicado("2016-05-25 15:03");
        encuestaPOJO.setEncuestaVista(1);
        encuestaPOJO.setPreguntas("[{1:'Regular'},{2:'Casi siempre'}, {3:'Positivo'}]");

        return Response.status(200).entity(encuestaPOJO).build();
    }



    @Override
    public Response consultarSensorPorPaciente(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la SensorPorProgramar
        List<SensorPOJO> sensorPOJOList = new ArrayList<SensorPOJO>();

        SensorPOJO sensorPOJO = new SensorPOJO();
        sensorPOJO.setCantidadDias(2);
        sensorPOJO.setDescripcionAlerta("Alarma de presion alta");
        //sensorPOJO.setFechaMuestra("2016-05-24 14:36");
        sensorPOJO.setLimiteMaximo(23.2);
        sensorPOJO.setLimiteMinimo(1.0);
        sensorPOJO.setNombreSensor("Presion arterial");
        sensorPOJO.setPeriodicidad(3);

        sensorPOJOList.add(sensorPOJO);
        SensorPOJO sensorPOJO1 = new SensorPOJO();
        sensorPOJO1.setCantidadDias(4);
        sensorPOJO1.setDescripcionAlerta("Alarma de temperatura");
        //sensorPOJO.setFechaMuestra("2016-05-24 14:36");
        sensorPOJO1.setLimiteMaximo(40.52);
        sensorPOJO1.setLimiteMinimo(30.12);
        sensorPOJO1.setNombreSensor("Temperatura");
        sensorPOJO1.setPeriodicidad(10);
        sensorPOJOList.add(sensorPOJO1);

        SensorPorProgramarPOJO sensorPorProgramarPOJO= new SensorPorProgramarPOJO(sensorPOJOList, 1);

        return Response.status(200).entity(sensorPorProgramarPOJO).build();
    }



    @Override
    public Response consultarMedicamentoPorPaciente(ConexionPOJO conexionPOJO)
    {
        //TODO copiar a la Medicamento por programar

        List<MedicamentoPOJO> medicamentoPOJOList = new ArrayList<MedicamentoPOJO>();

        MedicamentoPOJO medicamentoPOJO = new MedicamentoPOJO();
        medicamentoPOJO.setCantidadRecetada(3.1);
        medicamentoPOJO.setCantidadTotal(20.0);
        medicamentoPOJO.setFechaInicio("2016-05-28 12:25");
        medicamentoPOJO.setFechafinal("2016-08-10 18:25");
        medicamentoPOJO.setNombreMedicamento("Ibuprofeno");
        medicamentoPOJO.setPeriodicidadToma(8);

        MedicamentoPOJO medicamentoPOJO1 = new MedicamentoPOJO();
        medicamentoPOJO1.setCantidadRecetada(2.0);
        medicamentoPOJO1.setCantidadTotal(12.0);
        medicamentoPOJO1.setFechaInicio("2016-05-25 08:03");
        medicamentoPOJO1.setFechafinal("2016-05-26 22:10");
        medicamentoPOJO1.setNombreMedicamento("Acetaminofen");
        medicamentoPOJO1.setPeriodicidadToma(8);

        medicamentoPOJOList.add(medicamentoPOJO);
        medicamentoPOJOList.add(medicamentoPOJO1);

        MedicamentoPorProgramarPOJO medicamentoPorProgramarPOJO= new MedicamentoPorProgramarPOJO(medicamentoPOJOList, 1);


        String medicamento="correcta";

        return Response.status(200).entity(medicamentoPorProgramarPOJO).build();
    }

}