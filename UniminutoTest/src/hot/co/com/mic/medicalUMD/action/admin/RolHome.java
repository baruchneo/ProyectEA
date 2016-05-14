package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Rol;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("rolHome")
public class RolHome extends EntityHome<Rol>
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
	
	//@In(create = true)
	//inancialInstitutionTypeHome financialInstitutionTypeHome;
	
	//@In(create = true)
	//FinancialInstitutionHome financialInstitutionHome;
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	@Override
	protected Rol createInstance()
	{
		return new Rol();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setRolId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getRolId()
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
	public Rol getInstance()
	{
		return super.getInstance();
	}
	
	public Rol getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
	
	/*@Override
	public String remove()
	{
		//1. buscar la existencia de tipos de reporte tipos de entidades, y entidades asociadas al reporte
		if(getInstance().getFinancialInstitutions().size() > 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar el proceso de negocio. Tiene entidades financieras asociadas", "Entidades financieras asociadas"));
			return "fail";
		}
		else
		{
			return super.remove();
		}
	}*/
}