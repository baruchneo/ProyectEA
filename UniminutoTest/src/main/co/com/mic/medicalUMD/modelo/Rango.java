package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "rango")
@Name("rango")
public class Rango implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombreRango;
    private String rango;
    private Estado estado;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqPaciente")
    @SequenceGenerator(name = "SeqPaciente", sequenceName = "SeqPaciente", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "nombre_rango", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getNombreRango() {
        return nombreRango;
    }

    @Column(name = "rango", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getRango() {
        return rango;
    }

    @ManyToOne(targetEntity = Estado.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_rango_estado")
    @JoinColumn(name = "id_estado", nullable = false)
    public Estado getEstado() {
        return estado;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setNombreRango(String nombreRango)
    {
        this.nombreRango = nombreRango;
    }

    public void setRango(String rango)
    {
        this.rango = rango;
    }

    public void setEstado(Estado estado)
    {
        this.estado = estado;
    }
}
