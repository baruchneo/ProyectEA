package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Rango;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("rangoList")
public class RangoList extends EntityQuery<Rango>
{

    //---------------------------------------------------------------//
    // Class constants
    //---------------------------------------------------------------//

    /**
     * Default serial version Id
     */
    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select rango from Rango rango";

    private static final String[] RESTRICTIONS =
            {
                    "lower(rango.nombreRango) like lower(concat('%', concat(#{rangoList.rango.nombreRango},'%')))",
                    "rango.id = #{rangoList.rango.id} ",
                    "lower(rango.rangoRango) like lower(concat('%', concat(#{rangoList.rango.rangoRango},'%')))",
                    "rango.estadoRango = #{rangoList.rango.estadoRango} ",
            };


    //---------------------------------------------------------------//
    // Class attributes
    //---------------------------------------------------------------//

    private Rango rango = new Rango();

    //---------------------------------------------------------------//
    // Class constructors methods
    //---------------------------------------------------------------//

    public RangoList()
    {
        setOrderColumn("rango.id");
        setOrderDirection("asc");
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        //setMaxResults(25);
    }

    //---------------------------------------------------------------//
    // Class getters methods
    //---------------------------------------------------------------//

    public Rango getRango()
    {
        return rango;
    }

    //---------------------------------------------------------------//
    // Class setters methods
    //---------------------------------------------------------------//

    /**
     * @param rango the rango to set
     */
    public void setRango(Rango rango)
    {
        this.rango = rango;
    }

    //---------------------------------------------------------------//
    // Class business methods
    //---------------------------------------------------------------//

    @Override
    public List<Rango> getResultList()
    {
        return super.getResultList();
    }
}
