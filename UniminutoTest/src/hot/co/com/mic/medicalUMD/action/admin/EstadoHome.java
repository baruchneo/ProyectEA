package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Estado;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("estadoHome")
public class EstadoHome extends EntityHome<Estado>
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
	protected Estado createInstance()
	{
		return new Estado();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setEstadoId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getEstadoId()
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
	public Estado getInstance()
	{
		return super.getInstance();
	}
	
	public Estado getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}