package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Alerta;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("alertaHome")
public class AlertaHome extends EntityHome<Alerta>
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
	protected Alerta createInstance()
	{
		return new Alerta();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setAlertaId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getAlertaId()
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
	public Alerta getInstance()
	{
		return super.getInstance();
	}
	
	public Alerta getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}