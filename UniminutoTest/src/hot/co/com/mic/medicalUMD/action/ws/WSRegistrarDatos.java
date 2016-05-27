package co.com.mic.medicalUMD.action.ws;


import co.com.mic.medicalUMD.action.user.AlertaHome;
import co.com.mic.medicalUMD.action.user.SensorPorProgramarHome;
import co.com.mic.medicalUMD.action.admin.TipoAlertaList;
import co.com.mic.medicalUMD.modelo.Historial;
import co.com.mic.medicalUMD.pojo.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

@Stateless
@Name("wSRegistrarDatos")
public class WSRegistrarDatos implements IWSRegistrarDatos
{
    @In(create = true)
    SensorPorProgramarHome sensorPorProgramarHome;

    @In
    TipoAlertaList tipoAlertaList;

    @In
    AlertaHome alertaHome;

    @In
    Historial historial;

    @Override
    public Response saludo() {
        return Response.status(200).entity("Correcto").build();
    }

    @Override
    public Response registrarSensor(InsertarSensorPOJO insertarSensorPOJO)
    {
        //TODO copiar a la tabla Historial
        //TODO Verificar que exista usuario y clave
        // Solo Jhon envia esta data
        //se espera json:
        //sensor:{
        //      'identificadorPaciente':'PAC1',
        //      'nombreSensor':'temperatura',
        //      'fechaMuestra':'2016-05-23 15:30',
        //      'valorSensor':37.3,
        //      'cantidadRespiraciones':-1, //-1 no aplica, de lo contrario se miden
        //      'activarAlarma':0  //0=no;1=si
        // }
        String respuesta = "{'resultado':'sensor registrado satisfactoriamente para el paciente PAC1'}";
        return Response.status(200).entity(respuesta).build();
    }

    @Override
    public Response programarSensor(InsertarSensorPOJO insertarSensorPOJO) {

        try {
            // 1 verificar conexion

            // 2 insertar en tabla sensor por progrmar
            sensorPorProgramarHome.getInstance();
            sensorPorProgramarHome.getInstance().setCantidadDias(insertarSensorPOJO.getSensorPOJO().getCantidadDias());
            sensorPorProgramarHome.getInstance().setCodigoDoctor(insertarSensorPOJO.getConexionPOJO().getIdentificacionDoctor());
            sensorPorProgramarHome.getInstance().setCodigoPaciente(insertarSensorPOJO.getConexionPOJO().getIdentificacionPaciente());
            sensorPorProgramarHome.getInstance().setDescripcionSensor(insertarSensorPOJO.getSensorPOJO().getDescripcionAlerta());
            sensorPorProgramarHome.getInstance().setLimiteMaximo(insertarSensorPOJO.getSensorPOJO().getLimiteMaximo());
            sensorPorProgramarHome.getInstance().setLimiteMinimo(insertarSensorPOJO.getSensorPOJO().getLimiteMinimo());
            sensorPorProgramarHome.getInstance().setNombreSensor(insertarSensorPOJO.getSensorPOJO().getNombreSensor());
            sensorPorProgramarHome.getInstance().setNumeroRespiraciones(insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones());
            sensorPorProgramarHome.getInstance().setPeriodicidad(insertarSensorPOJO.getSensorPOJO().getPeriodicidad());
            sensorPorProgramarHome.create();

            // 3 insertar en tabla historial

            // 3.1 insertar en alerta
            alertaHome.getInstance();
            alertaHome.getInstance().setActivarAlarma(false); //la alarma la activa la Unidad BAse

            // 3.2 obtener tipoalerta

            // 3.2 insertar historial


            String result = "objeto recibido con exito";
            System.out.println("funciono HP");

            return Response.status(200).entity(result).build();
        }
        catch (Exception e)
        {
            return Response.status(500).entity("Se presentaron errores al progrmar el sensor: " + e.getMessage() + "Por favor intEntelo de nuevo").build();
        }
    }

    @Override
    public Response programarMedicamento(InsertarMedicamentoPOJO insertarMedicamentoPOJO) {
        String result = "objeto recibido con exito";

        return Response.status(200).entity(result).build();

    }

    @Override
    public Response registrarEncuesta(InsertarEncuestaPOJO insertarEncuestaPOJO) {

        String result = "objeto recibido con exito";

        return Response.status(200).entity(result).build();
    }




}
