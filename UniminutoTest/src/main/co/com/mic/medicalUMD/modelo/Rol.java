package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "rol")
@Name("rol")
public class Rol implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    private Long id;

    /**
     * nombre del rol
     */
    private String nombreRol;

    private Integer estadoRol;

    @Id
    @GeneratedValue(generator = "SeqRol")
    @SequenceGenerator(name = "SeqRol", sequenceName = "SeqRol")
    @Column(name = "id")
    public Long getId()
    {
        return id;
    }

    @Column(name = "nombre_rol", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getNombreRol()
    {
        return nombreRol;
    }

    @Column(name = "estado_rol", nullable = false, length = 2)
    @NotNull
    public Integer getEstadoRol() {
        return estadoRol;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setNombreRol(String nombreRol)
    {
        this.nombreRol = nombreRol;
    }

    public void setEstadoRol(Integer estadoRol) {
        this.estadoRol = estadoRol;
    }
}
