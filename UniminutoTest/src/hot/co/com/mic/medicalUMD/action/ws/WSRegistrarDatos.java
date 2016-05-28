package co.com.mic.medicalUMD.action.ws;

import co.com.mic.medicalUMD.action.admin.TipoAlertaList;
import co.com.mic.medicalUMD.action.user.AlertaHome;
import co.com.mic.medicalUMD.action.user.EncuestaHome;
import co.com.mic.medicalUMD.action.user.HistorialHome;
import co.com.mic.medicalUMD.action.user.SensorPorProgramarHome;
import co.com.mic.medicalUMD.modelo.ResponsableAlertaEnum;
import co.com.mic.medicalUMD.modelo.TipoAlerta;
import co.com.mic.medicalUMD.pojo.InsertarEncuestaPOJO;
import co.com.mic.medicalUMD.pojo.InsertarMedicamentoPOJO;
import co.com.mic.medicalUMD.pojo.InsertarSensorPOJO;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@Name("wSRegistrarDatos")
public class WSRegistrarDatos implements IWSRegistrarDatos {
    //------------------------------------------------- Constantes ---------------------------------------------------//
    private static final String _NOMBRE_CENTRO_ATENCION = "CNTROATNC001";

    //---------------------------------------- Objetos ejb injectados ------------------------------------------------//

    @In(create = true)
    SensorPorProgramarHome sensorPorProgramarHome;

    @In(create = true)
    AlertaHome alertaHome;

    @In(create = true)
    HistorialHome historialHome;

    @In(create = true)
    TipoAlertaList tipoAlertaList;

    @In(create = true)
    EncuestaHome encuestaHome;

    //------------------------------------------ Bussiness Methods ---------------------------------------------------//

    /**
     * Implementacion servicio web de pruebas
     *
     * @return response string con la palabra correcto
     */
    @Override
    public Response saludo() {
        return Response.status(200).entity("Correcto").build();
    }

    /**
     * Implementacion servicio web para el registro de los datos calculados por el sensor
     *
     * @param insertarSensorPOJO objeto complejo con datos de conexion (ConexionPOJO) y la medicion generada
     *                           por la unidad base del sensor respectivo (SensorPOJO)
     * @return id del registro correcto del sensor en el sistema de integracion, de lo contrario el error respectivo en estado 500
     */
    @Override
    public Response registrarSensor(InsertarSensorPOJO insertarSensorPOJO) {
        try {
            // 1 verificar conexion y fechas
            String usuario = insertarSensorPOJO.getConexionPOJO().getUsuario();
            String password = insertarSensorPOJO.getConexionPOJO().getPassword();

            if (usuario.trim().equals("") || password.trim().equals("")) {
                return Response.status(500).entity("El usuario o la clave estAn vacIos... Por favor intEntelo de nuevo").build();
            }

            //1.2 validacion campo fecha
            String dateRecived = insertarSensorPOJO.getSensorPOJO().getFechaMuestra();
            if (validarFormatoFecha(dateRecived)) {
                return Response.status(500).entity("El campo fecha no tiene el formato correcto... Por favor intEntelo de nuevo").build();
            }

            // 2 Insertar en historial

            // 2.1 obtener tipoalerta
            tipoAlertaList.getTipoAlerta().setNombreAlerta(insertarSensorPOJO.getSensorPOJO().getNombreSensor());
            TipoAlerta tipoAlerta;

            if (tipoAlertaList.getResultList().size() <= 0) {
                return Response.status(500).entity("El tipo de sensor indicado no existe.. Por favor intEntelo de nuevo").build();
            }

            tipoAlerta = tipoAlertaList.getResultList().get(0);

            // 2.2 Insertar en tabla alarma

            alertaHome.getInstance();
            alertaHome.getInstance().setTipoAlerta(tipoAlerta);
            alertaHome.getInstance().setDescripcionAlerta("valor registrado por sensor de: " + tipoAlerta.getNombreAlerta());
            alertaHome.getInstance().setCantidadDias(insertarSensorPOJO.getSensorPOJO().getCantidadDias());
            alertaHome.getInstance().setPeriodicidad(insertarSensorPOJO.getSensorPOJO().getPeriodicidad());
            alertaHome.getInstance().setLimiteMaximo(insertarSensorPOJO.getSensorPOJO().getLimiteMaximo());
            alertaHome.getInstance().setLimiteMinimo(insertarSensorPOJO.getSensorPOJO().getLimiteMinimo());
            alertaHome.getInstance().setNumeroRespiraciones(insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones());
            alertaHome.getInstance().setActivarAlarma(insertarSensorPOJO.getSensorPOJO().getActivarAlarma().equals(1)); // 1 alarma activa, 0 alarma inactiva
            alertaHome.getInstance().setFechaMuestra(String2Date(dateRecived));
            alertaHome.getInstance().setValorActual(insertarSensorPOJO.getSensorPOJO().getValorActual());
            alertaHome.persist();

            // 2.3 Registro en Historial

            String mensajeHistorial = "regstro Medicion sensor [" + tipoAlerta.getNombreAlerta() + "]";
            mensajeHistorial += "fecha y hora de la muestra [" + dateRecived + "]";
            mensajeHistorial += "valor de la muestra [" + insertarSensorPOJO.getSensorPOJO().getValorActual() + "]";

            historialHome.getInstance();
            historialHome.getInstance().setCodigoCentroAtencion(_NOMBRE_CENTRO_ATENCION);
            historialHome.getInstance().setCodigoPaciente(insertarSensorPOJO.getConexionPOJO().getIdentificacionPaciente());
            historialHome.getInstance().setFechaCreacion(new Date());
            historialHome.getInstance().setAlerta(alertaHome.getInstance());
            historialHome.getInstance().setTriage(-1);
            historialHome.getInstance().setAlertaClinica(false); //esta solo la puede generar el centro medico
            historialHome.getInstance().setDescripcionHistorial(mensajeHistorial);
            historialHome.getInstance().setUnidadGeneraAlerta(ResponsableAlertaEnum.ALERTA_POR_PACIENTE.getName());
            historialHome.persist();

            String result = "objeto recibido con exito, id generado " + historialHome.getInstance().getId();
            return Response.status(200).entity(result).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Se presentaron errores al registrar medicion del sensor: " + e.getMessage() + "Por favor intEntelo de nuevo").build();
        }
    }

    /**
     * Implementacion servicio web con los datos necesarios para programar el sensor en la unidad base
     *
     * @param insertarSensorPOJO objeto complejo con datos de conexion (ConexionPOJO) y la programacion generada por el
     *                           centro medico del sensor respectivo (SensorPOJO)
     * @return id del registro correcto del sensor en el sistema de integracion, de lo contrario el error respectivo en estado 500
     */
    @Override
    public Response programarSensor(InsertarSensorPOJO insertarSensorPOJO) {

        try {
            // 1 verificar conexion y fechas
            String usuario = insertarSensorPOJO.getConexionPOJO().getUsuario();
            String password = insertarSensorPOJO.getConexionPOJO().getPassword();

            if (usuario.trim().equals("") || password.trim().equals("")) {
                return Response.status(500).entity("El usuario o la clave estAn vacIos... Por favor intEntelo de nuevo").build();
            }

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
            TipoAlerta tipoAlerta;

            if (tipoAlertaList.getResultList().size() <= 0) {
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
            String mensajeAlerta = "Programación Sensor " + insertarSensorPOJO.getSensorPOJO().getNombreSensor() + " con las siguientes condiciones: ";
            mensajeAlerta += "lim max: [" + insertarSensorPOJO.getSensorPOJO().getLimiteMaximo() + "] ";
            mensajeAlerta += "lim min: [" + insertarSensorPOJO.getSensorPOJO().getLimiteMinimo() + "] ";
            mensajeAlerta += "periodicidad: [" + insertarSensorPOJO.getSensorPOJO().getPeriodicidad() + "]";
            mensajeAlerta += "cantidad de dias programados [" + insertarSensorPOJO.getSensorPOJO().getCantidadDias() + "]";
            if (!insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones().equals(-1)) {
                mensajeAlerta += "numeor de respiraciones [" + insertarSensorPOJO.getSensorPOJO().getNumeroRespiraciones() + "]";
            }

            historialHome.getInstance();
            historialHome.getInstance().setAlerta(alertaHome.getInstance());
            historialHome.getInstance().setAlertaClinica(false);
            historialHome.getInstance().setCodigoCentroAtencion(_NOMBRE_CENTRO_ATENCION);
            historialHome.getInstance().setCodigoDoctor(insertarSensorPOJO.getConexionPOJO().getIdentificacionDoctor());
            historialHome.getInstance().setCodigoPaciente(insertarSensorPOJO.getConexionPOJO().getIdentificacionPaciente());
            historialHome.getInstance().setDescripcionHistorial(mensajeAlerta);
            historialHome.getInstance().setFechaCreacion(new Date());
            historialHome.getInstance().setTriage(-1);
            historialHome.getInstance().setUnidadGeneraAlerta(ResponsableAlertaEnum.ALERTA_POR_CENTRO_ATENCION.getName());
            historialHome.persist();

            String result = "objeto recibido con exito, id generado " + historialHome.getInstance().getId();
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Se presentaron errores al progrmar el sensor: " + e.getMessage() + "Por favor intEntelo de nuevo").build();
        }
    }

    /**
     * Implementacion servicio web para el registro y programacion de medicamentos por parte del centro medico
     *
     * @param insertarMedicamentoPOJO objeto complejo con datos de conexion (ConexionPOJO) y la programacion del medicamento
     *                                por el centro medico del sensor respectivo (MedicamentoOJO)
     * @return id del registro correcto del medicamento en el sistema de integracion, de lo contrario el error respectivo en estado 500
     */
    @Override
    public Response programarMedicamento(InsertarMedicamentoPOJO insertarMedicamentoPOJO) {

        try {
            /*// # Insertar en historial

            String mensajeHistorial = "";

            historialHome.getInstance();
            historialHome.getInstance().setCodigoCentroAtencion(_NOMBRE_CENTRO_ATENCION);
            historialHome.getInstance().setCodigoPaciente();
            historialHome.getInstance().setCodigoDoctor();
            historialHome.getInstance().setCodigoAmbulancia();
            historialHome.getInstance().setFechaCreacion(new Date());
            historialHome.getInstance().setAlerta();
            historialHome.getInstance().setTriage();
            historialHome.getInstance().setAlertaClinica(); //esta solo la puede generar el centro medico
            historialHome.getInstance().setDescripcionHistorial(mensajeHistorial);
            historialHome.getInstance().setUnidadGeneraAlerta(ResponsableAlertaEnum);
            historialHome.persist();

            String result = "objeto recibido con exito, id generado " + historialHome.getInstance().getId();
            return Response.status(200).entity(result).build();*/

            String result = "objeto recibido con exito";
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Se presentaron errores al registrar medicion del sensor: " + e.getMessage() + "Por favor intEntelo de nuevo").build();
        }

    }

    /**
     * Implementacion del servicio web para el registro de la ecuesta realizada por el paciente en el
     * sistema centralizado ante un sintoma detectado por el paciente
     *
     * @param insertarEncuestaPOJO objeto complejo con datos de conexion (ConexionPOJO) y la encuesta diligenciada
     *                             por el paciente del sensor respectivo (EncuestaPOJO)
     * @return id del registro correcto de la encuesta en el sistema de integracion, de lo contrario el error respectivo en estado 500
     */
    @Override
    public Response registrarEncuesta(InsertarEncuestaPOJO insertarEncuestaPOJO) {

        try {
            // 1 verificar conexion y fechas
            String usuario = insertarEncuestaPOJO.getConexionPOJO().getUsuario();
            String password = insertarEncuestaPOJO.getConexionPOJO().getPassword();

            if (usuario.trim().equals("") || password.trim().equals("")) {
                return Response.status(500).entity("El usuario o la clave estAn vacIos... Por favor intEntelo de nuevo").build();
            }

            //1.2 validacion campo fecha
            String dateRecived = insertarEncuestaPOJO.getEncuestaPOJO().getFechaRadicado();
            if (!validarFormatoFecha(dateRecived)) {
                return Response.status(500).entity("El campo fecha no tiene el formato correcto... Por favor intEntelo de nuevo").build();
            }

            // 2 insertar en tabla encuesta
            encuestaHome.getEncuestaId();
            encuestaHome.getInstance().setCodigoPaciente(insertarEncuestaPOJO.getConexionPOJO().getIdentificacionPaciente());
            encuestaHome.getInstance().setFechaRadicado(dateRecived);
            encuestaHome.getInstance().setPreguntas(insertarEncuestaPOJO.getEncuestaPOJO().getPreguntas());
            encuestaHome.getInstance().setEncuestaVista(false);
            encuestaHome.persist();

            // 3 Insertar en historial

            String mensajeHistorial = "Encuesta enviada por el paciente con cOdigo: [" + insertarEncuestaPOJO.getConexionPOJO().getIdentificacionPaciente() + "] ";
            mensajeHistorial += "con la estructura: ->[" + insertarEncuestaPOJO.getEncuestaPOJO().getPreguntas() + "]<- ";
            mensajeHistorial += "en la fecha: [" + dateRecived + "]";

            historialHome.getInstance();
            historialHome.getInstance().setTriage(-1);
            historialHome.getInstance().setUnidadGeneraAlerta(ResponsableAlertaEnum.ALERTA_POR_PACIENTE.getName());
            historialHome.getInstance().setFechaCreacion(new Date());
            historialHome.getInstance().setCodigoPaciente(insertarEncuestaPOJO.getConexionPOJO().getIdentificacionPaciente());
            historialHome.getInstance().setAlertaClinica(false); //esta solo la puede generar el centro medico
            historialHome.getInstance().setDescripcionHistorial(mensajeHistorial);
            historialHome.getInstance().setCodigoCentroAtencion(_NOMBRE_CENTRO_ATENCION);
            historialHome.persist();

            String result = "objeto recibido con exito, id generado " + historialHome.getInstance().getId();
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Se presentaron errores al enviar la encuesta: " + e.getMessage() + "Por favor intEntelo de nuevo").build();
        }
    }

    //------------------------------------------ Private Methods ---------------------------------------------------//

    /**
     * Metodo encargado de validar la fecha enviada a traves de los web services
     * @param dateForValidate valor string a validad
     * @return true si el formato corresponde con el estanda YYYY-MM-DD HH:MI de lo contrario falso
     */
    private boolean validarFormatoFecha(String dateForValidate) {
        String patternString = "^\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(dateForValidate);
        return matcher.find();
        //return true;
    }

    /**
     * Metodo encargado de convertir la fecha recivida por un servicio web en un objeto date
     * @param date2Format fecha en formato string a convertir
     * @return valor en formato Date
     * @throws ParseException excepción producida al momento de parsear la fecha
     */
    private Date String2Date(String date2Format) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mi");
        return formatter.parse(date2Format);
    }
}
