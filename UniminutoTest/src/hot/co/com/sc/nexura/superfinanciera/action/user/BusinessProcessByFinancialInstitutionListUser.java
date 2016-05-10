package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.model.BusinessProcess;

@Name("businessProcessByFinancialInstitutionListUser")
public class BusinessProcessByFinancialInstitutionListUser extends EntityQuery <BusinessProcess>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select distinct businessProcess from BusinessProcess businessProcess INNER JOIN businessProcess.reportTypes reportTypes INNER JOIN reportTypes.financialInstitutionTypes financialInstitutionTypes INNER JOIN financialInstitutionTypes.financialInstitutions financialInstitutions";

	private static final String[] RESTRICTIONS = 
	{
		"lower(financialInstitutions.code) like lower(#{authenticator.financialInstitutionCode})",
		"lower(financialInstitutionTypes.code) like lower(#{authenticator.financialInstitutionTypeCode})",
	};
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public BusinessProcessByFinancialInstitutionListUser()
	{
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<BusinessProcess> getResultList()
	{
		return super.getResultList();
	}
}
