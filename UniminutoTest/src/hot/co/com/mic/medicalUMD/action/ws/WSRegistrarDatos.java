package co.com.mic.medicalUMD.action.ws;


import co.com.mic.medicalUMD.action.user.AlertaHome;
import co.com.mic.medicalUMD.action.user.HistorialHome;
import co.com.mic.medicalUMD.action.user.SensorPorProgramarHome;
import co.com.mic.medicalUMD.action.admin.TipoAlertaList;
import co.com.mic.medicalUMD.modelo.Historial;
import co.com.mic.medicalUMD.modelo.ResponsableAlertaEnum;
import co.com.mic.medicalUMD.modelo.TipoAlerta;
import co.com.mic.medicalUMD.pojo.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import java.util.Date;

@Stateless
@Name("wSRegistrarDatos")
public class WSRegistrarDatos implements IWSRegistrarDatos
{
    @In(create = true)
    SensorPorProgramarHome sensorPorProgramarHome;

    @In
    AlertaHome alertaHome;

    @In
    HistorialHome historialHome;

    @In
    TipoAlertaList tipoAlertaList;

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
            // 1 verificar conexion y fechas

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
            sensorPorProgramarHome.persist();

            // 3 insertar en tabla historial

            // 3.1 obtener tipoalerta
            tipoAlertaList.getTipoAlerta().setNombreAlerta(insertarSensorPOJO.getSensorPOJO().getNombreSensor());
            TipoAlerta tipoAlerta = new TipoAlerta();

            if(tipoAlertaList.getResultList().size() <= 0)
            {
                return Response.status(500).entity("El tipo de sensor indicado no existe.. Por favor intEntelo de nuevo").build();
            }

            tipoAlerta = tipoAlertaList.getResultList().get(0);

            // 3.2 insertar en alerta
            alertaHome.getInstance();
            alertaHome.getInstance().setActivarAlarma(false); //la alarma la activa la Unidad Base
            alertaHome.getInstance().setPeriodicidad(insertarSensorPOJO.getSensorPOJO().getPeriodicidad());
            alertaHome.getInstance().setNumeroRespiraciones(insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones());
            alertaHome.getInstance().setLimiteMinimo(insertarSensorPOJO.getSensorPOJO().getLimiteMinimo());
            alertaHome.getInstance().setCantidadDias(insertarSensorPOJO.getSensorPOJO().getCantidadDias());
            alertaHome.getInstance().setDescripcionAlerta(insertarSensorPOJO.getSensorPOJO().getDescripcionAlerta());
            alertaHome.getInstance().setLimiteMaximo(insertarSensorPOJO.getSensorPOJO().getLimiteMaximo());
            alertaHome.getInstance().setTipoAlerta(tipoAlerta);
            alertaHome.persist();

            // 3.2 insertar historial
            String mensajeAlerta = "Programación Sensor " + insertarSensorPOJO.getSensorPOJO().getNombreSensor() +  " con las siguientes condiciones: ";
            mensajeAlerta += "lim max: [" + insertarSensorPOJO.getSensorPOJO().getLimiteMaximo() + "] ";
            mensajeAlerta += "lim min: [" + insertarSensorPOJO.getSensorPOJO().getLimiteMinimo() + "] ";
            mensajeAlerta += "periodicidad: [" + insertarSensorPOJO.getSensorPOJO().getPeriodicidad() + "]";
            mensajeAlerta += "cantidad de dias programados [" + insertarSensorPOJO.getSensorPOJO().getCantidadDias() + "]";
            if(!insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones().equals(-1))
            {
                mensajeAlerta += "numeor de respiraciones [" + insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones() + "]";
            }

            historialHome.getInstance();
            historialHome.getInstance().setAlerta(alertaHome.getInstance());
            historialHome.getInstance().setAlertaClinica(false);
            historialHome.getInstance().setCodigoCentroAtencion("CNTROATNC001");
            historialHome.getInstance().setCodigoDoctor(insertarSensorPOJO.getConexionPOJO().getIdentificacionDoctor());
            historialHome.getInstance().setCodigoPaciente(insertarSensorPOJO.getConexionPOJO().getIdentificacionPaciente());
            historialHome.getInstance().setDescripcionHistorial(mensajeAlerta);
            historialHome.getInstance().setFechaCreacion(new Date());
            historialHome.getInstance().setTriage(5);
            historialHome.getInstance().setUnidadGeneraAlerta(ResponsableAlertaEnum.ALERTA_POR_CENTRO_ATENCION.getName());
            historialHome.persist();

            String result = "objeto recibido con exito, id generado " + historialHome.getInstance().getId();
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
