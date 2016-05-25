package co.com.mic.medicalUMD.pojo;

public class MedicamentoPorProgramarPOJO
{
    private MedicamentoPOJO medicamentoPOJO;
    private Integer estaProgramado;

    //------------------------------------------- Constructors --------------------------------------------------//


    public MedicamentoPorProgramarPOJO() {
    }

    public MedicamentoPorProgramarPOJO(MedicamentoPOJO medicamentoPOJO, Integer estaProgramado) {
        this.medicamentoPOJO = medicamentoPOJO;
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Getters --------------------------------------------------//


    public MedicamentoPOJO getMedicamentoPOJO() {
        return medicamentoPOJO;
    }

    public Integer getEstaProgramado() {
        return estaProgramado;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setMedicamentoPOJO(MedicamentoPOJO medicamentoPOJO) {
        this.medicamentoPOJO = medicamentoPOJO;
    }

    public void setEstaProgramado(Integer estaProgramado) {
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Business Methods --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("MedicamentoPOJO : ").append(this.medicamentoPOJO.toString())
                .append("estaProgramado : ").append(this.estaProgramado)
                .toString();
    }
}
