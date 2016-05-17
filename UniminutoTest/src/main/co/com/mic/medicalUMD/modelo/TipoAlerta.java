package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tipo_alerta")
@Name("tipoAlerta")
public class TipoAlerta implements Serializable, Cloneable
{
    /**
     * Default serial version Id.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombreAlerta;
    private Set<Alerta> alertas;

    //------------------------------------------- Getters --------------------------------------------------//

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqTipoAlerta")
    @SequenceGenerator(name = "SeqTipoAlerta", sequenceName = "SeqTipoAlerta", allocationSize=1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "nombre_alerta", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getNombreAlerta() {
        return nombreAlerta;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy="tipoAlerta")
    public Set<Alerta> getAlertas() {
        return alertas;
    }

    //------------------------------------------- Setters --------------------------------------------------//

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setNombreAlerta(String nombreAlerta)
    {
        this.nombreAlerta = nombreAlerta;
    }

    public void setAlertas(Set<Alerta> alertas) {
        this.alertas = alertas;
    }
}
