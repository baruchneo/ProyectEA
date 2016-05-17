package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Usuario;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("usuarioHome")
public class UsuarioHome extends EntityHome<Usuario>
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
	protected Usuario createInstance()
	{
		return new Usuario();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setUsuarioId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getUsuarioId()
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
	public Usuario getInstance()
	{
		return super.getInstance();
	}
	
	public Usuario getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}