package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.TipoAlerta;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("tipoAlertaList")
public class TipoAlertaList extends EntityQuery<TipoAlerta>
{

    //---------------------------------------------------------------//
    // Class constants
    //---------------------------------------------------------------//

    /**
     * Default serial version Id
     */
    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select tipoAlerta from TipoAlerta tipoAlerta";

    private static final String[] RESTRICTIONS =
            {
                    "lower(tipoAlerta.nombreTipoAlerta) like lower(concat('%', concat(#{tipoAlertaList.tipoAlerta.nombreAlerta},'%')))",
                    "tipoAlerta.id = #{tipoAlertaList.tipoAlerta.id} ",
            };

    //---------------------------------------------------------------//
    // Class attributes
    //---------------------------------------------------------------//

    private TipoAlerta tipoAlerta = new TipoAlerta();

    //---------------------------------------------------------------//
    // Class constructors methods
    //---------------------------------------------------------------//

    public TipoAlertaList()
    {
        setOrderColumn("tipoAlerta.id");
        setOrderDirection("asc");
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        //setMaxResults(25);
    }

    //---------------------------------------------------------------//
    // Class getters methods
    //---------------------------------------------------------------//

    public TipoAlerta getTipoAlerta()
    {
        return tipoAlerta;
    }

    //---------------------------------------------------------------//
    // Class setters methods
    //---------------------------------------------------------------//

    /**
     * @param tipoAlerta the tipoAlerta to set
     */
    public void setTipoAlerta(TipoAlerta tipoAlerta)
    {
        this.tipoAlerta = tipoAlerta;
    }

    //---------------------------------------------------------------//
    // Class business methods
    //---------------------------------------------------------------//

    @Override
    public List<TipoAlerta> getResultList()
    {
        return super.getResultList();
    }
}
