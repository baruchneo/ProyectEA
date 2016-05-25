package co.com.mic.medicalUMD.pojo;


import co.com.mic.medicalUMD.modelo.TipoAlerta;

import java.util.Date;

public class SensorPOJO
{
    private Integer activarAlarma;
    private Integer periodicidad;
    private Integer cantidadDias;
    private Double limiteMaximo;
    private Double limiteMinimo;
    private Double valorActual;
    private String fechaMuestra;
    private Integer numeroRespiraciones;
    private String descripcionAlerta;
    private String nombreSensor; // este se mapea con el tipo alerta

    //------------------------------------------- Constructors --------------------------------------------------//

    public SensorPOJO(Integer activarAlarma, Integer periodicidad, Double limiteMaximo, Double limiteMinimo, Double valorActual, String fechaMuestra, Integer numeroRespiraciones, String descripcionAlerta, String nombreSensor, Integer cantidadDias) {
        this.activarAlarma = activarAlarma;
        this.periodicidad = periodicidad;
        this.limiteMaximo = limiteMaximo;
        this.limiteMinimo = limiteMinimo;
        this.valorActual = valorActual;
        this.fechaMuestra = fechaMuestra;
        this.numeroRespiraciones = numeroRespiraciones;
        this.descripcionAlerta = descripcionAlerta;
        this.nombreSensor = nombreSensor;
        this.cantidadDias = cantidadDias;
    }

    public SensorPOJO() {

    }


    //------------------------------------------- Getters --------------------------------------------------//


    public Integer getActivarAlarma() {
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

    public String getFechaMuestra() {
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

    public Integer getCantidadDias() {
        return cantidadDias;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setActivarAlarma(Integer activarAlarma) {
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

    public void setFechaMuestra(String fechaMuestra) {
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

    public void setCantidadDias(Integer cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    //------------------------------------------- Business Method  --------------------------------------------------//

    @Override
    public String toString()
    {
        return new StringBuffer("nombreSensor : ").append(this.nombreSensor)
            .append("cantidadDias : ").append(this.cantidadDias)
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
