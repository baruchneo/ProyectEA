package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Paciente;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("pacienteList")
public class PacienteList extends EntityQuery<Paciente>
{

    //---------------------------------------------------------------//
    // Class constants
    //---------------------------------------------------------------//

    /**
     * Default serial version Id
     */
    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select paciente from Paciente paciente";

    private static final String[] RESTRICTIONS =
            {
                    "lower(paciente.nombrePaciente) like lower(concat('%', concat(#{pacienteList.paciente.nombrePaciente},'%')))",
                    "paciente.id = #{pacienteList.paciente.id} ",
                    "lower(paciente.cedulaPaciente) like lower(concat('%', concat(#{pacienteList.paciente.cedulaPaciente},'%')))",
                    "paciente.cedulaPaciente = #{pacienteList.paciente.cedulaPaciente} ",
            };


    //---------------------------------------------------------------//
    // Class attributes
    //---------------------------------------------------------------//

    private Paciente paciente = new Paciente();

    //---------------------------------------------------------------//
    // Class constructors methods
    //---------------------------------------------------------------//

    public PacienteList()
    {
        setOrderColumn("paciente.id");
        setOrderDirection("asc");
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        //setMaxResults(25);
    }

    //---------------------------------------------------------------//
    // Class getters methods
    //---------------------------------------------------------------//

    public Paciente getPaciente()
    {
        return paciente;
    }

    //---------------------------------------------------------------//
    // Class setters methods
    //---------------------------------------------------------------//

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente)
    {
        this.paciente = paciente;
    }

    //---------------------------------------------------------------//
    // Class business methods
    //---------------------------------------------------------------//

    @Override
    public List<Paciente> getResultList()
    {
        return super.getResultList();
    }
}
