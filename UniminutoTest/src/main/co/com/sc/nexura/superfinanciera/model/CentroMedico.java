package co.com.sc.nexura.superfinanciera.model;

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
@Table(name = "centro_medico")
@Name("centroMedico")
public class CentroMedico implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String nombreCentro;

    private String direccionCentro;

    private Set<Doctor> doctorSet;

    @Id
    @GeneratedValue(generator = "SeqCentroMedico")
    @SequenceGenerator(name = "SeqCentroMedico", sequenceName = "SeqCentroMedico")
    @Column(name = "Id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "nombre_centro", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getNombreCentro() {
        return nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }

    @Column(name = "direccion_centro", unique = false, nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getDireccionCentro() {
        return direccionCentro;
    }

    public void setDireccionCentro(String direccionCentro) {
        this.direccionCentro = direccionCentro;
    }

    @ManyToMany(targetEntity = Doctor.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_cent_med_cent_med" , inverseName = "FK_cent_med_doc")
    @JoinTable(name = "centro_medico_doctor", joinColumns = @JoinColumn(name = "id_cent_med"), inverseJoinColumns = @JoinColumn(name = "id_doctor"))
    public Set<Doctor> getDoctorSet() {
        return doctorSet;
    }

    public void setDoctorList(Set<Doctor> doctorSet) {
        this.doctorSet = doctorSet;
    }
}
