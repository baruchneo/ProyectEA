package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "historial")
@Name("historial")
public class Historial implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date fecha;
    private Integer Triage;
    private String descripcionHistorial;
    private Boolean alertaClinica;
    private Paciente paciente;
    private ResponsableAlertaEnum unidadGeneraAlerta;
    private Alerta alerta;
    private CentroMedico centroMedico;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqAlerta")
    @SequenceGenerator(name = "SeqAlerta", sequenceName = "SeqAlerta", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getFecha() {
        return fecha;
    }

    @Column(name = "triage", nullable = false, length = 2)
    @NotNull
    public Integer getTriage() {
        return Triage;
    }

    @Column(name = "descripcion_historial", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getDescripcionHistorial() {
        return descripcionHistorial;
    }

    @Column(name = "alerta_clinica", nullable = false)
    @NotNull
    public Boolean getAlertaClinica() {
        return alertaClinica;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_PACIENTE")
    @JoinColumn(name = "id_paciente", nullable = true, updatable = true, insertable = true)
    public Paciente getPaciente() {
        return paciente;
    }

    @Column(name = "unidad_genera_alerta", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public ResponsableAlertaEnum getUnidadGeneraAlerta() {
        return unidadGeneraAlerta;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public Alerta getAlerta() {
        return alerta;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_CENTRO_MEDICO")
    @JoinColumn(name = "id_centro_medico", nullable = true, updatable = true, insertable = true)
    public CentroMedico getCentroMedico() {
        return centroMedico;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTriage(Integer triage) {
        Triage = triage;
    }

    public void setDescripcionHistorial(String descripcionHistorial) {
        this.descripcionHistorial = descripcionHistorial;
    }

    public void setAlertaClinica(Boolean alertaClinica) {
        this.alertaClinica = alertaClinica;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setUnidadGeneraAlerta(ResponsableAlertaEnum unidadGeneraAlerta) {
        this.unidadGeneraAlerta = unidadGeneraAlerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public void setCentroMedico(CentroMedico centroMedico) {
        this.centroMedico = centroMedico;
    }
}
