package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.model.BusinessCodeEntity;

@Name("businessCodeControlList")
public class BusinessCodeControlList extends EntityQuery<BusinessCodeEntity>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select businessCodeEntity from BusinessCodeEntity businessCodeEntity where (businessCodeEntity.idTaxonomy IS NOT NULL AND businessCodeEntity.idTaxonomy != 0 ) ";

	private static final String[] RESTRICTIONS = 
	{
		"businessCodeEntity.financialInstitutionsId = #{businessCodeControlList.businessCodeEntity.financialInstitutionsId} ",
		"businessCodeEntity.financialInstitutionTypesId = #{businessCodeControlList.businessCodeEntity.financialInstitutionTypesId}",
		"businessCodeEntity.businessCode like concat(#{businessCodeControlList.businessCodeEntity.businessCode}, '%')",
		"businessCodeEntity.id = #{businessCodeControlList.businessCodeEntity.id}",
		"businessCodeEntity.legacyTypeCode = #{businessCodeControlList.businessCodeEntity.legacyTypeCode}",
		"businessCodeEntity.subLegacyTypeCode = #{businessCodeControlList.businessCodeEntity.subLegacyTypeCode}",
	};
	
	
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private BusinessCodeEntity businessCodeEntity = new BusinessCodeEntity();
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public BusinessCodeControlList()
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
