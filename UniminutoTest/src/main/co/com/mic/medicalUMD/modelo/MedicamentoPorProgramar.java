package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "medicamento_por_programar")
@Name("medicamentoPorProgramar")
public class MedicamentoPorProgramar implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoPaciente;
    private String codigoDoctor;
    private String nombreMedicamento;
    private Double cantidadRecetada;
    private Integer periodicidadToma;
    private String fechaInicio;
    private String fechafinal;
    private Double cantidadTotal;
    private Boolean estaProgramado;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqMeidcamentoProProg")
    @SequenceGenerator(name = "SeqMeidcamentoProProg", sequenceName = "SeqMeidcamentoProProg", allocationSize=1)
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

    @Column(name = "nombre_medicamento", nullable = true, length = 200)
    @Size(min = 2, max = 200)
    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    @Column(name = "cantidad_recetada", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getCantidadRecetada() {
        return cantidadRecetada;
    }

    @Column(name = "periodicidad_toma", nullable = true, length = 10)
    public Integer getPeriodicidadToma() {
        return periodicidadToma;
    }

    @Column(name = "fecha_inicio", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getFechaInicio() {
        return fechaInicio;
    }

    @Column(name = "fecha_final", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getFechafinal() {
        return fechafinal;
    }

    @Column(name = "cantidad_total", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getCantidadTotal() {
        return cantidadTotal;
    }

    @Column(name = "esta_programado", nullable = false)
    @NotNull
    public Boolean getEstaProgramado() {
        return estaProgramado;
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

    public void setEstaProgramado(Boolean estaProgramado) {
        this.estaProgramado = estaProgramado;
    }
}
