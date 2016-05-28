package co.com.mic.medicalUMD.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

// esto es para jhon, para que programe la unidad base

public class SensorPorProgramarPOJO
{
    private List<SensorPOJO> sensorPOJOList;
    private Integer estaProgramado;

    //------------------------------------------- Constructors --------------------------------------------------//


    public SensorPorProgramarPOJO(@JsonProperty("sensorPOJO") List<SensorPOJO> sensorPOJOList,
                                  @JsonProperty("estaProgramado") Integer estaProgramado) {
        this.sensorPOJOList = sensorPOJOList;
        this.estaProgramado = estaProgramado;
    }

    public SensorPorProgramarPOJO() {
    }

    //------------------------------------------- Getter --------------------------------------------------//


    public List<SensorPOJO> getSensorPOJOList() {
        return sensorPOJOList;
    }

    public Integer getEstaProgramado() {
        return estaProgramado;
    }

    //------------------------------------------- Setter --------------------------------------------------//

    public void setSensorPOJOList(List<SensorPOJO> sensorPOJOList) {
        this.sensorPOJOList = sensorPOJOList;
    }

    public void setEstaProgramado(Integer estaProgramado) {
        this.estaProgramado = estaProgramado;
    }

    //------------------------------------------- Bussines MEthod --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("sensorPOJO : ").append(this.sensorPOJOList.toString())
                .append("estaProgramado : ").append(this.estaProgramado)
                .toString();
    }
}
