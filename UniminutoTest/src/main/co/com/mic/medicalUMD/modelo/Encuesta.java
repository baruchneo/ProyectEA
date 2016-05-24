package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "encuesta")
@Name("encuesta")
@XmlRootElement(name="encuesta")
public class Encuesta implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoPaciente;
    private String fechaRadicado;
    private String preguntas;
    private Boolean encuestaVista;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqEncuesta")
    @SequenceGenerator(name = "SeqEncuesta", sequenceName = "SeqEncuesta", allocationSize=1)
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

    @Column(name = "fecha_radicado", nullable = true, length = 20)
    @Size(min = 2, max = 20)
    public String getFechaRadicado() {
        return fechaRadicado;
    }

    @Column(name = "nombre_medicamento", nullable = true, length = 200)
    @Size(min = 2, max = 200)
    public String getPreguntas() {
        return preguntas;
    }

    @Column(name = "encuesta_vista", nullable = false)
    @NotNull
    public Boolean getEncuestaVista() {
        return encuestaVista;
    }

    //------------------------------------------- Getters --------------------------------------------------//


    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigoPaciente(String codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public void setFechaRadicado(String fechaRadicado) {
        this.fechaRadicado = fechaRadicado;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }

    public void setEncuestaVista(Boolean encuestaVista) {
        this.encuestaVista = encuestaVista;
    }
}
