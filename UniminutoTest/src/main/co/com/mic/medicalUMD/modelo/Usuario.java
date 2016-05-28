package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Name("usuario")
public class Usuario implements Serializable, Cloneable{

    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Primary key
     */
    private Long id;

    /**
     * Nombre del usuario
     */
    private String usuario;

    /**
     * Contrasena
     */
    private String password;

    /**
     * Fecha de creaciOn
     */
    private Date fechaCreacion;

    private String email;

    private Rol rol;

    private String estado;

    //-------------------------------------------------- Getters ----------------------------------------------------//

    @Id
    @GeneratedValue(generator = "SeqUsuario")
    @SequenceGenerator(name = "SeqUsuario", sequenceName = "SeqUsuario")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "nombre_usuario", nullable = false, length = 80)
    @NotNull
    @Size(min = 2, max = 80)
    public String getUsuario() {
        return usuario;
    }

    @Column(name = "password", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getPassword() {
        return password;
    }

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    @Column(name = "email", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getEmail() {
        return email;
    }

    @Column(name = "estado", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getEstado() {
        return estado;
    }

    @ManyToOne(targetEntity = Rol.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_usuario_rol")
    @JoinColumn(name = "id_rol", nullable = false, updatable = false)
    public Rol getRol() {
        return rol;
    }

    //-------------------------------------------------- Getters ----------------------------------------------------//

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
