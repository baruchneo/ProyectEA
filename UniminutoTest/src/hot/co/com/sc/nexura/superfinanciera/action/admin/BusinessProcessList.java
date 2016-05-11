package co.com.sc.nexura.superfinanciera.action.admin;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

//import co.com.sc.nexura.superfinanciera.model.BusinessProcess;

@Name("businessProcessList")
public class BusinessProcessList //extends EntityQuery<BusinessProcess>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	/*private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select businessProcess from BusinessProcess businessProcess";

	private static final String[] RESTRICTIONS = 
	{
		"lower(businessProcess.name) like lower(concat('%', concat(#{businessProcessList.businessProcess.name},'%')))",
		"businessProcess.id = #{businessProcessList.businessProcess.id} ",
		"businessProcess.state = #{businessProcessList.businessProcess.state}",
		"businessProcess.digitalSignatureRequired = #{businessProcessList.businessProcess.digitalSignatureRequired}",
	};
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private BusinessProcess businessProcess = new BusinessProcess();
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public BusinessProcessList()
	{
		setOrderColumn("businessProcess.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public BusinessProcess getBusinessProcess()
	{
		return businessProcess;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param businessProcess the businessProcess to set
	 */
	/*public void setBusinessProcess(BusinessProcess businessProcess)
	{
		this.businessProcess = businessProcess;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<BusinessProcess> getResultList()
	{
		return super.getResultList();
	}*/
}
