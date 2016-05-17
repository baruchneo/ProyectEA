package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

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

    private Estado estado;

    private Set<Usuario> Usuarios;

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

    @ManyToOne(targetEntity = Estado.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_rol_estado")
    @JoinColumn(name = "id_estado", nullable = false)
    public Estado getEstado() {
        return estado;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy="rol")
    public Set<Usuario> getUsuarios()
    {
        return Usuarios;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setNombreRol(String nombreRol)
    {
        this.nombreRol = nombreRol;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        Usuarios = usuarios;
    }
}
