package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Rol;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("rolList")
public class RolList extends EntityQuery<Rol>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select rol from Rol rol";

	private static final String[] RESTRICTIONS = 
	{
		"lower(rol.nombreRol) like lower(concat('%', concat(#{rolList.rol.nombreRol},'%')))",
		"rol.id = #{rolList.rol.id} ",
	};
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private Rol rol = new Rol();
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public RolList()
	{
		setOrderColumn("rol.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public Rol getRol()
	{
		return rol;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param rol the rol to set
	 */
	public void setRol(Rol rol)
	{
		this.rol = rol;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<Rol> getResultList()
	{
		return super.getResultList();
	}
}
