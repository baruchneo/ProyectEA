package co.com.sc.nexura.superfinanciera.action.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.sc.nexura.superfinanciera.action.admin.SendingHome;
import co.com.sc.nexura.superfinanciera.action.admin.SendingList;
import co.com.sc.nexura.superfinanciera.action.admin.StateHome;
import co.com.sc.nexura.superfinanciera.action.admin.StateList;
import co.com.sc.nexura.superfinanciera.action.security.AuthenticatorBean;
import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.Sending;
import co.com.sc.nexura.superfinanciera.model.State;
import co.com.sc.nexura.superfinanciera.model.StateEnum;
import co.com.sc.utils.TransferFileSCP;

@Name("readerResponse")
public class ReaderResponse
{

	// ---------------------------------------------------------------//
	// Class attributes
	// ---------------------------------------------------------------//

	@In(create = true)
	SendingList sendingList;
	
	@In(create = true)
	SendingHome sendingHome;

	@In(create = true)
	StateList stateList;
	
	@In(create = true)
	RetrySending retrySending;
	
	AuthenticatorBean authenticator;
	
	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(ReaderResponse.class);
	
	// ---------------------------------------------------------------//
	// Class constructors methods
	// ---------------------------------------------------------------//
	public ReaderResponse()
	{
		authenticator = (AuthenticatorBean) Contexts.getSessionContext().get("authenticator");
	}

	// ---------------------------------------------------------------//
	// Class business methods
	// ---------------------------------------------------------------//

	public void iteratorSendingInProcess()
	{

		
		for (Sending sending : getSendingInProcess())
		{
			
			BusinessProcess bProcess;
			if(sending.getReportType() != null)
				bProcess = sending.getReportType().getBusinessProcess();
			else
				bProcess = sending.getBusinessProcess();
			
			TransferFileSCP transferFileSCP = new TransferFileSCP(bProcess.getScpMachine(), bProcess.getRemotePort(), bProcess.getScpUser(), bProcess.getScpPassword());
			
			StateEnum stateValidationResult;
			if (sending.getSendingForValidation())
			{
				stateValidationResult = readingResponse(sending, bProcess.getResponseValidationPath(), bProcess, transferFileSCP);
			}
			else
			{
				stateValidationResult = readingResponse(sending, bProcess.getResponseFinalPath(), bProcess, transferFileSCP);
			}
			
			//
			// Get the state according to the response
			State newState = null;
			
			stateList.getState().setName(stateValidationResult.getName());
			List<State> stateResult = stateList.getResultList();
			if (stateResult.size() > 0)
			{
				newState = stateResult.get(0);
			}
			
			if (newState == null)
			{
				_LOGGER.error("No existe un estado de procesamiento con exito o con errores con los siguientes nombres: {}, {}", StateEnum.INFORMACION_PROCESADA_EXITO.getName(), StateEnum.INFORMACION_PROCESADA_ERRORES.getName());
			}
			else
			{
				if(stateValidationResult.equals(StateEnum.INFORMACION_PROCESADA_EXITO) || stateValidationResult.equals(StateEnum.INFORMACION_PROCESADA_ERRORES))
				{
					sending.setState(newState);
					
					StateHome stateHome = (StateHome) Component.getInstance(StateHome.class,Boolean.TRUE);
					stateHome.setId(newState.getId());
					
					sendingHome.clearInstance();
					sendingHome.setInstance(sending);
					sendingHome.setStateHome(stateHome);
					sendingHome.update();
				
					transferFileSCP.removeFile(sending.getTempSignedFilesPath(),Boolean.FALSE);
					transferFileSCP.removeFile(sending.getTempSignedNotesPath(),Boolean.FALSE);
					transferFileSCP.removeFile(sending.getTempUnSignedFilesPath(),Boolean.FALSE);
					transferFileSCP.removeFile(sending.getTempUnSignedNotesPath(),Boolean.FALSE);
					transferFileSCP.removeFile(sending.getTempValidationPath(),Boolean.FALSE);
				}
				else
				{
					retrySending.checkSendingRetry(sending, bProcess);
				}
				
			}
			
		}
	}

	private StateEnum readingResponse(Sending sending,String responsePath,BusinessProcess bProcess,TransferFileSCP transferFileSCP)
	{
		BufferedReader br = null;
		String responsePathLocal = null;
		String nameFile = null;
		String successPattern;
		Pattern regex ;
		
		StateEnum stateResult = StateEnum.INFORMACION_EN_PROCESO;

		if(sending.getSendingForValidation())
		{
			nameFile = TransferFileSCP.getNameFile(sending.getResponseValidationPath());
			responsePathLocal = TransferFileSCP.getPathFile(sending.getResponseValidationPath());
			successPattern = bProcess.getSuccessResPatternValidat()==null?"":bProcess.getSuccessResPatternValidat();
		}
		else
		{
			nameFile = TransferFileSCP.getNameFile(sending.getResponseFinalPath());
			responsePathLocal = TransferFileSCP.getPathFile(sending.getResponseFinalPath());
			successPattern = bProcess.getSuccessResponsePattern();
		}
		
		regex = Pattern.compile(successPattern, Pattern.DOTALL);
		
		if(bProcess.getRemoteResponseRead())
		{
			String fileContentString = transferFileSCP.readingRemotefile(responsePath+"//"+nameFile,responsePathLocal+"//"+nameFile);
			
			if(fileContentString == null)
			{
				_LOGGER.info("No se obtuvo resultado del archivo {}", responsePath);
			}
			else
			{
				try
				{
					Matcher regexMatcher = regex.matcher(fileContentString);
					
					if (regexMatcher.find()) 
					{
						stateResult = StateEnum.INFORMACION_PROCESADA_EXITO;
					} 
					else
					{
						stateResult = StateEnum.INFORMACION_PROCESADA_ERRORES;
					}
				}
				catch (Exception e)
				{
					_LOGGER.error("Error al momento de analizar la respuesta de procesamiento de informacion. Expresion regular {} . Respuesta {}", successPattern, fileContentString);
				}
				
			}
		}
		else 
		{
			try
			{
				br = new BufferedReader(new FileReader(responsePath+"//"+nameFile));
	
				String line;
				stateResult = StateEnum.INFORMACION_PROCESADA_ERRORES;
				while ((line = br.readLine()) != null)
				{					
					if(line.matches(successPattern) || line.indexOf(successPattern) != -1 || regex.matcher(line).find())
					{
						stateResult = StateEnum.INFORMACION_PROCESADA_EXITO;
						break;
					}						
				}
				
				br.close();
				
				File fileOriginal = new File(responsePath+"//"+nameFile);
				File fileDestination = new File(responsePathLocal+"//"+nameFile);
				
				if (fileOriginal.renameTo(fileDestination))
				{
					fileOriginal.delete();
				}
				else
				{
					if (!renameFiles(responsePath+"//"+nameFile, responsePathLocal+"//"+nameFile))
					{
						throw new Exception("No fue posible mover el archiv de respuesta");
					}
				}
				
			}
			catch (Exception e)
			{
				_LOGGER.error("Problemas al momento de manejar la respuesta {} ", e.getMessage());
				_LOGGER.error("Respuesta origen {}", responsePath+"//"+nameFile);
				_LOGGER.error("Respuesta destino {}", responsePathLocal+"//"+nameFile);
				
			}
		}
		return stateResult;
	}
	
	/**
	 * @param oldName file to move
	 * @param newName target file path
	 * @return true if the file could be moved
	 */
	private boolean renameFiles(String oldName, String newName)
	{
		InputStream in = null;
		OutputStream out = null;
	    
		try
		{
			in = new FileInputStream(oldName);
			out = new FileOutputStream(newName); 

			byte[] buffer = new byte[1024];
			int length;
	        
			while ((length = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, length);
			}
	        
			return true;
		}
		catch (Exception e)
		{
			_LOGGER.error("No fue posible mover el archivo origen {}", oldName);
			_LOGGER.error("Al directorio destino {}", newName);
			return false;
		}
		finally
		{
			try
			{
				if (in != null )
				{
					in.close();
				}
		    	
				if (out != null)
				{
					out.flush();
					out.close();
				}
			}
			catch (Exception e){}
		}
	}
	
	public List<Sending> getSendingInProcess()
	{
		sendingList.getSending().getState().setName(StateEnum.INFORMACION_EN_PROCESO.getName());
		return sendingList.getResultList();
	}
}
