package co.com.mic.medicalUMD.modelo;

import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "estado")
@Name("estado")
public class Estado
{
    private Long id;

    private String nombreEstado;

    //-------------------------------------------------- Getters ----------------------------------------------------//

    @Id
    @GeneratedValue(generator = "SeqEstado")
    @SequenceGenerator(name = "SeqEstado", sequenceName = "SeqEstado")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "nombre_estado", nullable = false, length = 200)
    @NotNull
    @Size(min = 2, max = 200)
    public String getNombreEstado()
    {
        return nombreEstado;
    }

    //-------------------------------------------------- Getters ----------------------------------------------------//

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreEstado(String nombreEstado)
    {
        this.nombreEstado = nombreEstado;
    }
}
