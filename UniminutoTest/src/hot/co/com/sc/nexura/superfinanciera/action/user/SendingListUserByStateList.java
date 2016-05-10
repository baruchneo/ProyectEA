package co.com.sc.nexura.superfinanciera.action.user;

import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.model.State;

@Name("sendingListUserByStateList")
public class SendingListUserByStateList extends EntityQuery <State>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select state from State state ";
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public SendingListUserByStateList()
	{
		setEjbql(EJBQL);
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<State> getResultList()
	{
		return super.getResultList();
	}
}
