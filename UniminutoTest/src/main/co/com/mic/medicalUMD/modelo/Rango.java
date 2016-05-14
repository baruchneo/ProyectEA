package co.com.mic.medicalUMD.modelo;

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
    private String estadoRango;

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

    @Column(name = "estado_rango", nullable = false, length = 200)
    @NotNull
    public String getEstadoRango() {
        return estadoRango;
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

    public void setEstadoRango(String estadoRango)
    {
        this.estadoRango = estadoRango;
    }
}
