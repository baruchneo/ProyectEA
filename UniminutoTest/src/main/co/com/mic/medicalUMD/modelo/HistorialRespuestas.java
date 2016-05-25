package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "historial_respuestas")
@Name("historial_respuestas")
public class HistorialRespuestas implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoPaciente;
    private String codigoDoctor;
    private Date FechaHora;
    private String nombreDoctor;
    private String respuestaLarga;
    private String respuestaCorta;
    private String linkRespuesta;
    private UsuarioPortal usuarioPortal;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqhistRta")
    @SequenceGenerator(name = "SeqhistRta", sequenceName = "SeqhistRta", allocationSize=1)
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

    @Column(name = "fecha_hora", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getFechaHora() {
        return FechaHora;
    }

    @Column(name = "nombre_doctor", nullable = true, length = 200)
    @Size(min = 2, max = 200)
    public String getNombreDoctor() {
        return nombreDoctor;
    }

    @Column(name = "respuesta_larga", nullable = true, length = 400)
    @Size(min = 2, max = 400)
    public String getRespuestaLarga() {
        return respuestaLarga;
    }

    @Column(name = "respuesta_corta", nullable = true, length = 100)
    @Size(min = 2, max = 100)
    public String getRespuestaCorta() {
        return respuestaCorta;
    }

    @Column(name = "link_respuesta", nullable = true, length = 100)
    @Size(min = 2, max = 100)
    public String getLinkRespuesta() {
        return linkRespuesta;
    }

    @ManyToOne(targetEntity = UsuarioPortal.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_historial_respuesta_usaurio_portal")
    @JoinColumn(name = "id_usaurio_portal", nullable = false, updatable = false)
    public UsuarioPortal getUsuarioPortal() {
        return usuarioPortal;
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

    public void setFechaHora(Date fechaHora) {
        FechaHora = fechaHora;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public void setRespuestaLarga(String respuestaLarga) {
        this.respuestaLarga = respuestaLarga;
    }

    public void setRespuestaCorta(String respuestaCorta) {
        this.respuestaCorta = respuestaCorta;
    }

    public void setLinkRespuesta(String linkRespuesta) {
        this.linkRespuesta = linkRespuesta;
    }

    public void setUsuarioPortal(UsuarioPortal usuarioPortal) {
        this.usuarioPortal = usuarioPortal;
    }
}
