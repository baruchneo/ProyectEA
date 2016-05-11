package co.com.sc.nexura.superfinanciera.model;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
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
     * primary key
     */
    private Long id;

    /**
     * nombre del usuario
     */
    private String usuario;

    /**
     * contrasena
     */
    private String contrasena;

    /**
     * fecha
     */

    private Date fechaCreacion;


    private Set rolSet;


    private Set<CentroMedico> centroMedicoSet;

    /**
     * --------------Inicio------------------------------------
     */


    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "SeqUsuario")
    @SequenceGenerator(name = "SeqUsuario", sequenceName = "SeqUsuario")
    @Column(name = "ID")
    public Long getId()
    {
        return id;
    }

    public void setID(Long id)
    {
        this.id = id;
    }


    /**
     * USUARIO
     */
    @Column(name = "usuario", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getUsuario()
    {
        return usuario;
    }
    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    /**
     * CONTRASENA
     */

    @Column(name = "contrasena", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getContrasena()
    {
        return contrasena;
    }

    public void setContrasena(String contrasena)
    {
        this.contrasena = contrasena;
    }


    /**
     * FECHA DE CREACION
     */
    @Column(name = "fechaCreacion", unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getfechaCreacion()
    {
        return fechaCreacion;
    }

    /**
     * ROL
     */
    @ManyToOne(fetch=FetchType.EAGER)
    @ForeignKey(name = "FK_Rol")
    @JoinColumn (name = "Id_Rol", nullable=false, updatable=false, insertable=true)
    public Set rolSet() {
        return rolSet;
    }

    public void setrolSet(Set rolSet) {
        this.rolSet = rolSet;
    }



    /**
     * CENTRO MEDICO
     */


    @ManyToMany(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_use_use" , inverseName = "FK_use_cent_med")
    @JoinTable(name = "centro_medico_usuario", joinColumns = @JoinColumn(name = "id_cent_med"), inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    public Set<CentroMedico> getCentroMedicoSet() {
        return centroMedicoSet;
    }

    public void setCentroMedicoSet(Set<CentroMedico> centroMedicoSet) {
        this.centroMedicoSet = centroMedicoSet;
    }

}
