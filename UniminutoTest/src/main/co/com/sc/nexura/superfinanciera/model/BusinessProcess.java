package co.com.sc.nexura.superfinanciera.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.jboss.seam.annotations.Name;

/**
 * Business process business object. 
 * 
 * @author Alex Vicente ChacOn JimEnez (alex.chacon@software-colombia.com)
 * @since JDK 1.7.0_04
 * @version 1.0
 */
@Entity
@Table(name = "ProcesoNegocio")
@Name("businessProcess")
public class BusinessProcess implements Serializable, Cloneable
{
	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id.
	 */
	private static final long serialVersionUID = 1L;
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	/**
	 * Unique business process identifier
	 */
	private Long id;
	
	/**
	 * Business process name
	 */
	private String name;
	
	/**
	 * Business process state
	 */
	private Boolean state;
	
	/**
	 * Business process description
	 */
	private String description;
	 
	/**
	 * Signed files path
	 */
	private String signedFilesPath;
	
	/**
	 * unsigned files path
	 */
	private String unSignedFilesPath;
	
	/**
	 * unsigned files path temporal
	 */
	private String unSignedFilesPathTemp;
	
	/**
	 * Signed notes path
	 */
	private String signedNotesPath;
	
	/**
	 * Unsigned noted path
	 */
	private String unSignedNotesPath;
	
	/**
	 * Validation path
	 */
	private String validationPath;
	
	/**
	 * Response validation path
	 */
	private String responseValidationPath;
	
	/**
	 * Response final path
	 */
	private String responseFinalPath;
	
	/**
	 * Digital signature required flag
	 */
	private Boolean digitalSignatureRequired;
	
	/**
	 * Additional notes required flag
	 */
	private Boolean notesRequired;
	
	/**
	 * Business process validation logic available flag 
	 */
	private Boolean validationAvailable;
	
	/**
	 * Maximum retry number
	 */
	private Integer retryNumber;
	
	/**
	 * Time defined in order to execute a retry
	 */
	private Integer retryTime;
	
	/**
	 * Success response pattern
	 */
	private String successResponsePattern;
	
	/**
	 * Success validation response pattern
	 */
	private String successResPatternValidat;
	
	/**
	 * Success remote response read
	 */
	private Boolean remoteResponseRead;
	
	/**
	 * Remote copy
	 */
	private Boolean remoteReportCopy;
	
	/**
	 * Remote digital signed file copy
	 */
	private Boolean remoteAres;
	
	/**
	 * Superfinanciera business code include 
	 */
	private Boolean includeSFCBusinessCode;
	
	/**
	 * Remote port
	 */
	private Integer remotePort;
	
	/**
	 * User scp
	 */
	private String scpUser;
	
	/**
	 * Password scp
	 */
	private String scpPassword;
	
	/**
	 * Machine scp
	 */
	private String scpMachine;
	
	/**
	 * Remote Ares port
	 */
	private Integer remoteAresPort;
	
	/**
	 * Ares user scp
	 */
	private String scpAresUser;
	
	/**
	 * Ares password scp
	 */
	private String scpAresPassword;
	
	/**
	 * Ares machine scp
	 */
	private String scpAresMachine;
	
	/**
	 * Files block sending allowed
	 */
	private Boolean blockSending;
	
	/**
	 * Response file nomenclature
	 */
	private String nomenclatureResponse;
	
	/**
	 * Report file nomenclature
	 */
	private String nomenclatureReport;
	
	/**
	 * Business process associated report types
	 */
	//private Set<ReportType> reportTypes = new HashSet<ReportType>(0);
	
	/**
	 * Report type associated financial institution types
	 */
	private Set<FinancialInstitutionType> financialInstitutionTypes = new HashSet<FinancialInstitutionType>(0);
	
	/**
	 * Associated financial institutions
	 */
	private Set<FinancialInstitution> financialInstitutions = new HashSet<FinancialInstitution>(0);
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "SeqProcesoNegocio")
	@SequenceGenerator(name = "SeqProcesoNegocio", sequenceName = "SeqProcesoNegocio")
	@Column(name = "Id")
	public Long getId()
	{
		return id;
	}
	
	/**
	 * @return the name
	 */
	@Column(name = "Nombre", unique = false, nullable = false, length = 200)
	@NotNull
	@Size(min = 2, max = 200)
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the state
	 */
	@Column(name = "Estado", unique = false, nullable = false)
	@NotNull
	public Boolean getState()
	{
		return state;
	}
	
	/**
	 * @return the associated description
	 */
	@Column(name = "Descripcion", unique = false, nullable = false, length = 500)
	@NotNull
	@Size(min = 2, max = 500)
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * @return the signedFilesPath
	 */
	@Column(name = "RutaArchivosFirmados", unique = false, nullable = true, length = 1500)
	public String getSignedFilesPath()
	{
		return signedFilesPath;
	}

	/**
	 * @return the unSignedFilesPath
	 */
	@Column(name = "RutaArchivosNoFirmados", unique = false, nullable = true, length = 1500)
	public String getUnSignedFilesPath()
	{
		return unSignedFilesPath;
	}

	/**
	 * @return the signedNotesPath
	 */
	@Column(name = "RutaNotasFirmadas", unique = false, nullable = true, length = 1500)
	public String getSignedNotesPath()
	{
		return signedNotesPath;
	}

	/**
	 * @return the unSignedNotesPath
	 */
	@Column(name = "RutaNotasNoFirmadas", unique = false, nullable = true, length = 1500)
	public String getUnSignedNotesPath()
	{
		return unSignedNotesPath;
	}

	/**
	 * @return the validationPath
	 */
	@Column(name = "RutaValidacion", unique = false, nullable = true, length = 1500)
	public String getValidationPath()
	{
		return validationPath;
	}

	/**
	 * @return the responseValidationPath
	 */
	@Column(name = "RutaRespuestaValidacion", unique = false, nullable = true, length = 1500)
	public String getResponseValidationPath()
	{
		return responseValidationPath;
	}

	/**
	 * @return the responseFinalPath
	 */
	@Column(name = "RutaRespuestasProcesamiento", unique = false, nullable = true, length = 1500)
	public String getResponseFinalPath()
	{
		return responseFinalPath;
	}

	/**
	 * @return the digitalSignatureRequired
	 */
	@Column(name = "FirmaDigitalRequerida", unique = false, nullable = false)
	@NotNull
	public Boolean getDigitalSignatureRequired()
	{
		return digitalSignatureRequired;
	}

	/**
	 * @return the notesRequired
	 */
	@Column(name = "NotasRequeridas", unique = false, nullable = false)
	@NotNull
	public Boolean getNotesRequired()
	{
		return notesRequired;
	}

	/**
	 * @return the validationAvailable
	 */
	@Column(name = "LogicaValidacionDisponible", unique = false, nullable = false)
	@NotNull
	public Boolean getValidationAvailable()
	{
		return validationAvailable;
	}

	/**
	 * @return the retryNumber
	 */
	@Column(name = "NumeroReintentos", unique = false, nullable = false, length = 3)
	@NotNull
	public Integer getRetryNumber()
	{
		return retryNumber;
	}

	/**
	 * @return the retryTime
	 */
	@Column(name = "TiempoReintento", unique = false, nullable = false, length = 4)
	public Integer getRetryTime()
	{
		return retryTime;
	}

	/**
	 * @return the successResponsePattern
	 */
	@Column(name = "PatronRespuestaExitosa", unique = false, nullable = true, length = 1500)
	public String getSuccessResponsePattern()
	{
		return successResponsePattern;
	}
	
	/**
	 * @return the remoteResponseRead
	 */
	@Column(name = "LecturaRemotaRespuesta", unique = false, nullable = false)
	public Boolean getRemoteResponseRead()
	{
		return remoteResponseRead;
	}
	
	/**
	 * @return the remoteReportCopy
	 */
	@Column(name = "CopiaRemotaReportes", unique = false, nullable = false)
	@NotNull
	public Boolean getRemoteReportCopy()
	{
		return remoteReportCopy;
	}
	
	/**
	 * @return the remotePort
	 */
	@Column(name = "PuertoRemoto", unique = false, nullable = true, length = 6)
	public Integer getRemotePort()
	{
		return remotePort;
	}
	
	/**
	 * @return the scpUser
	 */
	@Column(name = "scpUser", unique = false, nullable = true, length = 200)
	@Size(min = 2, max = 200)
	public String getScpUser()
	{
		return scpUser;
	}
	
	/**
	 * @return the scpPassword
	 */
	@Column(name = "scpPassword", unique = false, nullable = true, length = 200)
	@Size(min = 2, max = 200)
	public String getScpPassword()
	{
		return scpPassword;
	}
	
	/**
	 * @return the  Remote Machine
	 */
	@Column(name = "scpMachine", unique = false, nullable = true, length = 200)
	@Size(min = 2, max = 200)
	public String getScpMachine()
	{
		return scpMachine;
	}
	
	/**
	 * @return the reportTypes
	 */
	//@OneToMany(fetch = FetchType.EAGER, mappedBy="businessProcess")
	//public Set<ReportType> getReportTypes()
	//{
	//	return reportTypes;
	//}
	
	/**
	 * @return the remoteAres
	 */
	@Column(name = "AresRemoto", unique = false, nullable = true)
	public Boolean getRemoteAres()
	{
		return remoteAres;
	}
	
	/**
	 * @return the includeSFCBusinessCode
	 */
	@Column(name = "SFCCodigoNegocio", unique = false, nullable = true)
	public Boolean getIncludeSFCBusinessCode()
	{
		return includeSFCBusinessCode;
	}

	/**
	 * @return the remoteAresPort
	 */
	@Column(name = "PuertoRemotoAres", unique = false, nullable = true, length = 6)
	public Integer getRemoteAresPort()
	{
		return remoteAresPort;
	}

	/**
	 * @return the scpAresUser
	 */
	@Column(name = "ScpAresUser", unique = false, nullable = true, length = 200)
	@Size(min = 2, max = 200)
	public String getScpAresUser()
	{
		return scpAresUser;
	}

	/**
	 * @return the scpAresPassword
	 */
	@Column(name = "ScpAresPassword", unique = false, nullable = true, length = 200)
	@Size(min = 2, max = 200)
	public String getScpAresPassword()
	{
		return scpAresPassword;
	}

	/**
	 * @return the scpAresMachine
	 */
	@Column(name = "ScpAresMachine", unique = false, nullable = true, length = 200)
	@Size(min = 2, max = 200)
	public String getScpAresMachine()
	{
		return scpAresMachine;
	}

	/**
	 * @return the financialInstitutionTypes
	 */
	@ManyToMany(targetEntity = FinancialInstitutionType.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_Proc_Neg_Proc_Neg" , inverseName = "FK_Proc_Neg_Tipo_Enti")
    @JoinTable(name = "ProcNeg_TipoEnti", joinColumns = @JoinColumn(name = "Id_ProceNego"), inverseJoinColumns = @JoinColumn(name = "Id_TipoEnti"))
	public Set<FinancialInstitutionType> getFinancialInstitutionTypes()
	{
		return financialInstitutionTypes;
	}
	
	/**
	 * @return the successResPatternValidat
	 */
	@Column(name = "PatronRespuestaExitosaVal", unique = false, nullable = true, length = 1500)
	public String getSuccessResPatternValidat()
	{
		return successResPatternValidat;
	}

	/**
	 * @return the blockSending
	 */
	@Column(name = "EnvioPorBloques", unique = false, nullable = true)
	public Boolean getBlockSending()
	{
		return blockSending;
	}

	/**
	 * @return the nomenclatureResponse
	 */
	@Column(name = "NomenclaturaRespuestas", unique = false, nullable = true, length = 1000)
	public String getNomenclatureResponse()
	{
		return nomenclatureResponse;
	}
	
	/**
	 * @return the nomenclatureReport
	 */
	@Column(name = "NomenclaturaReportes", unique = false, nullable = true, length = 1000)
	public String getNomenclatureReport()
	{
		return nomenclatureReport;
	}
	

	/**
	 * @return the financialInstitutions
	 */
	@ManyToMany(targetEntity = FinancialInstitution.class, fetch = FetchType.EAGER)
    @ForeignKey(name = "FK_Proc_Neg_Proc_Ent" , inverseName = "FK_Proc_Neg_Enti")
    @JoinTable(name = "ProcNeg_Enti", joinColumns = @JoinColumn(name = "Id_ProceNego"), inverseJoinColumns = @JoinColumn(name = "Id_Enti"))
	public Set<FinancialInstitution> getFinancialInstitutions()
	{
		return financialInstitutions;
	}
	
	/**
	 * @return the unSignedFilesPathTemp
	 */
	@Column(name = "RutaArchivosNoFirmadosTemp", unique = false, nullable = true, length = 1500)
	public String getUnSignedFilesPathTemp()
	{
		return unSignedFilesPathTemp;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	/**
	 * @param unSignedFilesPathTemp the unSignedFilesPathTemp to set
	 */
	public void setUnSignedFilesPathTemp(String unSignedFilesPathTemp)
	{
		this.unSignedFilesPathTemp = unSignedFilesPathTemp;
	}

	/**
	 * @param financialInstitutions the financialInstitutions to set
	 */
	public void setFinancialInstitutions(Set<FinancialInstitution> financialInstitutions)
	{
		this.financialInstitutions = financialInstitutions;
	}

	/**
	 * @param nomenclatureReport the nomenclatureReport to set
	 */
	public void setNomenclatureReport(String nomenclatureReport)
	{
		this.nomenclatureReport = nomenclatureReport;
	}

	/**
	 * @param successResPatternValidat the successResPatternValidat to set
	 */
	public void setSuccessResPatternValidat(String successResPatternValidat)
	{
		this.successResPatternValidat = successResPatternValidat;
	}

	/**
	 * @param blockSending the blockSending to set
	 */
	public void setBlockSending(Boolean blockSending)
	{
		this.blockSending = blockSending;
	}

	/**
	 * @param nomenclatureResponse the nomenclatureResponse to set
	 */
	public void setNomenclatureResponse(String nomenclatureResponse)
	{
		this.nomenclatureResponse = nomenclatureResponse;
	}

	/**
	 * @param financialInstitutionTypes the financialInstitutionTypes to set
	 */
	public void setFinancialInstitutionTypes(Set<FinancialInstitutionType> financialInstitutionTypes)
	{
		this.financialInstitutionTypes = financialInstitutionTypes;
	}

	/**
	 * @param remoteAres the remoteAres to set
	 */
	public void setRemoteAres(Boolean remoteAres)
	{
		this.remoteAres = remoteAres;
	}

	/**
	 * @param includeSFCBusinessCode the includeSFCBusinessCode to set
	 */
	public void setIncludeSFCBusinessCode(Boolean includeSFCBusinessCode)
	{
		this.includeSFCBusinessCode = includeSFCBusinessCode;
	}
	
	/**
	 * @param remoteAresPort the remoteAresPort to set
	 */
	public void setRemoteAresPort(Integer remoteAresPort)
	{
		this.remoteAresPort = remoteAresPort;
	}

	/**
	 * @param scpAresUser the scpAresUser to set
	 */
	public void setScpAresUser(String scpAresUser)
	{
		this.scpAresUser = scpAresUser;
	}

	/**
	 * @param scpAresPassword the scpAresPassword to set
	 */
	public void setScpAresPassword(String scpAresPassword)
	{
		this.scpAresPassword = scpAresPassword;
	}

	/**
	 * @param scpAresMachine the scpAresMachine to set
	 */
	public void setScpAresMachine(String scpAresMachine)
	{
		this.scpAresMachine = scpAresMachine;
	}

	/**
	 * @param remotePort the remotePort to set
	 */
	public void setRemotePort(Integer remotePort)
	{
		this.remotePort = remotePort;
	}
	
	/**
	 * @param reportTypes the reportTypes to set
	 */
	//public void setReportTypes(Set<ReportType> reportTypes)
	//{
	//	this.reportTypes = reportTypes;
	//}

	/**
	 * @param signedFilesPath the signedFilesPath to set
	 */
	public void setSignedFilesPath(String signedFilesPath)
	{
		this.signedFilesPath = signedFilesPath;
	}

	/**
	 * @param unSignedFilesPath the unSignedFilesPath to set
	 */
	public void setUnSignedFilesPath(String unSignedFilesPath)
	{
		this.unSignedFilesPath = unSignedFilesPath;
	}

	/**
	 * @param signedNotesPath the signedNotesPath to set
	 */
	public void setSignedNotesPath(String signedNotesPath)
	{
		this.signedNotesPath = signedNotesPath;
	}

	/**
	 * @param unSignedNotesPath the unSignedNotesPath to set
	 */
	public void setUnSignedNotesPath(String unSignedNotesPath)
	{
		this.unSignedNotesPath = unSignedNotesPath;
	}

	/**
	 * @param validationPath the validationPath to set
	 */
	public void setValidationPath(String validationPath)
	{
		this.validationPath = validationPath;
	}

	/**
	 * @param responseValidationPath the responseValidationPath to set
	 */
	public void setResponseValidationPath(String responseValidationPath)
	{
		this.responseValidationPath = responseValidationPath;
	}

	/**
	 * @param responseFinalPath the responseFinalPath to set
	 */
	public void setResponseFinalPath(String responseFinalPath)
	{
		this.responseFinalPath = responseFinalPath;
	}

	/**
	 * @param digitalSignatureRequired the digitalSignatureRequired to set
	 */
	public void setDigitalSignatureRequired(Boolean digitalSignatureRequired)
	{
		this.digitalSignatureRequired = digitalSignatureRequired;
	}

	/**
	 * @param notesRequired the notesRequired to set
	 */
	public void setNotesRequired(Boolean notesRequired)
	{
		this.notesRequired = notesRequired;
	}

	/**
	 * @param validationAvailable the validationAvailable to set
	 */
	public void setValidationAvailable(Boolean validationAvailable)
	{
		this.validationAvailable = validationAvailable;
	}

	/**
	 * @param retryNumber the retryNumber to set
	 */
	public void setRetryNumber(Integer retryNumber)
	{
		this.retryNumber = retryNumber;
	}

	/**
	 * @param retryTime the retryTime to set
	 */
	public void setRetryTime(Integer retryTime)
	{
		this.retryTime = retryTime;
	}

	/**
	 * @param successResponsePattern the successResponsePattern to set
	 */
	public void setSuccessResponsePattern(String successResponsePattern)
	{
		this.successResponsePattern = successResponsePattern;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @param state the state to set
	 */
	public void setState (Boolean state )
	{
		this.state = state ;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * @param remoteReportCopy the remoteReportCopy to set
	 */
	public void setRemoteReportCopy(Boolean remoteReportCopy)
	{
		this.remoteReportCopy = remoteReportCopy;
	}
	
	/**
	 * @param remoteResponseRead the remoteResponseRead to set
	 */
	public void setRemoteResponseRead(Boolean remoteResponseRead)
	{
		this.remoteResponseRead = remoteResponseRead;
	}
	
	/**
	 * @param scpUser the scpUser to set
	 */
	public void setScpUser(String scpUser)
	{
		this.scpUser = scpUser;
	}
	
	/**
	 * @param scpPassword the scpPassword to set
	 */
	public void setScpPassword(String scpPassword)
	{
		this.scpPassword = scpPassword;
	}
	
	/**
	 * @param scpMachine the scpMachine to set
	 */
	public void setScpMachine(String scpMachine)
	{
		this.scpMachine = scpMachine;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}