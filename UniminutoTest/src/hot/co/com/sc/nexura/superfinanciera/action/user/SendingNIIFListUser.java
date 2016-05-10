package co.com.sc.nexura.superfinanciera.action.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.sc.nexura.superfinanciera.action.admin.FinancialInstitutionList;
import co.com.sc.nexura.superfinanciera.action.admin.FinancialInstitutionTypeList;
import co.com.sc.nexura.superfinanciera.action.security.AuthenticatorBean;
import co.com.sc.nexura.superfinanciera.model.BusinessCodeEntity;
import co.com.sc.nexura.superfinanciera.model.BusinessProcessNIIFEnum;
import co.com.sc.nexura.superfinanciera.model.ConsolidatedSendingByNIIFReportType;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.NIIFReport;
import co.com.sc.nexura.superfinanciera.model.ReportTypeNIIFEnum;
import co.com.sc.nexura.superfinanciera.model.Sending;
import co.com.sc.nexura.superfinanciera.model.SendingUserReportNIIF;
import co.com.sc.nexura.superfinanciera.model.StateDisplayNIIFEnum;

@Name("sendingNIIFListUser")
public class SendingNIIFListUser extends EntityQuery<Sending>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(SendingNIIFListUser.class);
	
	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJBQL = "select sending from Sending sending inner join sending.reportType ReportType inner join reportType.businessProcess BusinessProcess where sending.idBusinessCode is not null";

	private static final String[] RESTRICTIONS = 
	{
		"lower(sending.reportType.name) like lower(concat('%', concat(#{sendingNIIFListUser.sending.reportType.name},'%')))",
		"sending.reportType.id = #{sendingNIIFListUser.sending.reportType.id}",
		"sending.financialInstitution.id = #{sendingNIIFsendingNIIFListUserList.sending.financialInstitution.id} ",
		"lower(sending.financialInstitution.name) like lower(concat('%', concat(#{sendingNIIFListUser.sending.financialInstitution.name},'%')))",
		"sending.cutOffDate = #{sendingNIIFListUser.sending.cutOffDate} ",
	};
	
	private static final String ORDERCOLUMNS = 
			"sending.financialInstitution.id desc, " +
			"sending.idBusinessCode desc, " +
			"sending.cutOffDate desc, " +
			"sending.reportType.name desc, " +
			"sending.businessProcess.id desc, " +
			"sending.currentDate desc";
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	private Sending sending = new Sending();
	
	private List<String> states = new ArrayList<String>();
	
	@In
	AuthenticatorBean authenticator;
	
	@In(create=true) 
	BusinessCodeList businessCodeList;
	
	@In(create=true)
	FinancialInstitutionTypeList financialInstitutionTypeList;
	
	@In(create=true)
	FinancialInstitutionList  financialInstitutionList;
	
	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	public SendingNIIFListUser()
	{
		setEjbql(EJBQL);
		setOrder(ORDERCOLUMNS);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
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
		
	
	public List<SendingUserReportNIIF> getResultListNIIF()
	{
		authenticator = (AuthenticatorBean) Contexts.getSessionContext().get("authenticator");
		FinancialInstitution financialInstitution = authenticator.getFinancialInstitution();
		sending.setFinancialInstitution(financialInstitution);
		
		List<SendingUserReportNIIF> sendingNIIFList = new ArrayList<SendingUserReportNIIF>();
		List<Sending> sendings = this.getResultList();
		
		Integer beforeBusinessCode = new Integer(-1);
		Date beforeCutOffDate = new Date();
		
		Long beforeBusinessProcessId = new Long(-1);
		Long beforeReportTypeId  = new Long(-1);
		
		SendingUserReportNIIF currentSendingNIIF = null;
		
		for ( int i = 0 ; i < sendings.size() ; i++)
		{
			Sending currentSending = sendings.get(i);
			Integer currentBusinessCode = currentSending.getIdBusinessCode();
			Date currentCutOffDate = currentSending.getCutOffDate();
			
			Long currentBusinessProcessId = currentSending.getBusinessProcess().getId();
			Long currentReportTypeId =  currentSending.getReportType().getId();
			
			if (currentBusinessCode.equals(beforeBusinessCode) && currentCutOffDate.equals(beforeCutOffDate))
			{
				if (!currentBusinessProcessId.equals(beforeBusinessProcessId) || !currentReportTypeId.equals(beforeReportTypeId))
				{
					setupSendingNIIF(currentSendingNIIF , currentSending);
					
					beforeBusinessProcessId = currentBusinessProcessId;
					beforeReportTypeId = currentReportTypeId;
				}
				
			}
			else
			{
				BusinessCodeEntity currentBusinessCodeEntity = getBusinessCodeEntity(currentSending.getIdBusinessCode());
				currentSendingNIIF = new SendingUserReportNIIF(currentSending.getCutOffDate(), currentBusinessCodeEntity.getBusinessCode(), currentBusinessCodeEntity.getBusinessName(), currentSending.getFinancialInstitution().getName());
				
				setupSendingNIIF(currentSendingNIIF , currentSending);
				
				sendingNIIFList.add(currentSendingNIIF);
				beforeBusinessCode = currentBusinessCode;
				beforeCutOffDate = currentCutOffDate;
				
				beforeBusinessProcessId = currentBusinessProcessId;
				beforeReportTypeId = currentReportTypeId;
			}
		}
		
		return sendingNIIFList;
	}
	
	/**
	 * @param code current business code
	 * @return business code match list
	 */
	public List<BusinessCodeEntity> loadBusinessCodeEntityList(String nombre)
	{
		if(nombre != null && !nombre.trim().equals(""))
        {	
			businessCodeList.getBusinessCodeEntity().setBusinessName(nombre);
        	return businessCodeList.getResultList();
        }
        else
        {
        	return new ArrayList<BusinessCodeEntity>();
        }
	}
	
	@Override
	public List<Sending> getResultList()
	{
		return super.getResultList();
	}
	
	//---------------------------------------------------------------//
	// Class private helper methods
	//---------------------------------------------------------------//

	/**
	 * @param id business code to find
	 * @return business code associated with the given ID
	 */
	private BusinessCodeEntity getBusinessCodeEntity(Integer id)
	{
		businessCodeList.getBusinessCodeEntity().setId(id);
		List<BusinessCodeEntity> result = businessCodeList.getResultList();
		
		if (result.size() > 0)
		{ 
			return result.get(0);
		}
		else
		{
			return new BusinessCodeEntity();
		}
	}
	
	
	
	/**
	 * Setups the current sending NIIF
	 * 
	 * @param currentSendingNIIF current sending NIIF
	 * @param currentSending current source sending 
	 */
	private void setupSendingNIIF(SendingUserReportNIIF currentSendingNIIF, Sending currentSending)
	{
		String reportTypeName = currentSending.getReportType().getName();
		
		if (reportTypeName.toLowerCase().contains(ReportTypeNIIFEnum.I_I.getName().toLowerCase()))
		{
			setupSendingNIIFHelper(currentSendingNIIF.getFormatIISendingsNIIF(), currentSending);
		}
		else if (reportTypeName.toLowerCase().contains(ReportTypeNIIFEnum.I_C.getName().toLowerCase()))
		{
			setupSendingNIIFHelper(currentSendingNIIF.getFormatICSendingsNIIF(), currentSending);
		}
		else if (reportTypeName.toLowerCase().contains(ReportTypeNIIFEnum.C_I.getName().toLowerCase()))
		{
			setupSendingNIIFHelper(currentSendingNIIF.getFormatCISendingsNIIF(), currentSending);
		}
		else if (reportTypeName.toLowerCase().contains(ReportTypeNIIFEnum.C_C.getName().toLowerCase()))
		{
			setupSendingNIIFHelper(currentSendingNIIF.getFormatCCSendingsNIIF(), currentSending);
		}
		else
		{
			_LOGGER.info("Tipo de reporte no configurado para el NIIF {}", reportTypeName);
		}
	}

	/**
	 * Setups the current sending NIIF
	 * 
	 * @param format current format NIIF
	 * @param currentSending current sending
	 */
	private void setupSendingNIIFHelper(ConsolidatedSendingByNIIFReportType format, Sending currentSending)
	{
		String businessProcessName = currentSending.getBusinessProcess().getName();
		
		if (businessProcessName.toLowerCase().contains(BusinessProcessNIIFEnum.XBRL.getName().toLowerCase()))
		{
			setupConsolidateNIIF(format.getXbrl(), currentSending);
		}
		else if (businessProcessName.toLowerCase().contains(BusinessProcessNIIFEnum.EXCEL_ESTADO.getName().toLowerCase()))
		{
			setupConsolidateNIIF(format.getExcel(), currentSending);
		}
		else if (businessProcessName.toLowerCase().contains(BusinessProcessNIIFEnum.EXCEL_NOTAS.getName().toLowerCase()))
		{
			setupConsolidateNIIF(format.getExcelNotas(), currentSending);
		}
		else if (businessProcessName.toLowerCase().contains(BusinessProcessNIIFEnum.PDF.getName().toLowerCase()))
		{
			setupConsolidateNIIF(format.getPdf(), currentSending);
		}
		else
		{
			_LOGGER.info("Tipo de negocio no configurado para el NIIF {}", businessProcessName);
		}
	}

	/**
	 * Setups current consolidated sending
	 * 
	 * @param report current report
	 * @param currentSending current sending
	 */
	private void setupConsolidateNIIF(NIIFReport report, Sending currentSending)
	{	
		report.setTransmissionDate(currentSending.getCurrentDate());
		report.setTransmissionId(currentSending.getId());
		report.setDownloadPath("#");
		
		String currentSendingState = currentSending.getState().getName();
		
		if (currentSendingState.toLowerCase().contains(StateDisplayNIIFEnum.INFORMACION_NO_ENVIADA.getName().toLowerCase()))
		{
			report.setState(StateDisplayNIIFEnum.INFORMACION_NO_ENVIADA);
		}
		else if (currentSendingState.toLowerCase().contains(StateDisplayNIIFEnum.INFORMACION_EN_PROCESO.getName().toLowerCase()))
		{
			report.setState(StateDisplayNIIFEnum.INFORMACION_EN_PROCESO);
		}
		else if (currentSendingState.toLowerCase().contains(StateDisplayNIIFEnum.INFORMACION_PROCESADA_EXITO.getName().toLowerCase()))
		{
			report.setState(StateDisplayNIIFEnum.INFORMACION_PROCESADA_EXITO);
		}
		else if (currentSendingState.toLowerCase().contains(StateDisplayNIIFEnum.INFORMACION_PROCESADA_ERRORES.getName().toLowerCase()))
		{
			report.setState(StateDisplayNIIFEnum.INFORMACION_PROCESADA_ERRORES);
		}
		else if (currentSendingState.toLowerCase().contains(StateDisplayNIIFEnum.INFORMACION_NO_PROCESADA.getName().toLowerCase()))
		{
			report.setState(StateDisplayNIIFEnum.INFORMACION_NO_PROCESADA);
		}
		else
		{
			_LOGGER.info("Tipo de estado no configurado para el NIIF {}", currentSendingState);
		}	
	}
}
