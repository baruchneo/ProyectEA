package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Alerta;
import co.com.mic.medicalUMD.modelo.Historial;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("historialList")
public class HistorialList extends EntityQuery<Historial>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select historial from Historial historial";

	private static final String[] RESTRICTIONS =
	{
		"historial.id = #{historialList.historial.id} ",
	};

	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	private Historial historial = new Historial();

	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//

	public HistorialList()
	{
		setOrderColumn("historial.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public Historial getHistorial()
	{
		return historial;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param historial the historial to set
	 */
	public void setHistorial(Historial historial)
	{
		this.historial = historial;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<Historial> getResultList()
	{
		return super.getResultList();
	}
}
