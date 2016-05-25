package co.com.mic.medicalUMD.pojo;

import java.util.List;

public class MedicamentoPorProgramarPOJO
{
    private List<MedicamentoPOJO> medicamentoPOJOList;
    private Integer estaProgramado;

    //------------------------------------------- Constructors --------------------------------------------------//


    public MedicamentoPorProgramarPOJO() {
    }

    public MedicamentoPorProgramarPOJO(List<MedicamentoPOJO> medicamentoPOJOList, Integer estaProgramado) {
        this.medicamentoPOJOList = medicamentoPOJOList;
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Getters --------------------------------------------------//


    public List<MedicamentoPOJO> getMedicamentoPOJOList() {
        return medicamentoPOJOList;
    }

    public Integer getEstaProgramado() {
        return estaProgramado;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setMedicamentoPOJOList(List<MedicamentoPOJO> medicamentoPOJOList) {
        this.medicamentoPOJOList = medicamentoPOJOList;
    }

    public void setEstaProgramado(Integer estaProgramado) {
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Business Methods --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("MedicamentoPOJO : ").append(this.medicamentoPOJOList.toString())
                .append("estaProgramado : ").append(this.estaProgramado)
                .toString();
    }
}
