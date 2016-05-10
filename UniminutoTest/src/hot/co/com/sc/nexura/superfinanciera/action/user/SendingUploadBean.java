package co.com.sc.nexura.superfinanciera.action.user;

import co.com.sc.nexura.superfinanciera.action.admin.BusinessProcessList;
import co.com.sc.nexura.superfinanciera.action.admin.ReportTypeByFinancialInstitutionTypeCodeList;
import co.com.sc.nexura.superfinanciera.action.admin.ReportTypeList;
import co.com.sc.nexura.superfinanciera.action.admin.SendingList;
import co.com.sc.nexura.superfinanciera.action.security.AuthenticatorBean;
import co.com.sc.nexura.superfinanciera.model.*;
import co.com.sc.utils.TransferFileSCP;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.web.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("sendingUpload")
@Scope(ScopeType.SESSION)
public class SendingUploadBean implements Serializable
{
	
	// ---------------------------------------------------------------//
	// Class attributes
	// ---------------------------------------------------------------//
	
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create=true) 
	ReportTypeByFinancialInstitutionTypeCodeList reportTypeByFinancialInstitutionTypeCodeList;
	
	@In(create=true) 
	ReportTypeByFinancialInstitutionList reportTypeByFinancialInstitutionList;
	
	@In(create=true) 
	BusinessProcessList businessProcessList;
	
	@In(create=true) 
	BusinessCodeList businessCodeList;
	
	@In(create=true) 
	BusinessCodeControlList businessCodeControlList;
	
	@In(create=true) 
	ReportTypeList reportTypeList;
	
	@In(create=true) 
	IUploadFileAsynchronous uploadFileAsynchronous;
	
	@In(create=true) 
	ReportTypePeriodicityBean reportTypePeriodicityBean;
	
	AuthenticatorBean authenticator;
	
	SendTypeEnum sendTypeSelected;
	
	List<ReportType> reportTypeItems = new ArrayList<ReportType>();

	List<File> dataDefinitiveFiles = new ArrayList<File>();

	File dataNotesFile;
	
	File dataNotesFile2;
	
	Boolean enabledNotes = false;
	
	private Boolean firstLoadExecuted = false;
	
	private Long businessCodeSFC;
	
	private String businessCodeNameDefaultSelected;
	
	private Integer idBusinessCodeAssociated;
	
	private String idBusinessCodeAssociatedList = "";
	
	// ---------------------------------------------------------------//
	// Class constant
	// ---------------------------------------------------------------//

	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(SendingUploadBean.class);

	// ---------------------------------------------------------------//
	// Business class attributes
	// ---------------------------------------------------------------//
	
	/**
	 * Current business process
	 */
	private BusinessProcess processContext = new BusinessProcess();
	
	/**
	 * Selected report type used to send a file
	 */
	private ReportType reportTypeContext = new ReportType();
		
	/**
	 * Report associated cut off date
	 */
	private Date cutOffDate;
	
    private Boolean disableBusinessCode = false;
    
    
    




	// ---------------------------------------------------------------//
	// Class constructors methods
	// ---------------------------------------------------------------//
	


	/**
	 * Default class constructor
	 */
	public SendingUploadBean()
	{
		authenticator = (AuthenticatorBean) Contexts.getSessionContext().get("authenticator");
		
		if (authenticator != null)
		{
			if (authenticator.getFinancialInstitution() == null)
			{
				FacesMessages.instance().add("Su sesion ha expirado. Por favor autentiquese nuevamente.");
				_LOGGER.info("La sesion del usuario ha caducado. El usuario debera ingresar por la interfaz de autenticacion.");
				Session.instance().invalidate();
			}
		}
		else
		{
			FacesMessages.instance().add("Su sesion ha expirado. Por favor autentiquese nuevamente.");
			_LOGGER.info("La sesion del usuario ha caducado. El usuario debera ingresar por la interfaz de autenticacion.");
			Session.instance().invalidate();
		}
	}

	// ---------------------------------------------------------------//
	// Class business methods
	// ---------------------------------------------------------------//

	/**
	 * Listener load type report a process
	 * @param event BusinessProcess id
	 */
	public void loadItemsReportType(ValueChangeEvent event)
	{
		if (event.getNewValue() != null)
		{
			Long idBusinessProcessValue = (Long) event.getNewValue();
			this.processContext = loadBusinessProcess(idBusinessProcessValue);
			getReportsByProcess();
			
			if (reportTypeItems.size() > 0)
			{
				this.reportTypeContext = reportTypeItems.get(0);
				reportTypePeriodicityBean.changeLogic(reportTypeItems.get(0).getId());
			}
			else
			{
				reportTypePeriodicityBean.changeLogic(null);
			}
			
		}
		else
		{
			reportTypeItems = new ArrayList<ReportType>();
			reportTypePeriodicityBean.changeLogic(null);
		}
	}
	
	/**
	 * Load a business process from his associated id.
	 * @param id param
	 * @return Current Business process
	 */
	private BusinessProcess loadBusinessProcess(Long id)
	{
		businessProcessList.getBusinessProcess().setId(id);
		return businessProcessList.getSingleResult();
	}

	public List<BusinessCodeEntity> loadBusinessCodeList(String values)
	{
		if(!values.trim().equals(""))
        {	
        	Pattern pattern = Pattern.compile("\\d+\\-\\d+\\-(\\d+)");
			Matcher matcher = pattern.matcher(values.trim());
			if(matcher.find())
			{
				businessCodeControlList.getBusinessCodeEntity().setBusinessCode(matcher.group(1));
			}
			else
			{
				pattern = Pattern.compile("\\d+");
				matcher = pattern.matcher(values.trim());
				if(matcher.find())
				{
					businessCodeControlList.getBusinessCodeEntity().setBusinessCode(values.trim());
				}
			}
        }
		businessCodeControlList.getBusinessCodeEntity().setFinancialInstitutionsId(Integer.parseInt(authenticator.getFinancialInstitutionCode()));
		businessCodeControlList.getBusinessCodeEntity().setFinancialInstitutionTypesId(Integer.parseInt(authenticator.getFinancialInstitutionTypeCode()));
        List<BusinessCodeEntity> businessCodeList =  this.businessCodeControlList.getResultList();
        
        if(businessCodeList.size() == 1)
        {
        	disableBusinessCode = false;
        	BusinessCodeEntity businessCodeEntity = businessCodeList.get(0);
        	businessCodeNameDefaultSelected = "(" + businessCodeEntity.getBusinessCode() + ") " + ((businessCodeEntity.getBusinessCode().trim().equals("0"))? "Sin negocios asociados":businessCodeEntity.getBusinessName());
        	idBusinessCodeAssociatedList = businessCodeEntity.getLegacyTypeCode() + "-" + businessCodeEntity.getSubLegacyTypeCode() + "-" + businessCodeEntity.getBusinessCode();
        	idBusinessCodeAssociated = businessCodeEntity.getId();
        }
        else
        {
        	disableBusinessCode = true;
        	businessCodeNameDefaultSelected = "";
        	if(values.equals(""))
        	{
        		businessCodeList = null;
        	}
        	else if(!values.trim().equals(""))
        	{
        		idBusinessCodeAssociatedList = values;
        	}
        }
        
		return businessCodeList;
	}
	
	/**
	 * Initial business process load
	 */
	public void initialBusinessProcessLoad()
	{
		if (this.firstLoadExecuted)	
		{
			return;
		}
		
		if (authenticator.getBusinessProcess().size() == 1)
		{
			this.processContext = cloneBusinessProcess(authenticator.getBusinessProcess().get(0));
		}
		else if (this.processContext.getId() != null)
		{
			boolean objectFound = false;
			
			for (BusinessProcess currentBusinessProcess :  authenticator.getBusinessProcess())
			{
				if (currentBusinessProcess.getId().equals(this.processContext.getId()))
				{
					this.processContext = cloneBusinessProcess(currentBusinessProcess);
					objectFound = true;
				}
			}
			
			if (!objectFound)
			{
				if (authenticator.getBusinessProcess().size() > 0)
				{
					this.processContext = cloneBusinessProcess(authenticator.getBusinessProcess().get(0));
				}
				else
				{
					this.processContext = new BusinessProcess();
					reportTypeItems = new ArrayList<ReportType>();
				}
			}
		}
		else
		{
			if (authenticator.getBusinessProcess().size() > 0)
			{
				this.processContext = cloneBusinessProcess(authenticator.getBusinessProcess().get(0));
			}
			else
			{
				this.processContext = new BusinessProcess();
				reportTypeItems = new ArrayList<ReportType>();
			}
		}
		
		
		
		if (this.processContext.getId() != null)
		{
			getReportsByProcess();
			if (reportTypeItems.size() > 0)
			{
				this.reportTypeContext = reportTypeItems.get(0);
				reportTypePeriodicityBean.changeLogic(reportTypeItems.get(0).getId());
			}
			else
			{
				reportTypePeriodicityBean.changeLogic(null);
			}
		}
		else
		{
			this.processContext = new BusinessProcess();
			reportTypeItems = new ArrayList<ReportType>();
		}
		
		this.firstLoadExecuted = true;
		loadBusinessCodeList(""); 
	}
	
	/**
	 * Event submit form to upload files in fileSystem
	 */
	public String sendFilesAction()
	{
		try
		{
			if(!processContext.getBlockSending().booleanValue() && reportTypeContext!=null)
			{
				reportTypeContext = getReportTypeId(reportTypeContext.getId());
			}
		
			if(!validateForm())
			{
				return "SendingUpload?faces-redirect=true";
			}
			
			String tmpReportStore = authenticator.getSpecificProperty(PropertyEnum.PROPERTY_NAME_TEMP_BASE_PATH.getName());
			String tmpResponseStore = authenticator.getSpecificProperty(PropertyEnum.PROPERTY_NAME_RESPONSE_BASE_PATH.getName());
			
			if(authenticator.getFinancialInstitution() != null && authenticator.getFinancialInstitution().getId() != null)
			{
				if (processContext.getBlockSending())
				{
					cutOffDate = null;
				}
				
				validateRemoteConection(processContext);
				_LOGGER.info("Listo para transmitir. Proceso validado correctamente...");
				
				//Para los procesos que no incluyen el cOdigo de negocio
				if(!processContext.getIncludeSFCBusinessCode())
				{
					idBusinessCodeAssociated = null;
				}
				else if(!idBusinessCodeAssociatedList.trim().equals(""))
				{
					idBusinessCodeAssociated = getIdBussinesCodeFromForm(idBusinessCodeAssociatedList);
				}

				
				uploadFileAsynchronous.processFile(dataDefinitiveFiles, dataNotesFile, dataNotesFile2, processContext, reportTypeContext, sendTypeSelected, cutOffDate,authenticator.getFinancialInstitution(),tmpReportStore,tmpResponseStore,idBusinessCodeAssociated);
				
				cleanForm();
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Los archivos enviados ya se encuentran listos para su validacion y procesamiento.");
				return "SendingStateVerifier?faces-redirect=true";
			}
			else
			{
				cleanForm();
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Su sesion ha expirado. Por favor autentiquese nuevamente.");
				Session.instance().invalidate();
				return "SendingUpload?faces-redirect=true";
			}
		}
		catch(IOException e)
		{
			FacesMessages.instance().clear();
			FacesMessages.instance().add(e.getMessage());
			return "SendingUpload?faces-redirect=true";
		}
		catch(Exception e1)
		{
			if(e1 instanceof SftpException)
			{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Error accediendo al directorio remoto o a sus propiedades. Descripci\u00f3n: " + e1.getMessage(),FacesMessage.SEVERITY_ERROR);
			}
			else if(e1 instanceof JSchException)
			{
				FacesMessages.instance().clear();
				e1.printStackTrace();
				FacesMessages.instance().add("Error al establecer la conexi\u00f3n remota. Descripci\u00f3n: " + e1.getMessage(),FacesMessage.SEVERITY_ERROR);
			}
			else if(e1 instanceof IOException)
			{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Error en la configuraci\u00f3n de las propiedades para la conexi\u00f3n remota. Descripci\u00f3n: " + e1.getMessage(),FacesMessage.SEVERITY_ERROR);
			}
			else
			{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Error general producido al intentar enviar los archivos. Descripci\u00f3n: " + e1.getMessage(),FacesMessage.SEVERITY_ERROR);
			}
			return "SendingUpload?faces-redirect=true";
		}
	}
	
	/**
	 * Cleans the form input
	 */
	public void cleanForm()
	{
		sendTypeSelected = null;
		dataNotesFile=null;
		dataNotesFile2 = null;
		dataDefinitiveFiles = new ArrayList<File>();
		reportTypeContext = new ReportType();
		processContext = new BusinessProcess();
		businessCodeNameDefaultSelected = "";
		idBusinessCodeAssociatedList = "";
		idBusinessCodeAssociated=null;
	}
	
	// ---------------------------------------------------------------//
	// Private methods
	// ---------------------------------------------------------------//

	/**
	 * Loads the reports associated with a business process and a financial institution.
	 */
	private void getReportsByProcess()
	{
		//
		// Validates an load the business process
		boolean objectFound = false;
		
		for (BusinessProcess currentBusinessProcess :  authenticator.getBusinessProcess())
		{
			if (currentBusinessProcess.getId().equals(this.processContext.getId()))
			{
				this.processContext = cloneBusinessProcess(currentBusinessProcess);
				objectFound = true;
			}
		}
		
		if (!objectFound)
		{
			if (authenticator.getBusinessProcess().size() > 0)
			{
				this.processContext = cloneBusinessProcess(authenticator.getBusinessProcess().get(0));
			}
			else
			{
				this.processContext = new BusinessProcess();
				reportTypeItems = new ArrayList<ReportType>();
			}
		}
		
		//
		//Load the report types if the business process exists
		if (this.processContext.getId() != null)
		{
			reportTypeByFinancialInstitutionTypeCodeList.getReportType().getBusinessProcess().setId(this.processContext.getId());
			reportTypeByFinancialInstitutionTypeCodeList.getFinancialInstitutionType().setCode(authenticator.getFinancialInstitution().getFinancialInstitutionType().getCode());
			reportTypeItems = reportTypeByFinancialInstitutionTypeCodeList.getResultList();
			
			reportTypeByFinancialInstitutionList.getReportType().getBusinessProcess().setId(this.processContext.getId());
			reportTypeByFinancialInstitutionList.getFinancialInstitution().setCode(authenticator.getFinancialInstitution().getCode());
			List <ReportType> secondReportTypeList = reportTypeByFinancialInstitutionList.getResultList();
			
			List<Long> currentReportTypesIds = new ArrayList<Long>();
			for(ReportType currentReportType : reportTypeItems)
			{
				currentReportTypesIds.add(currentReportType.getId());
			}
			
			for(ReportType currentReportType : secondReportTypeList)
			{
				if (!currentReportTypesIds.contains(currentReportType.getId()))
				{
					reportTypeItems.add(currentReportType);
				}
			}
			
			if(processContext.getBlockSending() != null && processContext.getBlockSending())
			{
				sendTypeSelected = SendTypeEnum.ENVIO_DEFINITIVO;
			}
			else if (!processContext.getValidationAvailable())
			{
				sendTypeSelected = SendTypeEnum.ENVIO_DEFINITIVO;
			}
		}
	}
	
	private BusinessProcess cloneBusinessProcess (BusinessProcess currentBusinessProcess)
	{
		try
		{
			return (BusinessProcess) currentBusinessProcess.clone();
		}
		catch (CloneNotSupportedException e)
		{
			return currentBusinessProcess;
		}
	}
	
	/**
	 * Validate form
	 * @return true if the form is valid. False otherwise
	 */
	private boolean validateForm()
	{
		boolean exito = true;
		Integer idBusinessCode;
		//
		// Validation File 
		if(dataDefinitiveFiles.size()==0)
		{
			FacesMessages.instance().add("Debe realizar la carga del archivo.");
			exito = false;
			this.firstLoadExecuted=false;
			//cleanForm();
		}
		
		if(cutOffDate == null && !processContext.getBlockSending())
		{
			
			FacesMessages.instance().add("Debe especificar la fecha de corte que desea cargar.");
			exito = false;
			this.firstLoadExecuted=false;
			//cleanForm();
		}
		
		//
		// Validation NoteFile
		if (authenticator.getFinancialInstitution().getNotesDisabled() == null)
		{
			authenticator.getFinancialInstitution().setNotesDisabled(false);
		}
		
		if(!authenticator.getFinancialInstitution().getNotesDisabled() && processContext.getNotesRequired() && sendTypeSelected.equals(SendTypeEnum.ENVIO_DEFINITIVO))
		{
			if((dataNotesFile==null && dataNotesFile2==null) || (dataNotesFile.getTotalSpace() == 0 && dataNotesFile.getTotalSpace() == 0))
			{
				FacesMessages.instance().add("Debe realizar la carga del archivo de Notas");
				exito = false;
				//cleanForm();
				firstLoadExecuted=false;
			}
		}
		
		//
		// Validation sent to BusinessProcess, reportypeContext and cutOfDate
		if(processContext != null && reportTypeContext != null && cutOffDate != null){
			
			SendingList sendingList= (SendingList) Component.getInstance(SendingList.class,Boolean.TRUE);
			sendingList.getSending().setFinancialInstitution(authenticator.getFinancialInstitution());
			sendingList.getSending().setCutOffDate(cutOffDate);
			sendingList.getSending().setReportType(reportTypeContext);
			if(idBusinessCodeAssociated != null && processContext.getIncludeSFCBusinessCode() && !idBusinessCodeAssociatedList.trim().equals(""))
			{
				idBusinessCode = getIdBussinesCodeFromForm(idBusinessCodeAssociatedList);
				sendingList.getSending().setIdBusinessCode(idBusinessCode);
			}
			else if(processContext.getIncludeSFCBusinessCode() && !idBusinessCodeAssociatedList.trim().equals(""))
			{
				idBusinessCode = getIdBussinesCodeFromForm(idBusinessCodeAssociatedList);
				sendingList.getSending().setIdBusinessCode(idBusinessCode);
			}

			List<String> states = new ArrayList<String>();
			states.add(StateEnum.INFORMACION_EN_PROCESO.getName());
			states.add(StateEnum.INFORMACION_PROCESADA_EXITO.getName());
			sendingList.setStates(states);
			sendingList.setOrderColumn("sending.id");
			sendingList.setOrderDirection("desc");
			
			List<Sending> sendings = sendingList.getResultList();
			Boolean hasSending =  (sendings.size() >0)?Boolean.TRUE:Boolean.FALSE;
			
			if(hasSending && !sendings.get(0).getResendAllowed() && !sendings.get(0).getSendingForValidation())
			{
				//_LOGGER.error("ParAmetro de entrada: idesfa [" + idBusinessCode +"] reporte [" + reportTypeContext.getName() + "] proceso: [" + processContext.getName() + "] codAsociado = [" + idBusinessCodeAssociated + "] valorCaja [" + idBusinessCodeAssociatedList + "] verdadero_Pre [" + processContext.getIncludeSFCBusinessCode() + "]");
				FacesMessages.instance().add("No se puede enviar informacion para el periodo de corte seleccionado ya que este se encuentra en cualquiera de los siguientes estados:  Informacion procesada con exito o Informacion en procesamiento");
				exito = false;
				firstLoadExecuted=false;
				//cleanForm();
			}
		}
		
		if(!exito)
		{
			cleanForm();
		}
		
		return exito;
	}

	/**
	 * get Type Report for Id
	 * @param idReportType param
	 * @return repor type object
	 */
	private ReportType getReportTypeId(Long idReportType)
	{	
		reportTypeList.getReportType().setId(idReportType);
		List<ReportType> rTypeList = reportTypeList.getResultList();
		ReportType reportType = null;
		
		if(rTypeList.size() > 0)
		{
			reportType = rTypeList.get(0);
		}
		else
		{
			_LOGGER.error("No existe el tipo de reporte con el ID: {}", idReportType);
		}
		return reportType;
	}
	
	private void validateEnabledNotes()
	{
		ReportType reportTypeTmp = getReportTypeId(reportTypeContext.getId());

		//Valida que el reporte tenga nomebclatura de notas es requisito para recibir notas
		if (reportTypeTmp.getNomenclatureNotes() != null)
		{
			if(reportTypeTmp!=null && reportTypeTmp.getId()!=null && reportTypeTmp.getNomenclatureNotes().trim().length() > 0 && (sendTypeSelected !=null && sendTypeSelected.equals(SendTypeEnum.ENVIO_DEFINITIVO)))
			{	
				if(processContext!=null)
				{
					setEnabledNotes(processContext.getNotesRequired() && !authenticator.getFinancialInstitution().getNotesDisabled());
				}
				else
				{
					processContext = new BusinessProcess(); 
					setEnabledNotes(false);
				}
			}
			else
			{
				setEnabledNotes(false);
			}
		}
		else
		{
			setEnabledNotes(false);
		}
	}
	
	private Integer getIdBussinesCodeFromForm(String idBusinessCodeAssociatedList)
	{
		Pattern pattern = Pattern.compile("(\\d+)\\-(\\d+)\\-(\\d+)");
		Matcher matcher = pattern.matcher(idBusinessCodeAssociatedList.trim());
		if(matcher.find())
		{
			businessCodeList.getBusinessCodeEntity().setLegacyTypeCode(Integer.parseInt(matcher.group(1)));
			businessCodeList.getBusinessCodeEntity().setSubLegacyTypeCode(Integer.parseInt(matcher.group(2)));
			businessCodeList.getBusinessCodeEntity().setBusinessCode(matcher.group(3));
			//_LOGGER.error("grupos regex grupo1["+matcher.group(1)+"] grupo2["+matcher.group(2)+"] grupo3["+matcher.group(3)+"] tipo [" + authenticator.getFinancialInstitutionCode() + "] code [" + authenticator.getFinancialInstitutionTypeCode() + "]");
		}
		
		businessCodeList.getBusinessCodeEntity().setFinancialInstitutionsId(Integer.parseInt(authenticator.getFinancialInstitutionCode()));
		businessCodeList.getBusinessCodeEntity().setFinancialInstitutionTypesId(Integer.parseInt(authenticator.getFinancialInstitutionTypeCode()));
		BusinessCodeEntity businessCodeEntityListResult = businessCodeList.getSingleResult();
		return businessCodeEntityListResult.getId();
	}
	
	/**
	 * Validate connection attributes to remote servers
	 * @param businessProcess object to validate
	 * @throws SftpException type exception in connection
	 * @throws JSchException type exception in connection
	 * @throws IOException type exception in connection
	 * @throws Exception another exception
	 */
	private void validateRemoteConection ( BusinessProcess businessProcess ) throws Exception
	{
		if(businessProcess.getRemoteAres())
		{
			// get server setting remote signature
			//Validar IP Ares
			validatePropertyConection(businessProcess.getScpAresMachine(), "La propiedad máquina o ip Ares no est\u00e1 configurada", true);
			//Validar puerto Ares
			validatePropertyConection(businessProcess.getRemoteAresPort().toString(), "La propiedad puerto remoto Ares no est\u00e1 configurado", true);
			//Validar uisuario Ares
			validatePropertyConection(businessProcess.getScpAresUser(), "La propiedad usuario Ares no est\u00e1 configurado", true);
			//Validar clave Ares
			validatePropertyConection(businessProcess.getScpAresPassword(), "La propiedad password Ares no est\u00e1 configurado", true);

			conectionTester(businessProcess.getScpAresMachine(), businessProcess.getRemoteAresPort(), businessProcess.getScpAresUser(), businessProcess.getScpAresPassword(), businessProcess, true);
		}
		else if(businessProcess.getRemoteReportCopy())
		{
			// get server setting remote UnSignature
			//Validar IP Servidor Remoto
			validatePropertyConection(businessProcess.getScpMachine(), "La propiedad máquina o ip remota no está configurada", true);
			//Validar puerto Servidor Remoto
			validatePropertyConection(businessProcess.getRemotePort().toString(), "La propiedad puerto remoto no est\u00e1 configurado", true);
			//Validar uisuario Servidor Remoto
			validatePropertyConection(businessProcess.getScpUser(), "La propiedad usuario remoto no est\u00e1 configurado", true);
			//Validar clave Servidor Remoto
			validatePropertyConection(businessProcess.getScpPassword(), "La propiedad password remoto no est\u00e1 configurado", true);

			conectionTester(businessProcess.getScpMachine(), businessProcess.getRemotePort(), businessProcess.getScpUser(), businessProcess.getScpPassword(), businessProcess, true);
		}


	}

	/**
	 * Test Remote folders before send files
	 * @param businessProcess param server connection
	 * @throws SftpException type exception in connection
	 * @throws JSchException type exception in connection
	 * @throws IOException type exception in connection
	 * @throws Exception another exception
	 */
	private void testRemoteFolders (BusinessProcess businessProcess) throws Exception
	{
		String host;
		int port;
		String user;
		String passwd;

		//Validate Ares sign folders
		if(businessProcess.getRemoteAres())
		{
			host = businessProcess.getScpAresMachine();
			port = businessProcess.getRemoteAresPort();
			user = businessProcess.getScpAresUser();
			passwd = businessProcess.getScpAresPassword();
			// Validar directorios en Ares para archivos definitivos y notas firmados en ambos casos
			validateDirectories(host, port, user, passwd, businessProcess.getSignedFilesPath(), false);
			validateDirectories(host, port, user, passwd, businessProcess.getSignedNotesPath(), false);
		}

		//Validate Remote Server unsigned folders
		if(businessProcess.getRemoteReportCopy())
		{
			host = businessProcess.getScpMachine();
			port = businessProcess.getRemotePort();
			user = businessProcess.getScpUser();
			passwd = businessProcess.getScpPassword();
			//Validar directorios para Servidor remoto, archivos definitivos, validacion, temporales y notas sin firma
			// En algunos casos se puede presentar con directorios locales
			validateDirectories(host, port, user, passwd, businessProcess.getValidationPath(), false);
			validateDirectories(host, port, user, passwd, businessProcess.getUnSignedNotesPath(), false);
			validateDirectories(host, port, user, passwd, businessProcess.getUnSignedFilesPath(), true);
			validateDirectories(host, port, user, passwd, businessProcess.getUnSignedFilesPathTemp(), true);
		}

		//Validate remote response folders
		if(businessProcess.getRemoteResponseRead())
		{
			host = businessProcess.getScpMachine();
			port = businessProcess.getRemotePort();
			user = businessProcess.getScpUser();
			passwd = businessProcess.getScpPassword();
			//Validar directorios para acrchivos de respuesta definitivos y validacion
			validateDirectories(host, port, user, passwd, businessProcess.getResponseFinalPath(), false);
			validateDirectories(host, port, user, passwd, businessProcess.getResponseValidationPath(), false);
		}
	}

	/**
	 * Tester for conections Ares an remote Server and tester directories associated to that
	 * @param ip param server to test connection
	 * @param port param server to test connection
	 * @param user param server to test connection
	 * @param passwd param server to test connection
	 * @param businessProcess param server to test connection
	 * @param validationProperties param server to test connection
	 * @throws Exception exception mannage
	 */
	private void conectionTester(String ip, Integer port, String user, String passwd, BusinessProcess businessProcess, boolean validationProperties) throws Exception
	{
		if(validationProperties)
		{
			//probar conectividad
			//validar la conexión al servidor remoto de Ares
			TransferFileSCP.testRemoteConection(ip, port, user, passwd);
			//validar directorios asociados al servidor Ares y al servidor remoto, los directorios de archivos firmados
			testRemoteFolders(businessProcess);
		}
	}

	/**
	 * Validate properties conection of the bussines process
	 * @param property param property to evaluate
	 * @param message paraqm messageerror report
	 * @param required property s required
	 * @return true if value is verified otherwise false
	 * @throws Exception
	 */
	private boolean validatePropertyConection(String property, String message, boolean required) throws Exception
	{
		String messageException = "No se puede realizar el proceso de env\u00edo del archivo. Descripci\u00f3n del error: ";
		String messageExceptionEnd = ". Comun\u00edquese con el administrador del sistema";

		// get server setting remote signature
		if(property == null || property.trim().equals("") ||property.trim().equals("0"))
		{
			if(required)
			{
				throw new Exception(messageException + message + messageExceptionEnd);
			}
			return false;
		}

		return true;
	}

	/**
	 * Tster remote directories
	 * @param host connection for tester
	 * @param port connection for tester
	 * @param user connection for tester
	 * @param passwd connection for tester
	 * @param directory connection for tester
	 * @param isLocal this directory can be local server
     * @throws Exception exception for remotes connections
     */
	private void validateDirectories(String host, int port, String user, String passwd, String directory, boolean isLocal) throws Exception
	{
		String pattern = ".rw.{7}";
		if(validatePropertyConection(directory, "", false))
		{
			try
			{
				TransferFileSCP.validateDirectoryPermission(host, port, user, passwd, directory, pattern);
			}
			catch(Exception e)
			{
				//Valida si el directorio puede manejare localmente solo para el caso del servidor remoto
				// Ares siempre es remoto
				if(!isLocal)
				{
					String message = "Directorio: [" + directory + "] Servidor: [" + host + "] Usuario: [" + user + "].";
					throw new Exception("Error al acceder al directorio: " + message + "Descripción interna:" + e.getCause());
				}
			}
		}
	}

	// ---------------------------------------------------------------//
	// Class getters methods
	// ---------------------------------------------------------------//


	/**
	 * @return the businessCodeNameDefaultSelected
	 */
	public String getBusinessCodeNameDefaultSelected()
	{
		return businessCodeNameDefaultSelected;
	}

	/**
	 * @return the disableBusinessCode
	 */
	public Boolean getDisableBusinessCode()
	{
		return disableBusinessCode;
	}

	/**
	 * @return the enabledNotes
	 */
	public Boolean getEnabledNotes()
	{
		validateEnabledNotes();
		return enabledNotes;
	}

	/**
	 * @return the reportTypeContext
	 */
	public ReportType getReportTypeContext()
	{
		return reportTypeContext;
	}

	/**
	 * @return the processContext
	 */
	public BusinessProcess getProcessContext()
	{
		return processContext;
	}

	/**
	 * @return the reportTypeItems
	 */
	public List<ReportType> getReportTypeItems()
	{
		return reportTypeItems;
	}

	public List<SelectItem> getSendTypes()
	{
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (SendTypeEnum type : SendTypeEnum.values())
		{
			if (processContext != null && processContext.getValidationAvailable() != null )
			{
				if (!type.equals(SendTypeEnum.ENVIO_VALIDACION))
				{
					items.add(new SelectItem(type, type.getLabel()));
				}
				else if (processContext.getValidationAvailable())
				{
					items.add(new SelectItem(type, type.getLabel()));
				}
			}
		}
		return items;
	}
	
	/**
	 * @return the cutOfDate
	 */
	public Date getCutOffDate()
	{
		return cutOffDate;
	}

	/**
	 * @return the firstLoadExecuted
	 */
	public Boolean getFirstLoadExecuted() 
	{
		return firstLoadExecuted;
	}
	
	/**
	 * @return the dataNotesFile
	 */
	public File getDataNotesFile()
	{
		return dataNotesFile;
	}

	/**
	 * @return the dataNotesFile2
	 */
	public File getDataNotesFile2()
	{
		return dataNotesFile2;
	}
	
	/**
	 * 
	 * @return the businessCodeSFC
	 */
	public Long getBusinessCodeSFC() 
	{
		return businessCodeSFC;
	}
	
	/**
	 * @return the sendTypeSelected
	 */
	public SendTypeEnum getSendTypeSelected()
	{
		return sendTypeSelected;
	}

	/**
	 * @return the idBusinessCodeAssociated
	 */
	public Integer getIdBusinessCodeAssociated() 
	{
		return idBusinessCodeAssociated;
	}

	/**
	 * @return the idBusinessCodeAssociatedObject
	 */
	public String getIdBusinessCodeAssociatedList() 
	{
		return idBusinessCodeAssociatedList;
	}

	
	// ---------------------------------------------------------------//
	// Class setters methods
	// ---------------------------------------------------------------//

	/**
	 * @param idBusinessCodeAssociatedList the idBusinessCodeAssociatedList to set
     */
	public void setIdBusinessCodeAssociatedList(String idBusinessCodeAssociatedList) 
	{
		this.idBusinessCodeAssociatedList = idBusinessCodeAssociatedList;
	}
	
	/**
	 * @param idBusinessCodeAssociated the idBusinessCodeAssociated to set
	 */
	public void setIdBusinessCodeAssociated(Integer idBusinessCodeAssociated) 
	{
		this.idBusinessCodeAssociated = idBusinessCodeAssociated;
	}

	/**
	 * 
	 * @param businessCodeSFC the businessCodeSFC to set
	 */
	public void setBusinessCodeSFC(Long businessCodeSFC) 
	{
		this.businessCodeSFC = businessCodeSFC;
	}

	/**
	 * @param firstLoadExecuted the firstLoadExecuted to set
	 */
	public void setFirstLoadExecuted(Boolean firstLoadExecuted)
	{
		if (firstLoadExecuted != null)
		{
			this.firstLoadExecuted = firstLoadExecuted;
		}
	}

	/**
	 * @param firstLoadExecuted the firstLoadExecuted to set
	 */
	public void setFirstLoadExecutedCustom(String firstLoadExecuted)
	{
		if (firstLoadExecuted != null && !firstLoadExecuted.trim().equals(""))
		{
			this.firstLoadExecuted = Boolean.parseBoolean(firstLoadExecuted);
		}
	}
	
	/**
	 * @param cutOffDate the cutOffDate to set
	 */
	public void setCutOffDate(Date cutOffDate)
	{
		this.cutOffDate = cutOffDate;
	}

	/**
	 * @param processContext the processContext to set
	 */
	public void setProcessContext(BusinessProcess processContext)
	{
		this.processContext = processContext;
	}

	/**
	 * @param reportTypeContext the reportTypeContext to set
	 */
	public void setReportTypeContext(ReportType reportTypeContext)
	{
		this.reportTypeContext = reportTypeContext;
	}

	/**
	 * @param enabledNotes the enabledNotes to set
	 */
	public void setEnabledNotes(Boolean enabledNotes)
	{
		this.enabledNotes = enabledNotes;
	}

	/**
	 * @param sendTypeSelected the sendTypeSelected to set
	 */
	public void setSendTypeSelected(SendTypeEnum sendTypeSelected)
	{
		this.sendTypeSelected = sendTypeSelected;
	}

	/**
	 * @param dataDefinitiveFiles the dataDefinitiveFiles to set
	 */
	public void setDataDefinitiveFiles(List<File> dataDefinitiveFiles)
	{
		this.dataDefinitiveFiles = dataDefinitiveFiles;
	}

	/**
	 * @param dataNotesFile the dataNotesFile to set
	 */
	public void setDataNotesFile(File dataNotesFile)
	{
		this.dataNotesFile = dataNotesFile;
	}

	/**
	 * @param dataNotesFile2 the dataNotesFile2 to set
	 */
	public void setDataNotesFile2(File dataNotesFile2)
	{
		this.dataNotesFile2 = dataNotesFile2;
	}
	
	/**
	 * @return the dataDefinitiveFiles
	 */
	public List<File> getDataDefinitiveFiles()
	{
		return dataDefinitiveFiles;
	}
}
