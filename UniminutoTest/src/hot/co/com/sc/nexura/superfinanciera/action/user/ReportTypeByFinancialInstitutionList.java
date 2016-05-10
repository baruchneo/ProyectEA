package co.com.sc.nexura.superfinanciera.action.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.ReportType;

@Name("reportTypeByFinancialInstitutionList")
public class ReportTypeByFinancialInstitutionList extends EntityQuery<ReportType>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select distinct reportType from ReportType reportType INNER JOIN reportType.financialInstitutions financialInstitutions";
	
	private static final String[] RESTRICTIONS = 
	{
		"reportType.businessProcess.id = #{reportTypeByFinancialInstitutionList.reportType.businessProcess.id} ", 
		"lower(financialInstitutions.code) like lower(#{reportTypeByFinancialInstitutionList.financialInstitution.code})",
		"reportType.id = #{reportTypeByFinancialInstitutionList.reportType.id} ",
	};
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private ReportType reportType = new ReportType();
	
	private FinancialInstitution financialInstitution = new FinancialInstitution();
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public ReportTypeByFinancialInstitutionList()
	{
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public ReportType getReportType()
	{
		return reportType;
	}

	public FinancialInstitution getFinancialInstitution()
	{
		return financialInstitution;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(ReportType reportType)
	{
		this.reportType = reportType;
	}
	

	/**
	 * @param financialInstitution the financialInstitution to set
	 */
	public void setFinancialInstitution(FinancialInstitution financialInstitution)
	{
		this.financialInstitution = financialInstitution;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<ReportType> getResultList()
	{
		if (reportType.getBusinessProcess().getId() != null)
		{
			return super.getResultList();
		}
		else
		{
			return new ArrayList<ReportType>();
		}
	}
	
	public void loadNewItems(ValueChangeEvent event)
	{
		Long id = (Long) event.getNewValue();
		this.reportType.getBusinessProcess().setId(id);
	}
}
