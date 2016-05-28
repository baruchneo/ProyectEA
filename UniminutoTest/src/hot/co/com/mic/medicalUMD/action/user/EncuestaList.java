package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Encuesta;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("encuestaList")
public class EncuestaList extends EntityQuery<Encuesta>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select encuesta from Encuesta encuesta";

	private static final String[] RESTRICTIONS =
	{
		"encuesta.id = #{encuestaList.encuesta.id} ",
	};

	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	private Encuesta encuesta = new Encuesta();

	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//

	public EncuestaList()
	{
		setOrderColumn("encuesta.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public Encuesta getEncuesta()
	{
		return encuesta;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param encuesta the encuesta to set
	 */
	public void setEncuesta(Encuesta encuesta)
	{
		this.encuesta = encuesta;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<Encuesta> getResultList()
	{
		return super.getResultList();
	}
}
