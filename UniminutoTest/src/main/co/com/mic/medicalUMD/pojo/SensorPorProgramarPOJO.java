package co.com.mic.medicalUMD.pojo;

import java.util.List;


public class SensorPorProgramarPOJO
{
    private SensorPOJO sensorPOJO;
    private Integer estaProgramado;

    //------------------------------------------- Constructors --------------------------------------------------//


    public SensorPorProgramarPOJO(SensorPOJO sensorPOJO, Integer estaProgramado) {
        this.sensorPOJO = sensorPOJO;
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Getter --------------------------------------------------//


    public SensorPOJO getSensorPOJO() {
        return sensorPOJO;
    }

    public Integer getEstaProgramado() {
        return estaProgramado;
    }

    //------------------------------------------- Setter --------------------------------------------------//


    public void setSensorPOJOList(SensorPOJO sensorPOJO) {
        this.sensorPOJO = sensorPOJO;
    }

    public void setEstaProgramado(Integer estaProgramado) {
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Bussines MEthod --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("SensorPOJO : ").append(this.sensorPOJO.toString())
                .append("estaProgramado : ").append(this.estaProgramado)
                .toString();
    }
}
