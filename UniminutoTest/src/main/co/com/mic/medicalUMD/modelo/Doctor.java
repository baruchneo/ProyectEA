package co.com.mic.medicalUMD.modelo;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by IEUser on 5/10/2016.
 */
@Entity
@Table(name = "doctor")
@Name("doctor")
public class Doctor implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String cedula;

    private String nombres;

    private String apellidos;

    private Set<CentroMedico> centroMedicoSet;

    @Id
    @GeneratedValue(generator = "SeqDoctor")
    @SequenceGenerator(name = "SeqDoctor", sequenceName = "SeqDoctor")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "cedula", unique = false, nullable = false, length = 20)
    @NotNull
    @Size(min = 2, max = 20)
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Column(name = "nombres", unique = false, nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Column(name = "apellidos", unique = false, nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @ManyToMany(targetEntity = Doctor.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_doc_doc" , inverseName = "FK_doc_cent_med")
    @JoinTable(name = "centro_medico_doctor", joinColumns = @JoinColumn(name = "id_cent_med"), inverseJoinColumns = @JoinColumn(name = "id_doctor"))
    public Set<CentroMedico> getCentroMedicoSet() {
        return centroMedicoSet;
    }

    public void setCentroMedicoSet(Set<CentroMedico> centroMedicoSet) {
        this.centroMedicoSet = centroMedicoSet;
    }
}
