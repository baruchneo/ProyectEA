package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Alerta;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("alertaList")
public class AlertaList extends EntityQuery<Alerta>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select alerta from Alerta alerta";

	private static final String[] RESTRICTIONS =
	{
		"alerta.id = #{alertaList.alerta.id} ",
	};

	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	private Alerta alerta = new Alerta();

	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//

	public AlertaList()
	{
		setOrderColumn("alerta.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public Alerta getAlerta()
	{
		return alerta;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param alerta the alerta to set
	 */
	public void setAlerta(Alerta alerta)
	{
		this.alerta = alerta;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<Alerta> getResultList()
	{
		return super.getResultList();
	}
}
