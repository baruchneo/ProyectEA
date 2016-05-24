package co.com.mic.medicalUMD.pojo;


import co.com.mic.medicalUMD.modelo.TipoAlerta;

import java.util.Date;

public class SensorPOJO
{
    private Boolean activarAlarma;
    private Integer periodicidad;
    private Double limiteMaximo;
    private Double limiteMinimo;
    private Double valorActual;
    private Date fechaMuestra;
    private Integer numeroRespiraciones;
    private String descripcionAlerta;
    private String nombreSensor; // este se mapea con el tipo alerta

    //------------------------------------------- Constructors --------------------------------------------------//

    public SensorPOJO(Boolean activarAlarma, Integer periodicidad, Double limiteMaximo, Double limiteMinimo, Double valorActual, Date fechaMuestra, Integer numeroRespiraciones, String descripcionAlerta, String nombreSensor) {
        this.activarAlarma = activarAlarma;
        this.periodicidad = periodicidad;
        this.limiteMaximo = limiteMaximo;
        this.limiteMinimo = limiteMinimo;
        this.valorActual = valorActual;
        this.fechaMuestra = fechaMuestra;
        this.numeroRespiraciones = numeroRespiraciones;
        this.descripcionAlerta = descripcionAlerta;
        this.nombreSensor = nombreSensor;
    }


    //------------------------------------------- Getters --------------------------------------------------//


    public Boolean getActivarAlarma() {
        return activarAlarma;
    }

    public Integer getPeriodicidad() {
        return periodicidad;
    }

    public Double getLimiteMaximo() {
        return limiteMaximo;
    }

    public Double getLimiteMinimo() {
        return limiteMinimo;
    }

    public Double getValorActual() {
        return valorActual;
    }

    public Date getFechaMuestra() {
        return fechaMuestra;
    }

    public Integer getNumeroRespiraciones() {
        return numeroRespiraciones;
    }

    public String getDescripcionAlerta() {
        return descripcionAlerta;
    }

    public String getNombreSensor() {
        return nombreSensor;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setActivarAlarma(Boolean activarAlarma) {
        this.activarAlarma = activarAlarma;
    }

    public void setPeriodicidad(Integer periodicidad) {
        this.periodicidad = periodicidad;
    }

    public void setLimiteMaximo(Double limiteMaximo) {
        this.limiteMaximo = limiteMaximo;
    }

    public void setLimiteMinimo(Double limiteMinimo) {
        this.limiteMinimo = limiteMinimo;
    }

    public void setValorActual(Double valorActual) {
        this.valorActual = valorActual;
    }

    public void setFechaMuestra(Date fechaMuestra) {
        this.fechaMuestra = fechaMuestra;
    }

    public void setNumeroRespiraciones(Integer numeroRespiraciones) {
        this.numeroRespiraciones = numeroRespiraciones;
    }

    public void setDescripcionAlerta(String descripcionAlerta) {
        this.descripcionAlerta = descripcionAlerta;
    }

    public void setNombreSensor(String nombreSensor) {
        this.nombreSensor = nombreSensor;
    }

    //------------------------------------------- Business Method  --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("nombreSensor : ").append(this.nombreSensor)
            .append("periodicidad : ").append(this.periodicidad)
            .append("limiteMaximo : ").append(this.limiteMaximo)
            .append("limiteMinimo : ").append(this.limiteMinimo)
            .append("valorActual : ").append(this.valorActual)
            .append("fechaMuestra : ").append(this.fechaMuestra)
            .append("numeroRespiraciones : ").append(this.numeroRespiraciones)
            .append("activarAlarma : ").append(this.activarAlarma)
            .append("descripcionAlerta : ").append(this.descripcionAlerta)
            .toString();
    }
}
