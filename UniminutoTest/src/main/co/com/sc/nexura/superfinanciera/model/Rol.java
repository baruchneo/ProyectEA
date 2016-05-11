package co.com.sc.nexura.superfinanciera.model;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by IEUser on 5/10/2016.
 */
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

    @Id
    @GeneratedValue(generator = "SeqRol")
    @SequenceGenerator(name = "SeqRol", sequenceName = "SeqRol")
    @Column(name = "ID")
    public Long getId()
    {
        return id;
    }

    public void setID(Long id)
    {
        this.id = id;
    }

    @Column(name = "nombre_rol", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getNombreRol()
    {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol)
    {
        this.nombreRol = nombreRol;
    }
}
