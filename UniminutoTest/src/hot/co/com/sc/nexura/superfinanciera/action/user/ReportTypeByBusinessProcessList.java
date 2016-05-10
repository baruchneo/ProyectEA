package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Arrays;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.action.admin.BusinessProcessList;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitutionType;
import co.com.sc.nexura.superfinanciera.model.ReportType;

@Name("reportTypeByBusinessProcessList")
public class ReportTypeByBusinessProcessList extends EntityQuery<ReportType>
{

	@In(create=true)
	BusinessProcessList businessProcessList;
	
	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select distinct reportType from ReportType reportType LEFT OUTER JOIN reportType.financialInstitutions finantialInstitutions LEFT OUTER JOIN reportType.financialInstitutionTypes financialInstitutionTypes";
	
	private static final String[] RESTRICTIONS = 
	{
		"reportType.businessProcess.id = #{reportTypeByBusinessProcessList.reportType.businessProcess.id} ", 
		"lower(financialInstitutions.code) like lower(#{reportTypeByBusinessProcessList.financialInstitution.code})",
		"reportType.id = #{reportTypeByBusinessProcessList.reportType.id} ",
	};
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private ReportType reportType = new ReportType();
	
	private FinancialInstitution financialInstitution = new FinancialInstitution();
	
	private FinancialInstitutionType financialInstitutionType = new FinancialInstitutionType(); 
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public ReportTypeByBusinessProcessList()
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
	
	/**
	 * @return the financialInstitutionType
	 */
	public FinancialInstitutionType getFinancialInstitutionType()
	{
		return financialInstitutionType;
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
	
	/**
	 * @param financialInstitutionType the financialInstitutionType to set
	 */
	public void setFinancialInstitutionType(FinancialInstitutionType financialInstitutionType)
	{
		this.financialInstitutionType = financialInstitutionType;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<ReportType> getResultList()
	{
		return super.getResultList();
	}
	
	public List<ReportType> getResultListByBusinessProcess()
	{
		return super.getResultList();
	}
	
	public void loadNewItems(ValueChangeEvent event)
	{
		Long id = (Long) event.getNewValue();
		this.reportType.getBusinessProcess().setId(id);
	}
}
