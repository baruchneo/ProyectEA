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
@Table(name = "alerta")
@Name("alerta")
public class Alerta implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private Boolean activarAlarma;
    private Integer periodicidad;
    private Double limiteMaximo;
    private Double limiteMinimo;
    private Double valorActual;
    private Date fechaMuestra;
    private Integer cantidadDias;
    private Integer numeroRespiraciones;
    private String descripcionAlerta;
    private TipoAlerta tipoAlerta;
    private Historial historial;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqAlerta")
    @SequenceGenerator(name = "SeqAlerta", sequenceName = "SeqAlerta", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "activar_alarma", nullable = false)
    @NotNull
    public Boolean getActivarAlarma() {
        return activarAlarma;
    }

    @Column(name = "periodicidad", nullable = false, length = 10)
    @NotNull
    public Integer getPeriodicidad() {
        return periodicidad;
    }

    @Column(name = "limite_maximo", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getLimiteMaximo()
    {
        return limiteMaximo;
    }

    @Column(name = "limite_minimo", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getLimiteMinimo() {
        return limiteMinimo;
    }

    @Column(name = "numero_respiraciones", nullable = true, length = 2)
    public Integer getNumeroRespiraciones() {
        return numeroRespiraciones;
    }

    @Column(name = "descripcion_alerta", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getDescripcionAlerta() {
        return descripcionAlerta;
    }

    @Column(name = "valor_actual", nullable = true, length = 20, precision = 2)
    @Size(min = 0)
    public Double getValorActual() {
        return valorActual;
    }

    @Column(name = "fecha_muestra", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFechaMuestra() {
        return fechaMuestra;
    }

    @Column(name = "cantidad_dias", nullable = true, length = 2)
    public Integer getCantidadDias() {
        return cantidadDias;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_TIPO_ALERTA")
    @JoinColumn(name = "id_tipo_alerta", nullable = false, updatable = true, insertable = true)
    @NotNull
    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "alerta", cascade = CascadeType.ALL)
    public Historial getHistorial() {
        return historial;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public void setNumeroRespiraciones(Integer numeroRespiraciones) {
        this.numeroRespiraciones = numeroRespiraciones;
    }

    public void setDescripcionAlerta(String descripcionAlerta) {
        this.descripcionAlerta = descripcionAlerta;
    }

    public void setValorActual(Double valorActual) {
        this.valorActual = valorActual;
    }

    public void setFechaMuestra(Date fechaMuestra) {
        this.fechaMuestra = fechaMuestra;
    }

    public void setCantidadDias(Integer cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public void setTipoAlerta(TipoAlerta tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }
}
