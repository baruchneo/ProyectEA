package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import java.util.Date;
import java.util.Set;

/**
 * Created by Edward on 10/05/2016.
 */

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

    private Set<Rol> rolSet;

    private Set<CentroMedico> centroMedicoSet;

    private String email;

    //-------------------------------------------------- Getters ----------------------------------------------------//

    @Id
    @GeneratedValue(generator = "SeqUsuario")
    @SequenceGenerator(name = "SeqUsuario", sequenceName = "SeqUsuario")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "nombre_usuario", unique = false, nullable = false, length = 80)
    @NotNull
    @Size(min = 2, max = 80)
    public String getUsuario() {
        return usuario;
    }

    @Column(name = "password", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getPassword() {
        return password;
    }

    @Column(name = "fecha_creacion", unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    @Column(name = "email", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getEmail() {
        return email;
    }

    @ManyToOne(targetEntity = CentroMedico.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_usuario_rol")
    @JoinColumn(name = "id_rol", nullable = false, updatable = false, insertable = true)
    public Set<Rol> getRolSet() {
        return rolSet;
    }

    @ManyToOne(targetEntity = CentroMedico.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_usuario_centro_medico")
    @JoinColumn(name = "id_centro_medico", nullable = false, updatable = true, insertable = true)
    public Set<CentroMedico> getCentroMedicoSet() {
        return centroMedicoSet;
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

    public void setRolSet(Set<Rol> rolSet) {
        this.rolSet = rolSet;
    }

    public void setCentroMedicoSet(Set<CentroMedico> centroMedicoSet) {
        this.centroMedicoSet = centroMedicoSet;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
