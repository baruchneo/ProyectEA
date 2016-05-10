package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Arrays;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.model.FinancialInstitutionType;
import co.com.sc.nexura.superfinanciera.model.ReportType;

@Name("reportTypeByFinancialInstitutionTypeList")
public class ReportTypeByFinancialInstitutionTypeList extends EntityQuery<ReportType>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select distinct reportType from ReportType reportType INNER JOIN reportType.financialInstitutionTypes financialInstitutionTypes";

	private static final String[] RESTRICTIONS = 
	{
		"reportType.businessProcess.id = #{reportTypeByFinancialInstitutionTypeCodeList.reportType.businessProcess.id} ", 
		"lower(financialInstitutionTypes.code) like lower(#{reportTypeByFinancialInstitutionTypeCodeList.financialInstitutionType.code})",
		"reportType.id = #{reportTypeByFinancialInstitutionTypeCodeList.reportType.id} ",
	};
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private ReportType reportType = new ReportType();
	
	private FinancialInstitutionType financialInstitutionType = new FinancialInstitutionType();
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public ReportTypeByFinancialInstitutionTypeList()
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
	
	public void loadNewItems(ValueChangeEvent event)
	{
		Long id = (Long) event.getNewValue();
		this.reportType.getBusinessProcess().setId(id);
	}
}
