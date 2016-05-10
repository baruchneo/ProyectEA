package co.com.sc.nexura.superfinanciera.action.admin;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitutionType;
import co.com.sc.nexura.superfinanciera.model.ReportType;

@Name("businessProcessHome")
public class BusinessProcessHome extends EntityHome<BusinessProcess>
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
	
	@In(create = true)
	FinancialInstitutionTypeHome financialInstitutionTypeHome;
	
	@In(create = true)
	FinancialInstitutionHome financialInstitutionHome;
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	@Override
	protected BusinessProcess createInstance()
	{
		BusinessProcess businessProcess = new BusinessProcess();
		return businessProcess;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setBusinessProcessId(Long id)
	{
		setId(id);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getBusinessProcessId()
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
	
	public void wireManyToManyRelationShip(String operation)
	{
		if (operation != null)
		{
			if (getFinancialInstitutionTypeHome().getDefinedInstance() != null || getFinancialInstitutionHome() != null)
			{
				if (getDefinedInstance() != null)
				{
					if (operation.equalsIgnoreCase("insert"))
					{
						if (!getDefinedInstance().getFinancialInstitutionTypes().contains(getFinancialInstitutionTypeHome().getDefinedInstance()) && getFinancialInstitutionTypeHome().getDefinedInstance() != null)
						{
							getDefinedInstance().getFinancialInstitutionTypes().add(getFinancialInstitutionTypeHome().getDefinedInstance());
							update();
							getFinancialInstitutionTypeHome().clearInstance();
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Relacion establecida exitosamente.", "Relacion establecida"));
						}
						else if(!getDefinedInstance().getFinancialInstitutions().contains(getFinancialInstitutionHome().getDefinedInstance()) && getFinancialInstitutionHome().getDefinedInstance() != null)
						{
							getDefinedInstance().getFinancialInstitutions().add(getFinancialInstitutionHome().getDefinedInstance());
							update();
							getFinancialInstitutionHome().clearInstance();
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Relacion establecida exitosamente.", "Relacion establecida"));
						}
						else
						{
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Esta entidad ya esta asociada.", "Duplicidad de datos"));
						}
					}
					else if (operation.equalsIgnoreCase("delete"))
					{
						if (getDefinedInstance().getFinancialInstitutionTypes().contains(getFinancialInstitutionTypeHome().getDefinedInstance()) && getFinancialInstitutionTypeHome().getDefinedInstance() != null)
						{
							getDefinedInstance().getFinancialInstitutionTypes().remove(getFinancialInstitutionTypeHome().getDefinedInstance());
							update();
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Relacion eliminada exitosamente.", "Relacion eliminada"));
						}
						else if(getDefinedInstance().getFinancialInstitutions().contains(getFinancialInstitutionHome().getDefinedInstance()) && getFinancialInstitutionHome() != null)
						{
							getDefinedInstance().getFinancialInstitutions().remove(getFinancialInstitutionHome().getDefinedInstance());
							update();
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Relacion eliminada exitosamente.", "Relacion eliminada"));
						}
						else
						{
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "La entidad que desea desvincular no esta asociada.", "Duplicidad de datos"));
						}
					}
				}
			}
		}
	}

	@Override
	public BusinessProcess getInstance()
	{
		return super.getInstance();
	}
	
	public BusinessProcess getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
	
	@Override
	public String remove()
	{
		//1. buscar la existencia de tipos de reporte tipos de entidades, y entidades asociadas al reporte
		if(getInstance().getFinancialInstitutions().size() > 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar el proceso de negocio. Tiene entidades financieras asociadas", "Entidades financieras asociadas"));
			return "fail";
		}
		else if(getInstance().getFinancialInstitutionTypes().size() > 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar el proceso de negocio. Tiene tipos de entidades financieras asociadas", "Tipos de entidades financieras asociadas"));
			return "fail";
		}
		else if(getInstance().getReportTypes().size() > 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar el proceso de negocio. Tiene tipos de reporte asociados", "Tipos de reporte asociados"));
			return "fail";
		}
		else
		{
			return super.remove();
		}
	}
	//---------------------------------------------------------------//
	// Associated objects return methods
	//---------------------------------------------------------------//

	public List<ReportType> getReportTypes()
	{
		return getInstance() == null ? null : new ArrayList<ReportType>(getInstance().getReportTypes());
	}
	
	public List<FinancialInstitutionType> getFinancialInstitutionTypes()
	{
		return getInstance() == null ? null : new ArrayList<FinancialInstitutionType>(getInstance().getFinancialInstitutionTypes());
	}
	
	public List<FinancialInstitution> getFinancialInstitutions()
	{
		return getInstance() == null ? null : new ArrayList<FinancialInstitution>(getInstance().getFinancialInstitutions());
	}
	
	public FinancialInstitutionTypeHome getFinancialInstitutionTypeHome()
	{
		return financialInstitutionTypeHome;
	}

	public void setFinancialInstitutionTypeHome(FinancialInstitutionTypeHome financialInstitutionTypeHome)
	{
		this.financialInstitutionTypeHome = financialInstitutionTypeHome;
	}
	
	public FinancialInstitutionHome getFinancialInstitutionHome() 
	{
		return financialInstitutionHome;
	}

	public void setFinancialInstitutionHome(FinancialInstitutionHome financialInstitutionHome) 
	{
		this.financialInstitutionHome = financialInstitutionHome;
	}
}