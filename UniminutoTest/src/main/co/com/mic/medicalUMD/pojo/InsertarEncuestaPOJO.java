package co.com.mic.medicalUMD.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

public class InsertarEncuestaPOJO
{
    private ConexionPOJO conexionPOJO;
    private EncuestaPOJO encuestaPOJO;

    //------------------------------------------- Constructors --------------------------------------------------//


    public InsertarEncuestaPOJO(@JsonProperty("conexion") ConexionPOJO conexionPOJO,
                                @JsonProperty("encuesta") EncuestaPOJO encuestaPOJO) {
        this.conexionPOJO = conexionPOJO;
        this.encuestaPOJO = encuestaPOJO;
    }

    public InsertarEncuestaPOJO() {
    }

    //------------------------------------------- Getters --------------------------------------------------//


    public ConexionPOJO getConexionPOJO() {
        return conexionPOJO;
    }

    public EncuestaPOJO getEncuestaPOJO() {
        return encuestaPOJO;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setConexionPOJO(ConexionPOJO conexionPOJO) {
        this.conexionPOJO = conexionPOJO;
    }

    public void setEncuestaPOJO(EncuestaPOJO encuestaPOJO) {
        this.encuestaPOJO = encuestaPOJO;
    }
}
