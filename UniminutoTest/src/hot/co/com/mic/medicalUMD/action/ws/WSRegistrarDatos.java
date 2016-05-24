package co.com.mic.medicalUMD.action.ws;


public class WSRegistrarDatos implements IWSRegistrarDatos
{

    @Override
    public String registrarSensor(String datosSensor, String usuario, String password)
    {
        //TODO copiar a la tabla Historial
        //TODO Verificar que exista usuario y clave

        return "{'resultado':'sensor registrado satisfactoriamente para el paciente PAC1'}";
    }
}
