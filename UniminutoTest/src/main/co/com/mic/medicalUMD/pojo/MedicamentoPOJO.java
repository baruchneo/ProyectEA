package co.com.mic.medicalUMD.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Cristian on 24/05/2016.
 */
public class MedicamentoPOJO
{
    private String nombreMedicamento;
    private Double cantidadRecetada;
    private Integer periodicidadToma;
    private String fechaInicio;  //FORMATO DADO EN  YYYY-MM-DD HH-MI example 2016-05-23 22:54 Formato hora 24 Horas
    private String fechafinal; //FORMATO DADO EN  YYYY-MM-DD HH-MI example 2016-05-23 22:54 Formato hora 24 Horas
    private Double cantidadTotal;

    //------------------------------------------- Constructors --------------------------------------------------//


    public MedicamentoPOJO() {
    }

    public MedicamentoPOJO(@JsonProperty("Nombre Medicamento") String nombreMedicamento,
                           @JsonProperty("Canridad Recetada") Double cantidadRecetada,
                           @JsonProperty("Periocidad Toma") Integer periodicidadToma,
                           @JsonProperty("Fecha Inicio") String fechaInicio,
                           @JsonProperty("Fecha Final") String fechafinal,
                           @JsonProperty("Cantidad Total") Double cantidadTotal) {
        this.nombreMedicamento = nombreMedicamento;
        this.cantidadRecetada = cantidadRecetada;
        this.periodicidadToma = periodicidadToma;
        this.fechaInicio = fechaInicio;
        this.fechafinal = fechafinal;
        this.cantidadTotal = cantidadTotal;
    }

    //------------------------------------------- Getters --------------------------------------------------//


    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public Double getCantidadRecetada() {
        return cantidadRecetada;
    }

    public Integer getPeriodicidadToma() {
        return periodicidadToma;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechafinal() {
        return fechafinal;
    }

    public Double getCantidadTotal() {
        return cantidadTotal;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public void setCantidadRecetada(Double cantidadRecetada) {
        this.cantidadRecetada = cantidadRecetada;
    }

    public void setPeriodicidadToma(Integer periodicidadToma) {
        this.periodicidadToma = periodicidadToma;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechafinal(String fechafinal) {
        this.fechafinal = fechafinal;
    }

    public void setCantidadTotal(Double cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("nombreMedicamento : ").append(this.nombreMedicamento)
                .append("cantidadRecetada : ").append(this.cantidadRecetada)
                .append("periodicidadToma : ").append(this.periodicidadToma)
                .append("fechaInicio : ").append(this.fechaInicio)
                .append("fechafinal : ").append(this.fechafinal)
                .append("cantidadTotal : ").append(this.cantidadTotal)
                .toString();
    }
}
