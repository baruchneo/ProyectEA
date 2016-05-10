package co.com.sc.nexura.superfinanciera.action.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;

import co.com.sc.nexura.superfinanciera.action.security.AuthenticatorBean;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.Sending;

@Name("sendingListUser")
public class SendingListUser extends EntityQuery<Sending>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select sending from Sending sending";

	private static final String[] RESTRICTIONS = 
	{
		"sending.id = #{sendingListUser.sending.id} ", 
		"sending.reportType.id = #{sendingListUser.sending.reportType.id} ",
		"sending.state.id = #{sendingListUser.sending.state.id} ", 
		"sending.cutOffDate = #{sendingListUser.sending.cutOffDate} ",
		"sending.state.name in (#{sendingListUser.states})",
		"sending.sendingForValidation = #{sendingListUser.sending.sendingForValidation} ",
		"sending.businessProcess.id = #{sendingListUser.sending.businessProcess.id} ",
		"sending.financialInstitution.id = #{sendingListUser.sending.financialInstitution.id}",
	};
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private Sending sending = new Sending();
	
	private List<String> states = new ArrayList<String>();
	
	@In
	AuthenticatorBean authenticator;
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public SendingListUser()
	{
		setEjbql(EJBQL);
		//setMaxResults(25);
		setOrderColumn("sending.id");
        setOrderDirection("desc");
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public Sending getSending()
	{
		return sending;
	}
	/**
	 * @return the states
	 */
	public List<String> getStates()
	{
		return states;
	}
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param states the states to set
	 */
	public void setStates(List<String> states)
	{
		this.states = states;
	}

	/**
	 * @param sending the sending to set
	 */
	public void setSending(Sending sending)
	{
		this.sending = sending;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<Sending> getResultList()
	{
		authenticator = (AuthenticatorBean) Contexts.getSessionContext().get("authenticator");
		FinancialInstitution financialInstitution = authenticator.getFinancialInstitution();
		sending.setFinancialInstitution(financialInstitution);
		
		List<Sending> listSending = new ArrayList<Sending>();
		
		listSending = super.getResultList();
		
		if(listSending.isEmpty())
		{
			sending.getBusinessProcess().setId(null);
			listSending = super.getResultList();
		}
		
		return listSending;
	}
}
