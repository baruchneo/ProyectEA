package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "usuario_portal")
@Name("usuario_portal")
public class UsuarioPortal implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoPaciente;
    private String cedula;
    private String clave;
    private String email;
    private Boolean verificacion;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqUsuarioPortal")
    @SequenceGenerator(name = "SeqUsuarioPortal", sequenceName = "SeqUsuarioPortal", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "codigo_paciente", nullable = true, length = 10)
    @Size(min = 2, max = 10)
    public String getCodigoPaciente() {
        return codigoPaciente;
    }

    @Column(name = "cedula", nullable = false, length = 80)
    @NotNull
    @Size(min = 2, max = 80)
    public String getCedula() {
        return cedula;
    }

    @Column(name = "clave", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getClave() {
        return clave;
    }

    @Column(name = "email", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getEmail() {
        return email;
    }

    @Column(name = "verifcacion", nullable = false)
    @NotNull
    public Boolean getVerificacion() {
        return verificacion;
    }

    //-------------------------------------------------- Getters ----------------------------------------------------//


    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigoPaciente(String codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerificacion(Boolean verificacion) {
        this.verificacion = verificacion;
    }
}
