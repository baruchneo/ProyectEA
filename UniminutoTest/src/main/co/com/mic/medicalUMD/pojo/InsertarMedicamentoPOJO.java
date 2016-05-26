package co.com.mic.medicalUMD.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Cristian on 25/05/2016.
 */
public class InsertarMedicamentoPOJO {
    private MedicamentoPOJO mmeMedicamentoPOJO;
    private ConexionPOJO conexionPOJO;

    //------------------------------------------- Constructors --------------------------------------------------//


    public InsertarMedicamentoPOJO(@JsonProperty("medicamento")MedicamentoPOJO mmeMedicamentoPOJO,
                                   @JsonProperty("conexion") ConexionPOJO conexionPOJO) {
        this.mmeMedicamentoPOJO = mmeMedicamentoPOJO;
        this.conexionPOJO = conexionPOJO;
    }

    public InsertarMedicamentoPOJO() {
    }

    //------------------------------------------- Getters --------------------------------------------------//

    public MedicamentoPOJO getMmeMedicamentoPOJO() {
        return mmeMedicamentoPOJO;
    }

    public ConexionPOJO getConexionPOJO() {
        return conexionPOJO;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setMmeMedicamentoPOJO(MedicamentoPOJO mmeMedicamentoPOJO) {
        this.mmeMedicamentoPOJO = mmeMedicamentoPOJO;
    }

    public void setConexionPOJO(ConexionPOJO conexionPOJO) {
        this.conexionPOJO = conexionPOJO;
    }
}
