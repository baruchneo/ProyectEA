package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "sensor_por_programar")
@Name("sensorPorProgramar")
@XmlRootElement(name="sensorPorProgramar")
public class SensorPorProgramar implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoPaciente;
    private String codigoDoctor;
    private String nombreSensor;
    private Integer periodicidad;
    private Double limiteMinimo;
    private Double limiteMaximo;
    private Integer numeroRespiraciones;
    private String descripcionSensor;
    private Boolean estaProgramado;
    private Integer cantidadDias;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqSensorProProg")
    @SequenceGenerator(name = "SeqSensorProProg", sequenceName = "SeqSensorProProg", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "codigo_paciente", nullable = false, length = 10)
    @NotNull
    @Size(min = 2, max = 10)
    public String getCodigoPaciente() {
        return codigoPaciente;
    }

    @Column(name = "codigo_doctor", nullable = true, length = 10)
    @Size(min = 2, max = 10)
    public String getCodigoDoctor() {
        return codigoDoctor;
    }

    @Column(name = "nombre_sensor", nullable = true, length = 200)
    @Size(min = 2, max = 200)
    public String getNombreSensor() {
        return nombreSensor;
    }

    @Column(name = "periodicidad", nullable = true, length = 10)
    public Integer getPeriodicidad() {
        return periodicidad;
    }

    @Column(name = "limite_maximo", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getLimiteMinimo() {
        return limiteMinimo;
    }

    @Column(name = "limite_minimo", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getLimiteMaximo() {
        return limiteMaximo;
    }

    @Column(name = "numero_respiraciones", nullable = true, length = 2)
    public Integer getNumeroRespiraciones() {
        return numeroRespiraciones;
    }

    @Column(name = "descripcion_sensor", nullable = true, length = 200)
    @Size(min = 2, max = 200)
    public String getDescripcionSensor() {
        return descripcionSensor;
    }

    @Column(name = "esta_programado", nullable = false)
    @NotNull
    public Boolean getEstaProgramado() {
        return estaProgramado;
    }

    @Column(name = "cantidad_dias", nullable = true, length = 2)
    public Integer getCantidadDias() {
        return cantidadDias;
    }

    //------------------------------------------- Setters --------------------------------------------------//


    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigoPaciente(String codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public void setCodigoDoctor(String codigoDoctor) {
        this.codigoDoctor = codigoDoctor;
    }

    public void setNombreSensor(String nombreSensor) {
        this.nombreSensor = nombreSensor;
    }

    public void setPeriodicidad(Integer periodicidad) {
        this.periodicidad = periodicidad;
    }

    public void setLimiteMinimo(Double limiteMinimo) {
        this.limiteMinimo = limiteMinimo;
    }

    public void setLimiteMaximo(Double limiteMaximo) {
        this.limiteMaximo = limiteMaximo;
    }

    public void setNumeroRespiraciones(Integer numeroRespiraciones) {
        this.numeroRespiraciones = numeroRespiraciones;
    }

    public void setDescripcionSensor(String descripcionSensor) {
        this.descripcionSensor = descripcionSensor;
    }

    public void setEstaProgramado(Boolean estaProgramado) {
        this.estaProgramado = estaProgramado;
    }

    public void setCantidadDias(Integer cantidadDias) {
        this.cantidadDias = cantidadDias;
    }
}
