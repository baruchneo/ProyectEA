package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Table(name = "paciente")
@Name("paciente")
public class Paciente implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String cedula;
    private String nombrePaciente;
    private Double edad;


    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqPaciente")
    @SequenceGenerator(name = "SeqPaciente", sequenceName = "SeqPaciente", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "cedula", nullable = false, length = 40)
    @NotNull
    @Size(min = 2, max = 40)
    public String getCedula() {
        return cedula;
    }

    @Column(name = "cedula", nullable = false, length = 40)
    @NotNull
    @Size(min = 2, max = 40)
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    @Column(name = "edad", nullable = false, length = 20, precision = 2)
    @NotNull
    @Size(min = 0)
    public Double getEdad()
    {
        return edad;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setCedula(String cedula)
    {
        this.cedula = cedula;
    }

    public void setNombrePaciente(String nombrePaciente)
    {
        this.nombrePaciente = nombrePaciente;
    }

    public void setEdad(Double edad)
    {
        this.edad = edad;
    }

}
