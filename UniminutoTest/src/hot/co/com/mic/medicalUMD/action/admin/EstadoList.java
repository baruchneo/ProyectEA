package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Estado;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("estadoList")
public class EstadoList extends EntityQuery<Estado>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select estado from Estado estado";

	private static final String[] RESTRICTIONS =
	{
		"lower(estado.nombreEstado) like lower(concat('%', concat(#{estadoList.estado.nombreEstado},'%')))",
		"estado.id = #{estadoList.estado.id} ",
	};

	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	private Estado estado = new Estado();

	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//

	public EstadoList()
	{
		setOrderColumn("estado.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public Estado getEstado()
	{
		return estado;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Estado estado)
	{
		this.estado = estado;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<Estado> getResultList()
	{
		return super.getResultList();
	}
}
