package co.com.mic.medicalUMD.action.ws;


import co.com.mic.medicalUMD.modelo.Alerta;
import co.com.mic.medicalUMD.pojo.*;

import javax.ws.rs.core.Response;

public class WSRegistrarDatos implements IWSRegistrarDatos
{

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
    public Response programarSensor(ConexionPOJO conexionPOJO) {

        String result = "objeto recibido con exito";
        System.out.println("funciono HP");

        return Response.status(200).entity(result).build();
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
