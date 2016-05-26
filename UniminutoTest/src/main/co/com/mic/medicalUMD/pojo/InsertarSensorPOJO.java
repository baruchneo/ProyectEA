package co.com.mic.medicalUMD.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Cristian on 25/05/2016.
 */
public class InsertarSensorPOJO
{
    private  SensorPOJO sensorPOJO;
    private ConexionPOJO conexionPOJO;

    //------------------------------------------- Constructors --------------------------------------------------//


    public InsertarSensorPOJO(@JsonProperty("sensor") SensorPOJO sensorPOJO,
                              @JsonProperty("conexion") ConexionPOJO conexionPOJO) {
        this.sensorPOJO = sensorPOJO;
        this.conexionPOJO = conexionPOJO;
    }

    //------------------------------------------- Getters ---------------------------------------------------------//


    public SensorPOJO getSensorPOJO() {
        return sensorPOJO;
    }

    public ConexionPOJO getConexionPOJO() {
        return conexionPOJO;
    }

    //------------------------------------------- Setters ---------------------------------------------------------//


    public void setSensorPOJO(SensorPOJO sensorPOJO) {
        this.sensorPOJO = sensorPOJO;
    }

    public void setConexionPOJO(ConexionPOJO conexionPOJO) {
        this.conexionPOJO = conexionPOJO;
    }
}

