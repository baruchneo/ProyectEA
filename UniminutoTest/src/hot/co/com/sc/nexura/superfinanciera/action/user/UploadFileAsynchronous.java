package co.com.sc.nexura.superfinanciera.action.user;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.sc.nexura.superfinanciera.action.admin.SendingHome;
import co.com.sc.nexura.superfinanciera.action.admin.StateHome;
import co.com.sc.nexura.superfinanciera.action.admin.StateList;
import co.com.sc.nexura.superfinanciera.model.BusinessCodeEntity;
import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.ReportType;
import co.com.sc.nexura.superfinanciera.model.SendTypeEnum;
import co.com.sc.nexura.superfinanciera.model.Sending;
import co.com.sc.nexura.superfinanciera.model.State;
import co.com.sc.nexura.superfinanciera.model.StateEnum;
import co.com.sc.utils.TransferFileSCP;

@Name("uploadFileAsynchronous")
public class UploadFileAsynchronous implements IUploadFileAsynchronous
{
	
	final String _TEMP_SIGNED_FILES_PATH = "signedFiles";
	final String _TEMP_SIGNED_NOTES_PATH = "signedNotes";
	final String _TEMP_UN_SIGNED_FILES_PATH = "unSignedFiles";
	final String _TEMP_UN_SIGNED_NOTES_PATH = "unSignedNotes";
	final String _TEMP_VALIDATION_PATH = "validation";
	
	final String _TEMP_RESPONSE_FINAL_PATH = "final";
	final String _TEMP_RESPONSE_VALIDATION_PATH = "validation";
	
	
	@In(create = true)
	StateList stateList;
	
	@In(create=true) 
	BusinessCodeList businessCodeList;
	
	
	BusinessProcess businessProcess;
	ReportType reportType;
	SendTypeEnum sendTypeSelected;
	FinancialInstitution financialInstitution;
	Date cutOffDate;
	String tmpReportStore;
	String tmpResponseStore;
	Integer idBusinessCode;
	
	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(SendingUploadBean.class);
	
	@Override
	@Asynchronous
	public void processFile(List<File> dataDefinitiveFiles, File dataNotesFile, File dataNotesFile2, BusinessProcess businessProcess, ReportType reportType, SendTypeEnum sendTypeSelected, Date cutOffDate, FinancialInstitution financialInstitution, String tmpReportStore, String tmpResponseStore, Integer idBusinessCode)
	{
		
		this.businessProcess = businessProcess;
		this.reportType = reportType;
		this.sendTypeSelected = sendTypeSelected;
		this.financialInstitution = financialInstitution;
		this.cutOffDate = cutOffDate;
		this.tmpReportStore = tmpReportStore;
		this.tmpResponseStore = tmpResponseStore;
		this.idBusinessCode = idBusinessCode;
		
		for (File definitiveFile : dataDefinitiveFiles)
		{
			processFileSending(definitiveFile, dataNotesFile, dataNotesFile2, businessProcess.getBlockSending()==null?Boolean.FALSE:businessProcess.getBlockSending());
		}
		
	}
	//
	// TODO Manejar el segundo archivo
	private void processFileSending(File definitiveFile, File dataNotesFile,  File dataNotesFile2, Boolean blockSending)
	{
		try
		{
			String remoteMachine = null;
			int remotePort = 0;
			String remoteUser = null;
			String remotePassword = null;
			Boolean remoteCopy = Boolean.FALSE;
			long currentTime = Calendar.getInstance().getTimeInMillis();
			
			//Persist the sent
			Sending sending = new Sending();
			saveSending(sending,blockSending);
			
			//get nomenclature to file
			String nomenclatureReport;
			String nomenclatureResponse;
			if(blockSending)
			{
				nomenclatureReport = businessProcess.getNomenclatureReport();
				nomenclatureResponse = businessProcess.getNomenclatureResponse();
			}
			else
			{
				nomenclatureReport= reportType.getNomenclatureReport();
				nomenclatureResponse = reportType.getNomenclatureResponse();
			}	
			
			String nameReport = generateNameReport(nomenclatureReport, definitiveFile,sending.getId(),blockSending);
			String nameResponse = generateNameReport(nomenclatureResponse, definitiveFile,sending.getId(),blockSending);
			
			_LOGGER.info("Nombre del reporte a actualizar en el envIo : {}", nameReport);
			_LOGGER.info("Nombre de la respuesta a actualizar en el envIo : {}", nameResponse);
			
			switch (sendTypeSelected)
			{
				case ENVIO_VALIDACION:
					
					TransferFileSCP.copyFileLocal(generateReportPath(_TEMP_VALIDATION_PATH,blockSending, currentTime), nameReport, definitiveFile);
					TransferFileSCP.copyFile(businessProcess.getValidationPath(), nameReport, definitiveFile,generateReportPath(_TEMP_VALIDATION_PATH,blockSending, currentTime),businessProcess.getRemoteReportCopy(),businessProcess.getScpMachine(),businessProcess.getRemotePort(),businessProcess.getScpUser(),businessProcess.getScpPassword());
					updateSending(sending, Boolean.TRUE, Boolean.FALSE, null, null, nameReport,nameResponse,blockSending, currentTime);
					break;
	
				case ENVIO_DEFINITIVO:
					String nomenclatureNotes;
					String nameNotes = null;
					String nameNotes2 = null;
					if(!blockSending)
					{
						nomenclatureNotes = reportType.getNomenclatureNotes();
						
						if (nomenclatureNotes != null)
						{
							nameNotes = generateNameReport(nomenclatureNotes, dataNotesFile,sending.getId(),blockSending);
							if(dataNotesFile2 != null)
							{
								nameNotes2 = generateNameReport(nomenclatureNotes, dataNotesFile2,sending.getId(),blockSending);
							}
						}
					}
	
					String definitivePath;
					String notesPath;
					String definitiveTmpPath;
					String notesTmpPath;
					
					Boolean requiredSignature = businessProcess.getDigitalSignatureRequired() && !financialInstitution.getDigitalSignatureDisabled();
	
					if (requiredSignature)
					{
						// get file path Signature
						definitiveTmpPath = generateReportPath(_TEMP_SIGNED_FILES_PATH,blockSending, currentTime);
						notesTmpPath = generateReportPath(_TEMP_SIGNED_NOTES_PATH,blockSending, currentTime);
						definitivePath = businessProcess.getSignedFilesPath();
						notesPath = businessProcess.getSignedNotesPath();
					}
					else
					{
						// get file path UnSignature
						definitiveTmpPath = generateReportPath(_TEMP_UN_SIGNED_FILES_PATH,blockSending, currentTime);
						notesTmpPath = generateReportPath(_TEMP_UN_SIGNED_NOTES_PATH,blockSending, currentTime);
						definitivePath = businessProcess.getUnSignedFilesPath();
						notesPath = businessProcess.getUnSignedNotesPath();
					}
					
					_LOGGER.info("Ruta temporal de almacenamiento del envIo : {}", definitiveTmpPath);
					_LOGGER.info("Ruta temporal de almacenamiento de las notas del envIo : {}", notesTmpPath);
					_LOGGER.info("Ruta de almacenamiento del envIo definitivo : {}", definitivePath);
					_LOGGER.info("Ruta de almacenamiento de las notas del envIo definitivo : {}", notesPath);



					if (businessProcess.getRemoteAres() && requiredSignature) {
						try
						{
							// get server setting remote signature
							remoteCopy = businessProcess.getRemoteAres();
							remoteMachine = businessProcess.getScpAresMachine();
							remotePort = businessProcess.getRemoteAresPort();
							remoteUser = businessProcess.getScpAresUser();
							remotePassword = businessProcess.getScpAresPassword();
						}
						catch(NullPointerException e)
						{
							_LOGGER.error("Se ha producido un error al cargar las propiedades para copiar al servidor de Ares. Actualicelas e intente de nuevo. DescripciOn del error: " + e.getMessage());
							throw new Exception ("Se ha producido un error al cargar las propiedades para copiar al servidor de Ares. Actualicelas e intente de nuevo. DescripciOn del error: " + e.getMessage());
						}
					} else {
						try {
							// get server setting remote UnSignature
							remoteCopy = businessProcess.getRemoteReportCopy();
							remoteMachine = businessProcess.getScpMachine();
							remotePort = businessProcess.getRemotePort();
							remoteUser = businessProcess.getScpUser();
							remotePassword = businessProcess.getScpPassword();
						}
						catch(NullPointerException e)
						{
							_LOGGER.error("Se ha producido un error al cargar las propiedades para copiar al servidor remoto. Actualicelas e intente de nuevo. DescripciOn del error: " + e.getMessage());
							throw new Exception("Se ha producido un error al cargar las propiedades para copiar al servidor remoto. Actualicelas e intente de nuevo. DescripciOn del error: " + e.getMessage());
						}
					}


					_LOGGER.info("Copia remota en actualizaciOn de envIo : {}", remoteCopy);
					_LOGGER.info("MAquina remota en actualizaciOn de envIo : {}", remoteMachine);
					_LOGGER.info("Puerto remoto en actualizaciOn de envIo : {}", remotePort);
					_LOGGER.info("Usuario remoto en actualizaciOn de envIo : {}", remoteUser);
	
					// Copy definitive file
					TransferFileSCP.copyFileLocal(definitiveTmpPath, nameReport,definitiveFile);
					// delete temporal file of TEMP_UPLOADED_FILES_PATH
					definitiveFile.delete();
					
					if (businessProcess.getUnSignedFilesPathTemp() != null && !businessProcess.getUnSignedFilesPath().trim().equals(""))
					{
						TransferFileSCP.copyFile(businessProcess.getUnSignedFilesPathTemp(), nameReport,null,definitiveTmpPath,remoteCopy,remoteMachine,remotePort, remoteUser,remotePassword);
						TransferFileSCP transferFileSCP = new TransferFileSCP(remoteMachine, remotePort, remoteUser, remotePassword);
						transferFileSCP.move(businessProcess.getUnSignedFilesPathTemp() + "/" + nameReport, definitivePath + "/" + nameReport);
					}
					else
					{
						TransferFileSCP.copyFile(definitivePath, nameReport,null,definitiveTmpPath,remoteCopy,remoteMachine,remotePort, remoteUser,remotePassword);
					}
					
					// Copy notes file
					if(dataNotesFile!=null)
					{
						TransferFileSCP.copyFileLocal(notesTmpPath, nameNotes,dataNotesFile);
						// delete temporal note file of TEMP_UPLOADED_FILES_PATH
						dataNotesFile.delete();
						TransferFileSCP.copyFile(notesPath, nameNotes,null,notesTmpPath,remoteCopy,remoteMachine,remotePort,remoteUser,remotePassword);
					}
					else
					{
						nameNotes=null;
					}
					
					// Copy notes file
					if(dataNotesFile2!=null)
					{
						TransferFileSCP.copyFileLocal(notesTmpPath, nameNotes2,dataNotesFile2);
						// delete temporal note file of TEMP_UPLOADED_FILES_PATH
						dataNotesFile2.delete();
						TransferFileSCP.copyFile(notesPath, nameNotes2,null,notesTmpPath,remoteCopy,remoteMachine,remotePort,remoteUser,remotePassword);
					}
					else
					{
						nameNotes2=null;
					}
					
					//Persist the sent
					updateSending(sending,Boolean.FALSE,requiredSignature,nameNotes,nameNotes2,nameReport,nameResponse,blockSending, currentTime);
	
					break;
	
				default:
					break;
			}
		}
		catch (Exception e)
		{
			_LOGGER.error("Se ha producido un error al momento de actualizar la informaciOn de un envio. DescripciOn del error: " + e.getMessage());
		}
	}
	
	/**
	 * Persist the sent
	 * @param sending send data
	 * @param blocking indicates type sending
	 */
	private void saveSending(Sending sending,Boolean blocking)
	{
		
		sending.setCurrentDate(new Date());
		sending.setCurrentRetry(0);
		sending.setCutOffDate(cutOffDate);
		sending.setFinancialInstitution(financialInstitution);
		//include the business code id
		sending.setIdBusinessCode(this.idBusinessCode);
		
		if(blocking)
		{
			sending.setReportType(null);
			sending.setBusinessProcess(businessProcess);
		}
		else{
			sending.setBusinessProcess(null);
			sending.setReportType(reportType);
		}
		
		if(this.idBusinessCode != null)
		{
			sending.setBusinessProcess(businessProcess);
		}
		
		sending.setResendAllowed(Boolean.FALSE);
		sending.setSendingForValidation(sendTypeSelected.equals(SendTypeEnum.ENVIO_VALIDACION));
		// Get the associated state
		stateList.getState().setName(StateEnum.INFORMACION_NO_PROCESADA.getName());
		List<State> statesResult = stateList.getResultList();
		State state = null;
		
		if (statesResult.size() > 0)
		{
			state = statesResult.get(0);
			sending.setState(state);
			SendingHome sendingHome = (SendingHome) Component.getInstance(SendingHome.class,Boolean.TRUE);
			sendingHome.setInstance(sending);
			sendingHome.persist();
			_LOGGER.info("Guardado inicial del envio : {}", sending.getId());
		}
		else
		{
			_LOGGER.error("No existe un estado de procesamiento con el siguiente nombre: {}", StateEnum.INFORMACION_NO_PROCESADA.getName());
		}
		
	}
	
	/**
	 * Persist the sent
	 * @param validation sent to validation
	 * @param requiredSignature if file is signed
	 * @param nameNotes name of note file 
	 * @param nameReport name of report file 
	 */
	private void updateSending(Sending sending, Boolean validation, Boolean requiredSignature, String nameNotes, String nameNotes2, String nameReport, String nameResponse,Boolean blockSending, long currentTime)
	{
		sending.setResponseFinalPath(generateResponsePath(_TEMP_RESPONSE_FINAL_PATH,blockSending, currentTime)+"/"+nameResponse);
		sending.setResponseValidationPath(generateResponsePath(_TEMP_RESPONSE_VALIDATION_PATH,blockSending, currentTime)+"/"+nameResponse);
		
		if(validation)
		{
			sending.setTempValidationPath(generateReportPath(_TEMP_VALIDATION_PATH,blockSending, currentTime) + "/" + nameReport);
		}
		else
		{
			if(requiredSignature)
				sending.setTempSignedFilesPath(generateReportPath(_TEMP_SIGNED_FILES_PATH,blockSending, currentTime) + "/" + nameReport);
			else
				sending.setTempUnSignedFilesPath(generateReportPath(_TEMP_UN_SIGNED_FILES_PATH,blockSending, currentTime) + "/" + nameReport);
		}
		
		if(nameNotes!=null)
		{
			if(requiredSignature)
				sending.setTempSignedNotesPath(generateReportPath(_TEMP_SIGNED_NOTES_PATH,blockSending, currentTime) + "/" + nameNotes);
			else
				sending.setTempUnSignedNotesPath(generateReportPath(_TEMP_UN_SIGNED_NOTES_PATH,blockSending, currentTime) + "/" + nameNotes);
		}
		
		if(nameNotes2!=null)
		{
			if(requiredSignature)
				sending.setTempSignedNotes2Path(generateReportPath(_TEMP_SIGNED_NOTES_PATH,blockSending, currentTime) + "/" + nameNotes2);
			else
				sending.setTempUnSignedNotes2Path(generateReportPath(_TEMP_UN_SIGNED_NOTES_PATH,blockSending, currentTime) + "/" + nameNotes2);
		}
		
		//
		// Get the associated state
		stateList.getState().setName(StateEnum.INFORMACION_EN_PROCESO.getName());
		List<State> statesResult = stateList.getResultList();
		State state = null;
		
		if (statesResult.size() > 0)
		{
			state = statesResult.get(0);
			sending.setState(state);
			
			StateHome stateHome = (StateHome) Component.getInstance(StateHome.class,Boolean.TRUE);
			stateHome.setId(state.getId());
			
			SendingHome sendingHome = (SendingHome) Component.getInstance(SendingHome.class,Boolean.TRUE);
			sendingHome.setStateHome(stateHome);
			
			sendingHome.setInstance(sending);
			sendingHome.update();
			
			_LOGGER.info("Actualizando envio : {}", sending.getId());
		}
		else
		{
			_LOGGER.error("No existe un estado de procesamiento con el siguiente nombre: {}", StateEnum.INFORMACION_EN_PROCESO.getName());
		}
	}
	
	/**
	 * get nomenclature of note or file 
	 * @param nomReport
	 * @return
	 */
	private String generateNameReport(String nomReport, File originalUploadFile, Long seqSending, Boolean blocking)
	{

		String nameResult = nomReport;
		// tipoEntidad RNVEI
		
		if ( financialInstitution.getFinancialInstitutionType().getOldRNVEICode() != null)
			nameResult = nameResult.replace("{TIPO_ENTIDAD_CODE_RNVEI}", financialInstitution.getFinancialInstitutionType().getOldRNVEICode());
		if (financialInstitution.getFinancialInstitutionType().getCode() != null)
		{
			String institutionTypeCode = financialInstitution.getFinancialInstitutionType().getCode();
			for (int i = 0 ; i < 20 - institutionTypeCode.length(); i++)
			{
				if (nameResult.indexOf("{TIPO_ENTIDAD_CODE_"+ (i+1) +"}") != -1)
				{
					String finalInstitutionTypeCode = institutionTypeCode;
					
					for (int k = 0 ; k < i+1 - institutionTypeCode.length() ; k++)
					{
						finalInstitutionTypeCode = "0" + finalInstitutionTypeCode;
					}
					
					nameResult = nameResult.replace("{TIPO_ENTIDAD_CODE_"+ (i+1) +"}", finalInstitutionTypeCode);
				}
			}
			
			nameResult = nameResult.replace("{TIPO_ENTIDAD_CODE}", financialInstitution.getFinancialInstitutionType().getCode());
		}
		
		// codigoEntidad
		if (financialInstitution.getCode() != null)
		{
			String institutionCode = financialInstitution.getCode();
			
			for (int i = 0 ; i < 20 - institutionCode.length(); i++)
			{
				if (nameResult.indexOf("{ENTIDAD_CODE_"+ (i+1) +"}") != -1)
				{
					String finalInstitutionCode = institutionCode;
					
					for (int k = 0 ; k < i+1 - institutionCode.length() ; k++)
					{
						finalInstitutionCode = "0" + finalInstitutionCode;
					}
					
					nameResult = nameResult.replace("{ENTIDAD_CODE_"+ (i+1) +"}", finalInstitutionCode);
				}
			}
			nameResult = nameResult.replace("{ENTIDAD_CODE}", financialInstitution.getCode());
		}
		// codigoEntidad RNVEI
		if (financialInstitution.getOldRNVEICode() != null)
			nameResult = nameResult.replace("{ENTIDAD_CODE_RNVEI}", financialInstitution.getOldRNVEICode());
		
		// codigo de negocio
		if (businessProcess.getIncludeSFCBusinessCode() != null && businessProcess.getIncludeSFCBusinessCode())
		{
			BusinessCodeEntity businessCodeEntity = getBusinessCode(idBusinessCode);
			String codigoNegocio = businessCodeEntity.getBusinessCode();
			String legacyTypeCode = businessCodeEntity.getLegacyTypeCode().toString();
			String subLegacyTypeCode = businessCodeEntity.getSubLegacyTypeCode().toString();
			
			//Padding for business code
			for (int i = 0 ; i < 20 - codigoNegocio.length(); i++)
			{
				if (nameResult.indexOf("{CODIGO_NEGOCIO_"+ (i+1) +"}") != -1)
				{
					String finalCodigoNegocio = codigoNegocio;
					
					for (int k = 0 ; k < i+1 - codigoNegocio.length() ; k++)
					{
						finalCodigoNegocio = "0" + finalCodigoNegocio;
					}
					
					nameResult = nameResult.replace("{CODIGO_NEGOCIO_"+ (i+1) +"}", finalCodigoNegocio);
				}
			}
			
			nameResult = nameResult.replace("{CODIGO_NEGOCIO}", codigoNegocio);
			
			//Padding for legacy type code
			for (int i = 0 ; i < 20 - legacyTypeCode.length(); i++)
			{
				if (nameResult.indexOf("{TIPO_PATRI_CODE_"+ (i+1) +"}") != -1)
				{
					String finalLegacyTypeCode = legacyTypeCode;
					
					for (int k = 0 ; k < i+1 - legacyTypeCode.length() ; k++)
					{
						finalLegacyTypeCode = "0" + finalLegacyTypeCode;
					}
					
					nameResult = nameResult.replace("{TIPO_PATRI_CODE_"+ (i+1) +"}", finalLegacyTypeCode);
				}
			}
			
			nameResult = nameResult.replace("{TIPO_PATRI_CODE}", legacyTypeCode);
			
			//Padding for sub legacy tipe code
			for (int i = 0 ; i < 20 - subLegacyTypeCode.length(); i++)
			{
				if (nameResult.indexOf("{SUB_TIPO_PATRI_CODE_"+ (i+1) +"}") != -1)
				{
					String finalSubLegacyTypeCode = subLegacyTypeCode;
					
					for (int k = 0 ; k < i+1 - subLegacyTypeCode.length() ; k++)
					{
						finalSubLegacyTypeCode = "0" + finalSubLegacyTypeCode;
					}
					
					nameResult = nameResult.replace("{SUB_TIPO_PATRI_CODE_"+ (i+1) +"}", finalSubLegacyTypeCode);
				}
			}
			
			nameResult = nameResult.replace("{SUB_TIPO_PATRI_CODE}", subLegacyTypeCode);
		}
			
		
		if(!blocking)
		{
			Calendar cutOfDateCalendar = Calendar.getInstance();
			cutOfDateCalendar.setTime(cutOffDate);
			// diaFechaCorte
			String dayOfMonth = String.valueOf(cutOfDateCalendar.get(Calendar.DAY_OF_MONTH));
			if (dayOfMonth.length() == 1) dayOfMonth = "0" + dayOfMonth;
			nameResult = nameResult.replace("{CORTE_DIA}", dayOfMonth);
			// mesFechaCorte
			String month = String.valueOf(cutOfDateCalendar.get(Calendar.MONTH)+1);
			if (month.length() == 1) month = "0" + month;
			nameResult = nameResult.replace("{CORTE_MES}", month);
			// ano fecha corte
			nameResult = nameResult.replace("{CORTE_ANIO_4}", String.valueOf(cutOfDateCalendar.get(Calendar.YEAR)));
			// dos ultimos digitos ano
			nameResult = nameResult.replace("{CORTE_ANIO_2}", String.valueOf(cutOfDateCalendar.get(Calendar.YEAR)).subSequence(2, 4));
			// ultimo digito ano fecha corte
			nameResult = nameResult.replace("{CORTE_ANIO_1}", String.valueOf(cutOfDateCalendar.get(Calendar.YEAR)).subSequence(3, 4));
		}
		
		// extension		
		if (originalUploadFile!= null &&  originalUploadFile.getName().lastIndexOf(".")!= -1)
			nameResult = nameResult.replace("{EXT}", originalUploadFile.getName().substring(originalUploadFile.getName().lastIndexOf(".")+1));
		//Sub Extenion
		if (originalUploadFile!= null &&  originalUploadFile.getName().matches("^.+?\\.\\w+?\\.\\w+$"))
		{
			Pattern pattern = Pattern.compile("^.+?\\.(\\w+?)\\.\\w+$");
			Matcher match = pattern.matcher(originalUploadFile.getName());
			if(match.find())
			{
				nameResult = nameResult.replace("{SUB_EXT}", match.group(1));
			}
		}
		// codigo tipo de informacion
		if (reportType.getSuperfinancieraCode() != null)
			nameResult = nameResult.replace("{CODIGO_TIPO_INFORME}", reportType.getSuperfinancieraCode());
		// codigo del subsistema asociado al tipo de reporte
		if (reportType.getSubSystemCode() != null)
			nameResult = nameResult.replace("{CODIGO_SUB_SISTEMA}", reportType.getSubSystemCode());
		//Id envio
		if(seqSending!=null)
		{
			String sendingId = seqSending.toString();
			
			for (int i = 0 ; i < 20 - sendingId.length(); i++)
			{
				if (nameResult.indexOf("{ID_ENVIO_"+ (i+1) +"}") != -1)
				{
					String finalId = sendingId;
					
					for (int k = 0 ; k < i+1 - sendingId.length() ; k++)
					{
						finalId = "0" + finalId;
					}
					
					nameResult = nameResult.replace("{ID_ENVIO_"+ (i+1) +"}", finalId);
				}
			} 
			
			
			nameResult = nameResult.replace("{ID_ENVIO}", sendingId);
		}
		return nameResult;
	}
	
	/**
	 * get file path to store 
	 * @param directory
	 * @return
	 */
	private String generateReportPath(String directory,Boolean blocking, long currentTime){
		
		String pathTmpEnd = tmpReportStore;
		String pathReport = "";
		String pathCutOffDate ="";
		if(!blocking)
		{
			pathReport = "/"+reportType.getId();
			pathCutOffDate = "/Fecha_corte-" + formatDate.format(cutOffDate);
		}
		pathTmpEnd = pathTmpEnd + "/"+businessProcess.getId()+pathReport;
		pathTmpEnd = pathTmpEnd + "/" + directory + "/" + financialInstitution.getCode() + pathCutOffDate + "/" + currentTime;
		return pathTmpEnd; 
	}
	
	private String generateResponsePath(String directory,Boolean blocking, long currentTime)
	{
		String pathResponseEnd = tmpResponseStore; 
		String pathReport = "";
		String pathCutOffDate ="";
		if(!blocking)
		{
			pathReport = "/"+reportType.getId();
			pathCutOffDate = "/Fecha_corte-" + formatDate.format(cutOffDate);
		}
		pathResponseEnd = pathResponseEnd + "/"+businessProcess.getId()+pathReport;
		pathResponseEnd = pathResponseEnd + "/" + directory + "/" + financialInstitution.getCode() + pathCutOffDate + "/" + currentTime;
		// Create directory
		File file = new File(pathResponseEnd);
		file.mkdirs();
		return pathResponseEnd; 
	}
	
	private BusinessCodeEntity getBusinessCode(int idbusinessCode)
	{
		BusinessCodeEntity businessCodeEntity = new BusinessCodeEntity();
		businessCodeList.getBusinessCodeEntity().setId(idbusinessCode);
		List <BusinessCodeEntity> businessCodeEntityList = businessCodeList.getResultList();
		
		if(businessCodeEntityList.size() > 0)
		{
			businessCodeEntity = businessCodeEntityList.get(0);
		}
		else
		{
			businessCodeEntity = null;
		}

		return businessCodeEntity;
	}

}
