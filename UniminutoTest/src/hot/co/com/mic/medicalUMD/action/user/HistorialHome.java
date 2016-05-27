package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Alerta;
import co.com.mic.medicalUMD.modelo.Historial;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("historialHome")
public class HistorialHome extends EntityHome<Historial>
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
	protected Historial createInstance()
	{
		return new Historial();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setHistorialId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getHistorialId()
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
	public Historial getInstance()
	{
		return super.getInstance();
	}
	
	public Historial getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}