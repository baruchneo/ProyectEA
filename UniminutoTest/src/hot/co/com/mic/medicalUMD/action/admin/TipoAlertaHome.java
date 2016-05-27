package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Alerta;
import co.com.mic.medicalUMD.modelo.TipoAlerta;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipoAlertaHome")
public class TipoAlertaHome extends EntityHome<TipoAlerta>
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
	protected TipoAlerta createInstance()
	{
		return new TipoAlerta();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setTipoAlertaId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getTipoAlertaId()
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
	public TipoAlerta getInstance()
	{
		return super.getInstance();
	}
	
	public TipoAlerta getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}