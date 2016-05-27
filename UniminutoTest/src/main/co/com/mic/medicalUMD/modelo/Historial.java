package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "historial")
@Name("historial")
@XmlRootElement(name="historial")
public class Historial implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date fechaCreacion;
    private Integer Triage;
    private String descripcionHistorial;
    private Boolean alertaClinica;
    private String codigoCentroAtencion;
    private String codigoDoctor;
    private String codigoAmbulancia;
    private String codigoPaciente;
    private String unidadGeneraAlerta;
    private Alerta alerta;

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
    public Date getFechaCreacion() {
        return fechaCreacion;
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

    @Column(name = "unidad_genera_alerta", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getUnidadGeneraAlerta() {
        return unidadGeneraAlerta;
    }

    @Column(name = "codigo_centro_atencion", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getCodigoCentroAtencion() {
        return codigoCentroAtencion;
    }

    @Column(name = "codigo_doctor", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getCodigoDoctor() {
        return codigoDoctor;
    }

    @Column(name = "codigo_ambulancia", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getCodigoAmbulancia() {
        return codigoAmbulancia;
    }

    @Column(name = "codigo_paciente", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getCodigoPaciente() {
        return codigoPaciente;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_alerta")
    public Alerta getAlerta() {
        return alerta;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public void setUnidadGeneraAlerta(String unidadGeneraAlerta) {
        this.unidadGeneraAlerta = unidadGeneraAlerta;
    }

    public void setCodigoCentroAtencion(String codigoCentroAtencion) {
        this.codigoCentroAtencion = codigoCentroAtencion;
    }

    public void setCodigoDoctor(String codigoDoctor) {
        this.codigoDoctor = codigoDoctor;
    }

    public void setCodigoAmbulancia(String codigoAmbulancia) {
        this.codigoAmbulancia = codigoAmbulancia;
    }

    public void setCodigoPaciente(String codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }
}
