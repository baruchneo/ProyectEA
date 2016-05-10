package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.sc.nexura.superfinanciera.action.admin.SendingHome;
import co.com.sc.nexura.superfinanciera.action.admin.SendingList;
import co.com.sc.nexura.superfinanciera.action.admin.StateHome;
import co.com.sc.nexura.superfinanciera.action.admin.StateList;
import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.Sending;
import co.com.sc.nexura.superfinanciera.model.State;
import co.com.sc.nexura.superfinanciera.model.StateEnum;
import co.com.sc.utils.TransferFileSCP;

@Name("retrySending")
public class RetrySending{
	

	// ---------------------------------------------------------------//
	// Class attributes
	// ---------------------------------------------------------------//
	@In(create = true)
	SendingList sendingList;
	
	@In(create = true)
	SendingHome sendingHome;

	@In(create = true)
	StateList stateList;
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(RetrySending.class);
	
	
	// ---------------------------------------------------------------//
	// Class business methods
	// ---------------------------------------------------------------//
	
	
	public void iteratorRetrySendingInProcess()
	{
		for (Sending sending : getSendingInProcess())
		{
			BusinessProcess bProcess = sending.getReportType().getBusinessProcess();
			checkSendingRetry(sending, bProcess);
		}

	}
	
	
	public void checkSendingRetry(Sending sending, BusinessProcess bProcess)
	{
		
		if(bProcess.getRetryNumber() > sending.getCurrentRetry())
		{
			
			Calendar calendarRetryDate = Calendar.getInstance();
			calendarRetryDate.setTime(sending.getCurrentDate());
			calendarRetryDate.add(Calendar.MINUTE, bProcess.getRetryTime());
			if(calendarRetryDate.getTime().before(new Date()))
			{
				runRetry(sending,bProcess);
			}
		}
		else
		{
			TransferFileSCP transferFileSCP = new TransferFileSCP(bProcess.getScpMachine(), bProcess.getRemotePort(), bProcess.getScpUser(), bProcess.getScpPassword());
			transferFileSCP.removeFile(sending.getTempSignedFilesPath(),Boolean.FALSE);
			transferFileSCP.removeFile(sending.getTempSignedNotesPath(),Boolean.FALSE);
			transferFileSCP.removeFile(sending.getTempUnSignedFilesPath(),Boolean.FALSE);
			transferFileSCP.removeFile(sending.getTempUnSignedNotesPath(),Boolean.FALSE);
			transferFileSCP.removeFile(sending.getTempValidationPath(),Boolean.FALSE);
		}
		
	}

	private void runRetry(Sending sending, BusinessProcess bProcess)
	{
		String nameFile="";
		String pathFile="";
		
		String remoteMachine;
		int remotePort;
		String remoteUser;
		String remotePassword;
		Boolean remoteCopy = Boolean.FALSE;
		
		Boolean requiredSignature = bProcess.getDigitalSignatureRequired() && !sending.getFinancialInstitution().getDigitalSignatureDisabled();
		
		if(bProcess.getRemoteAres() && requiredSignature){
			// get server setting remote signature
			remoteCopy = bProcess.getRemoteAres();
			remoteMachine = bProcess.getScpAresMachine();
			remotePort = bProcess.getRemoteAresPort();
			remoteUser = bProcess.getScpAresUser();
			remotePassword = bProcess.getScpAresPassword();
		}else{
			// get server setting remote UnSignature
			remoteCopy = bProcess.getRemoteReportCopy();
			remoteMachine = bProcess.getScpMachine();
			remotePort = bProcess.getRemotePort();
			remoteUser = bProcess.getScpUser();
			remotePassword = bProcess.getScpPassword();
		}
		
		
		
		if(sending.getTempSignedFilesPath()!=null && !sending.getTempSignedFilesPath().equals(""))
		{
			nameFile = TransferFileSCP.getNameFile(sending.getTempSignedFilesPath());
			pathFile = TransferFileSCP.getPathFile(sending.getTempSignedFilesPath());
			TransferFileSCP.copyFile(bProcess.getSignedFilesPath(), nameFile ,null, pathFile, remoteCopy, remoteMachine, remotePort, remoteUser, remotePassword);
			
		}
		if(sending.getTempSignedNotesPath()!=null && !sending.getTempSignedNotesPath().endsWith(""))
		{
			nameFile = TransferFileSCP.getNameFile(sending.getTempSignedNotesPath());
			pathFile = TransferFileSCP.getPathFile(sending.getTempSignedNotesPath());
			TransferFileSCP.copyFile(bProcess.getSignedNotesPath(), nameFile ,null, pathFile, remoteCopy, remoteMachine, remotePort, remoteUser, remotePassword);
			
		}
		if(sending.getTempUnSignedFilesPath()!=null && !sending.getTempUnSignedFilesPath().equals(""))
		{
			nameFile = TransferFileSCP.getNameFile(sending.getTempUnSignedFilesPath());
			pathFile = TransferFileSCP.getPathFile(sending.getTempUnSignedFilesPath());
			TransferFileSCP.copyFile(bProcess.getUnSignedFilesPath(), nameFile ,null, pathFile, remoteCopy, remoteMachine, remotePort, remoteUser, remotePassword);
		}
		if(sending.getTempUnSignedNotesPath()!=null && !sending.getTempUnSignedNotesPath().equals(""))
		{
			nameFile = TransferFileSCP.getNameFile(sending.getTempUnSignedNotesPath());
			pathFile = TransferFileSCP.getPathFile(sending.getTempUnSignedNotesPath());
			TransferFileSCP.copyFile(bProcess.getUnSignedNotesPath(), nameFile ,null, pathFile, remoteCopy, remoteMachine, remotePort, remoteUser, remotePassword);
			
		}
		if(sending.getTempValidationPath()!=null && !sending.getTempValidationPath().equals(""))
		{
			nameFile = TransferFileSCP.getNameFile(sending.getTempValidationPath());
			pathFile = TransferFileSCP.getPathFile(sending.getTempValidationPath());
			TransferFileSCP.copyFile(bProcess.getValidationPath(), nameFile ,null, pathFile, remoteCopy, remoteMachine, remotePort, remoteUser, remotePassword);
		}
		
		UpdateSendingRetry(sending,bProcess.getRetryNumber());
		
	}
	
	private void UpdateSendingRetry(Sending sending, int maxRetry)
	{
		int currentRetry = sending.getCurrentRetry()+1;
		sendingHome.clearInstance();
		sending.setCurrentRetry(currentRetry);
		sending.setCurrentDate(new Date());
		
		if(currentRetry >= maxRetry )
		{
			State newState = null;
			stateList.getState().setName(StateEnum.INFORMACION_NO_PROCESADA.getName());
			List<State> stateResult = stateList.getResultList();
			
			if (stateResult.size() > 0)
			{
				newState = stateResult.get(0);
			}
			
			if (newState == null)
			{
				_LOGGER.error("No existe un estado de procesamiento : {}", StateEnum.INFORMACION_NO_PROCESADA.getName());
			}
			else
			{
				sending.setState(newState);
				
				StateHome stateHome = (StateHome) Component.getInstance(StateHome.class,Boolean.TRUE);
				stateHome.setId(newState.getId());
				
				sendingHome.setStateHome(stateHome);
			}
		}
		else
		{
			StateHome stateHome = (StateHome) Component.getInstance(StateHome.class,Boolean.TRUE);
			stateHome.setId(sending.getState().getId());
			
			sendingHome.setStateHome(stateHome);
		}
		
		
		sendingHome.setInstance(sending);
		sendingHome.update();
	}
	
	public List<Sending> getSendingInProcess()
	{
		sendingList.getSending().getState().setName(StateEnum.INFORMACION_EN_PROCESO.getName());
		return sendingList.getResultList();
	}

}
