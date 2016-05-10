package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.action.security.AuthenticatorBean;
import co.com.sc.nexura.superfinanciera.model.BusinessCodeEntity;

@Name("businessCodeList")
public class BusinessCodeList extends EntityQuery<BusinessCodeEntity>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select businessCodeEntity from BusinessCodeEntity businessCodeEntity";

	private static final String[] RESTRICTIONS = 
	{
		"lower(businessCodeEntity.businessName) like trim(lower(concat('%', concat(#{businessCodeList.businessCodeEntity.businessName},'%'))))",
		"businessCodeEntity.financialInstitutionsId = #{businessCodeList.businessCodeEntity.financialInstitutionsId} ",
		"businessCodeEntity.financialInstitutionTypesId = #{businessCodeList.businessCodeEntity.financialInstitutionTypesId}",
		"businessCodeEntity.businessCode = #{businessCodeList.businessCodeEntity.businessCode}",
		"businessCodeEntity.id = #{businessCodeList.businessCodeEntity.id}",
		"businessCodeEntity.legacyTypeCode = #{businessCodeList.businessCodeEntity.legacyTypeCode}",
		"businessCodeEntity.subLegacyTypeCode = #{businessCodeList.businessCodeEntity.subLegacyTypeCode}",
	};
	
	
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private BusinessCodeEntity businessCodeEntity = new BusinessCodeEntity();
	
	AuthenticatorBean authenticator;
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public BusinessCodeList()
	{
		setOrderColumn("businessCodeEntity.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public BusinessCodeEntity getBusinessCodeEntity()
	{
		return businessCodeEntity;
	}

	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	/**
	 * @param businessProcess the businessProcess to set
	 */
	public void setBusinessCodeEntity(BusinessCodeEntity businessCodeEntity)
	{
		this.businessCodeEntity = businessCodeEntity;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<BusinessCodeEntity> getResultList()
	{
		return super.getResultList();
	}
}
