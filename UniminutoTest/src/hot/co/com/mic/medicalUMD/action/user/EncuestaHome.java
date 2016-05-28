package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Encuesta;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("encuestaHome")
public class EncuestaHome extends EntityHome<Encuesta>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	@Override
	protected Encuesta createInstance()
	{
		return new Encuesta();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setEncuestaId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getEncuestaId()
	{
		return (Long) getId();
	}

	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//

	public void load()
	{
		if (isIdDefined())
		{
			wire();
		}
	}

	public void wire()
	{
		getInstance();
	}

	public boolean isWired()
	{
		return true;
	}
	

	@Override
	public Encuesta getInstance()
	{
		return super.getInstance();
	}
	
	public Encuesta getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}