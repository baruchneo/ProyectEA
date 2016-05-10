package co.com.sc.nexura.superfinanciera.action.security;

import co.com.sc.nexura.superfinanciera.action.admin.*;
import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.Property;
import co.com.sc.nexura.superfinanciera.model.PropertyEnum;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("authenticator")
@Scope(ScopeType.SESSION)
public class AuthenticatorBean implements Authenticator, Serializable 
{
	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Administrator default application role 
	 */
	private static final String _ADMIN_ROLE = "Admin";
	
	/**
	 * User default login 
	 */
	private static final String _USER_LOGIN = "user";

	/**
	 * User default password 
	 */
	private static final String _USER_PASSWORD = "Superfinanciera2012";
	
	/**
	 * User default application role 
	 */
	private static final String _USER_ROLE = "User";
	
	/**
	 * User default application role to change password
	 */
	private static final String _USER_ROLE_CHANGE_PASSWORD = "Password";
	
	/**
	 * Auditor default application role 
	 */
	private static final String _AUDITOR_ROLE = "Auditor";
	
	/**
	 * Default users property file
	 */
	private static final String _DEFAULT_USERS_PROPERTY_FILE = "pages.properties";
	
	/**
	 * User default financial institution code include with cero values i.e.: 0034
	 */
	private static final String _FINANCIAL_INSTITUTION_CODE = "000002"; //000037 rcn  000001 Titularizadora
	
	/**
	 * User default financial institution code include with cero values i.e.: 0034
	 */
	private static final String _FINANCIAL_INSTITUTION_TYPE_CODE = "025"; // 080 rcn 600 Titularizadora
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(AuthenticatorBean.class);
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	@In
	Identity identity;

	@In
	Credentials credentials;

	@In(create=true)
	DirectAuthenticate directAuthenticate;

	@In(create=true)
	FinancialInstitutionList financialInstitutionList;
	
	@In(create=true)
	PropertyList propertyList;
	
	@In(create=true)
	BusinessProcessByReportTypeList businessProcessByReportTypeList;
	
	@In(create=true)
	BusinessProcessByFinancialInstitutionTypeList businessProcessByFinancialInstitutionTypeList;
	
	@In(create=true)
	BusinessProcessByFinancialInstitutionList businessProcessByFinancialInstitutionList;
	
	@In(create=true)
	BusinessProcessByReportTypeByFinancialInstitutionList businessProcessByReportTypeByFinancialInstitutionList;
	
	/**
	 * User associated financial institution
	 */
	private FinancialInstitution financialInstitution;
	
	/**
	 * User associated financial institution code
	 */
	private String financialInstitutionCode;
	
	/**
	 * Financial institution type code
	 */
	private String financialInstitutionTypeCode;
	
	/**
	 * User associated authentication response
	 */
	private boolean authenticationResult = false;
	
	
	/**
	 * Administrator user authentication flag
	 */
	private boolean adminUserAuthentication = false;
	
	/**
	 * Gets the system properties
	 */
	private List<Property> systemProperties = null;
	
	/**
	 * Gets the associated business process
	 */
	private List<BusinessProcess> businessProcess = null;
	
	/**
	 * Open LDAP directory context
	 */
	private DirContext dirContext;
	
	/**
	 * User common name
	 */
	private String financialInstitutionPrincipal;

	/*
	 * @return true if the user exists and false otherwise.
	 */
	public boolean authenticate() 
	{
		return authenticate(credentials.getUsername(), credentials.getPassword());
	}

	/**
	 * This Method loged user from appbuilder params
	 */
	public String RedirectUserFromValores()
	{
		//decipher Optional

		//assign to financial institution
		if(directAuthenticate.getRequestFromValores() != null && directAuthenticate.getRequestFromValores()) {
			financialInstitutionCode = "000" + directAuthenticate.getUserFromRequest().substring(3, 6);
			financialInstitutionTypeCode = directAuthenticate.getUserFromRequest().substring(0, 3);
			financialInstitution = getFinancialInstitution(financialInstitutionCode, financialInstitutionTypeCode);
			credentials.setUsername(directAuthenticate.getUserFromRequest());
			credentials.setPassword("TemporalPassword");
		}
		String resultLogin = identity.login();

		return resultLogin;
	}

	/**
	 * @param login user login
	 * @param password user password
	 * @return true if the user exists and false otherwise
	 */
	public boolean authenticate(String login, String password) 
	{
		if(directAuthenticate.getRequestFromValores() != null && directAuthenticate.getRequestFromValores())
		{
			_LOGGER.info("Usuario conectado por opciOn valores {}", login);

			authenticationResult = true;
			loadSystemProperties ();

			identity.addRole(_USER_ROLE);

			if (financialInstitution != null && financialInstitution.getCode() != null)
			{
				associateBusinessProcessToCurrentUser();
			}

			//requestFromValores = false;
			return authenticationResult;
		}

		_LOGGER.info("Usuario actual {}", login);
		
		//
		// Write your authentication logic here,
		// return true if the authentication was
		// successful and false otherwise



		String defaultUserRole = verifyDefaultUsers(login, password);
		
		if (defaultUserRole != null)
		{
			identity.addRole(defaultUserRole);
			
			financialInstitution = null;
			financialInstitutionCode = null;
			authenticationResult = true;
			return authenticationResult;
		}
		
 		if (login.equals(_USER_LOGIN) && password.equals(_USER_PASSWORD))
		{
			loadSystemProperties ();
			identity.addRole(_USER_ROLE);
			
			financialInstitution = getFinancialInstitution(_FINANCIAL_INSTITUTION_CODE, _FINANCIAL_INSTITUTION_TYPE_CODE);
			financialInstitutionCode = _FINANCIAL_INSTITUTION_CODE;
			financialInstitutionTypeCode = _FINANCIAL_INSTITUTION_TYPE_CODE;
			authenticationResult = true;
			if (financialInstitution != null && financialInstitution.getCode() != null)
			{
				associateBusinessProcessToCurrentUser();
			}
			
			return authenticationResult;
		}

		if (ldapAuthenticacion (login, password))
		{
			if (financialInstitutionCode != null && !financialInstitutionCode.trim().equals("") && financialInstitutionTypeCode != null && !financialInstitutionTypeCode.trim().equals(""))
			{	
				setExternalUserEnvironment(login);
			}
			else
			{
				if (adminUserAuthentication)
				{
					
					Pattern pattern = Pattern.compile("(.*)(\\d{3})(\\d{3})");
					Matcher matcher = pattern.matcher(login);
					if(!matcher.find())
					{
						identity.addRole(_AUDITOR_ROLE);
						
						financialInstitution = null;
						financialInstitutionCode = null;
						authenticationResult = true;
					}
					else
					{
						financialInstitutionTypeCode = matcher.group(2);
						financialInstitutionCode = matcher.group(3);
						
						setExternalUserEnvironment(login);
						
						_LOGGER.info("Usuario autenticado pero tipo y codigo de entidad determinados por el nombre: {} {}", matcher.group(2), matcher.group(3));
					}
				}
				else
				{
					authenticationResult = false;
				}
			}
		}
		else
		{
			authenticationResult = false;
		}
		return authenticationResult;
	}

	/**
	 * @param login user login
	 * @param password user password
	 * 
	 * @return user role or null if the user does not exists
	 */
	private String verifyDefaultUsers(String login, String password)
	{
		String defaultUserRole = null;
		InputStream inputStream = null;
		
		try
		{
			Properties prop = new Properties();
			
			inputStream = this.getClass().getClassLoader().getResourceAsStream(_DEFAULT_USERS_PROPERTY_FILE);
			
			if (inputStream != null) 
			{
				prop.load(inputStream);
			} 
			else 
			{
				//intento de la carga medante url como contingencia
				URL url = this.getClass().getResource(_DEFAULT_USERS_PROPERTY_FILE);
				
				inputStream = url.openStream();
				
				if (inputStream != null) 
				{
					prop.load(inputStream);
				}
				else
				{
					// si ninguno de los dos casos funciona se va por la excepciOn
					throw new FileNotFoundException("Archivo de propiedades '" + _DEFAULT_USERS_PROPERTY_FILE + "' no encontrado en el classpath");
				}
			}
			
			//
			// Verify if the user is super administrator
			defaultUserRole = verifyDefaultUsersHelper(login, password, "AdminUser_", "AdminPassword_", _ADMIN_ROLE, prop);
			if (defaultUserRole != null)
			{
				return defaultUserRole;
			}
			
			//
			// Verify if the user is auditor
			
			defaultUserRole = verifyDefaultUsersHelper(login, password, "AuditorUser_", "AuditorPassword_", _AUDITOR_ROLE, prop);
			if (defaultUserRole != null)
			{
				return defaultUserRole;
			}
			
		}
		catch (Exception e)
		{
			_LOGGER.error("Problemas al momento de tratar de autenticar a los usuarios administradores: {} ", e.getMessage());
		}
		finally
		{
			try
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
			catch (Exception e){}
		}
		
		return defaultUserRole;
	}

	/**
	 * Verify default user helper
	 * @param login user login
	 * @param password user password
	 * @param propertyUserPrefix default user prefix
	 * @param propertyPasswordPrefix default user password
	 * @param role user role
	 * @param prop system properties
	 * @return null is the user is not found. The role is the user is found 
	 */
	private String verifyDefaultUsersHelper(String login, String password, String propertyUserPrefix, String propertyPasswordPrefix, String role, Properties prop)
	{
		int i = 1;
		String currentUser = prop.getProperty(propertyUserPrefix + i);
		
		while (currentUser != null)
		{
			String currentPassword = prop.getProperty(propertyPasswordPrefix + i);
			
			if (login.equals(currentUser.trim()) && password.equals(currentPassword.trim())) 
			{
				return role;
			}
			
			i++;
			currentUser = prop.getProperty(propertyUserPrefix + i);
		}
		return null;
	}
	
	/**
	 * @param login currentUserLoginn
	 */
	private void setExternalUserEnvironment(String login)
	{
		financialInstitution = getFinancialInstitution(financialInstitutionCode, financialInstitutionTypeCode);
		authenticationResult = true;
		
		if (financialInstitution.isChangePasswordNow(login))
		{
			identity.addRole(_USER_ROLE_CHANGE_PASSWORD);
		}
		else
		{
			identity.addRole(_USER_ROLE);
			
			if (financialInstitution != null && financialInstitution.getCode() != null)
			{
				associateBusinessProcessToCurrentUser();
			}
		}
	}
	
	/**
	 * This method associates business process to the current user.
	 */
	private void associateBusinessProcessToCurrentUser()
	{
		businessProcessByReportTypeList.getFinancialInstitution().setCode(financialInstitutionCode);
		businessProcessByReportTypeList.getFinancialInstitution().getFinancialInstitutionType().setCode(financialInstitutionTypeCode);
		this.businessProcess = businessProcessByReportTypeList.getResultList();
		
		List<Long> currentBusinessProcessIds = new ArrayList<Long>();
		for(BusinessProcess currentBusinessProcess : this.businessProcess)
		{
			currentBusinessProcessIds.add(currentBusinessProcess.getId());
		}
		
		businessProcessByFinancialInstitutionTypeList.getFinancialInstitution().setCode(financialInstitutionCode);
		businessProcessByFinancialInstitutionTypeList.getFinancialInstitution().getFinancialInstitutionType().setCode(financialInstitutionTypeCode);
		List<BusinessProcess> businessProcessByFinancialInstitutionType = businessProcessByFinancialInstitutionTypeList.getResultList();
		
		associateBusinessProcessToCurrentUserVerifier(currentBusinessProcessIds,businessProcessByFinancialInstitutionType);
		
		businessProcessByFinancialInstitutionList.getFinancialInstitution().setCode(financialInstitutionCode);
		businessProcessByFinancialInstitutionList.getFinancialInstitution().getFinancialInstitutionType().setCode(financialInstitutionTypeCode);
		List<BusinessProcess> businessProcessByFinancialInstitution = businessProcessByFinancialInstitutionList.getResultList();
		
		associateBusinessProcessToCurrentUserVerifier(currentBusinessProcessIds,businessProcessByFinancialInstitution);
		
		businessProcessByReportTypeByFinancialInstitutionList.getFinancialInstitution().setCode(financialInstitutionCode);
		businessProcessByReportTypeByFinancialInstitutionList.getFinancialInstitution().getFinancialInstitutionType().setCode(financialInstitutionTypeCode);
		List<BusinessProcess> businessProcessByReportTypeByFinancialInstitution = businessProcessByReportTypeByFinancialInstitutionList.getResultList();
		
		associateBusinessProcessToCurrentUserVerifier(currentBusinessProcessIds,businessProcessByReportTypeByFinancialInstitution);
	}

	/**
	 * Verifies that the process is not associated yet
	 * @param currentBusinessProcessIds
	 * @param businessProcessByFinancialInstitutionType
	 */
	private void associateBusinessProcessToCurrentUserVerifier( List<Long> currentBusinessProcessIds, List<BusinessProcess> businessProcessByFinancialInstitutionType)
	{
		for(BusinessProcess currentBusinessProcess : businessProcessByFinancialInstitutionType)
		{
			if (!currentBusinessProcessIds.contains(currentBusinessProcess.getId()))
			{
				currentBusinessProcessIds.add(currentBusinessProcess.getId());
				this.businessProcess.add(currentBusinessProcess);
			}
		}
	}
	
	//---------------------------------------------------------------//
	// Business methods
	//---------------------------------------------------------------//

	/**
	 * Executes an LDAP authentication. This class internally sets the financial institution code
	 * 
	 * @param userName User name
	 * @param password User password
	 */
	private boolean ldapAuthenticacion (String userName, String password)
	{
		//
		// Initials credential validation
		if (userName == null || userName.trim().equals("") || password == null || password.trim().equals(""))
		{
			return false;
		}
		
		//
		// Gets LDAP authentication properties
		loadSystemProperties();
		
		// 
    	// Get system attributes
    	String initialContextFactory = getSpecificProperty(PropertyEnum.PROPERTY_NAME_INITIAL_CONTEXT_FACTORY.getName());
    	String providerURL = getSpecificProperty(PropertyEnum.PROPERTY_NAME_PROVIDER_URL.getName());
    	String authenticationMode = getSpecificProperty(PropertyEnum.PROPERTY_NAME_SECURITY_AUTHENTICATION.getName());
    	String principalUser = getSpecificProperty(PropertyEnum.PROPERTY_NAME_USER_PRINCIPAL.getName());
    	String principalAdmin = getSpecificProperty(PropertyEnum.PROPERTY_NAME_ADMIN_PRINCIPAL.getName());
    	String principalUserPassword = getSpecificProperty(PropertyEnum.PROPERTY_NAME_LDAP_USER_PASSWORD.getName());
    	String principalAdminPassword = getSpecificProperty(PropertyEnum.PROPERTY_NAME_LDAP_ADMIN_PASSWORD.getName());
    	String principalUserBasePath = getSpecificProperty(PropertyEnum.PROPERTY_NAME_LDAP_SEARCH_BASE_PATH_USER.getName());
    	String principalAdminBasePath = getSpecificProperty(PropertyEnum.PROPERTY_NAME_LDAP_SEARCH_BASE_PATH_ADMIN.getName());
    	String principalUserBasePathContingency = getSpecificProperty(PropertyEnum.PROPERTY_NAME_LDAP_SEARCH_BASE_PATH_USER_CONTINGENCY.getName());
    	String principalAdminBasePathContingency = getSpecificProperty(PropertyEnum.PROPERTY_NAME_LDAP_SEARCH_BASE_PATH_ADMIN_CONTINGENCY.getName());
    	
    	//
    	// Step No. 1: Verify if the authentication is related with an external user - Main server path
    	boolean externalUserResponse = baseLogicConnection(initialContextFactory, providerURL, authenticationMode, principalUser, principalUserPassword, principalUserBasePath, "(cn=" + userName + ")", password, true, userName);
    	
    	if (!externalUserResponse)
    	{
        	//
        	// Step No. 2: Verify if the authentication is related with an internal user - Main server path
    		boolean internalUserResponse = baseLogicConnection(initialContextFactory, providerURL, authenticationMode, principalAdmin, principalAdminPassword, principalAdminBasePath, "(cn=" + userName + ")", password, false, userName);
    		
    		if (!internalUserResponse)
    		{
    			//
            	// Step No. 1: Verify if the authentication is related with an external user - Contingency server path
            	externalUserResponse = baseLogicConnection(initialContextFactory, providerURL, authenticationMode, principalUser, principalUserPassword, principalUserBasePathContingency, "(sAMAccountName=" + userName + ")", password, true, userName);
            	
            	if (!externalUserResponse)
            	{
            		//
    	        	// Step No. 2: Verify if the authentication is related with an internal user - Contingency server path
            		internalUserResponse = baseLogicConnection(initialContextFactory, providerURL, authenticationMode, principalAdmin, principalAdminPassword, principalAdminBasePathContingency, "(sAMAccountName=" + userName + ")", password, false, userName);
            		
            		if (!internalUserResponse)
            		{
            			return internalUserResponse;
            		}
            		else
            		{
            			adminUserAuthentication = true;
            			return internalUserResponse;
            		}
            	}
    		}
    		else
    		{
    			adminUserAuthentication = true;
    		}
    	}
    	
    	return true;
	}
	
	/**
	 * Base logic LDAP connection
	 * 
	 * @param initialContextFactory
	 * @param providerURL
	 * @param authenticationMode
	 * @param basePrincipal
	 * @param basePrincipalPassword
	 * @param searchFilter
	 * @param currentPassword
	 * @param principalBasePath
	 * @param setMetadata
	 * @return true if the user exists. False otherwise
	 */
	private boolean baseLogicConnection(String initialContextFactory, String providerURL, String authenticationMode, String basePrincipal, String basePrincipalPassword, String principalBasePath, String searchFilter, String currentPassword, boolean setMetadata, String currentLogin)
	{
		//
    	// Step No. 1 Access the LDAP directory with the base user
    	DirContext dirContext = ldapConnectionBusinessLogic (initialContextFactory, providerURL, authenticationMode, basePrincipal, basePrincipalPassword);
    	financialInstitutionTypeCode = null;
    	financialInstitutionCode = null;
		
    	if (dirContext == null)
    	{
    		_LOGGER.info("Falla al momento de ingresar al directorio LDAP con las credenciales del usuario base.  ProviderURL: {} -  Access: {} ", providerURL, basePrincipal + ":" + basePrincipalPassword);
            return false;
    	}
    	
    	//
    	// Step No. 2. Search the user
    	SearchResult searchResult = findAccountByAccountName(dirContext, principalBasePath, searchFilter);
    	
    	if (searchResult == null)
    	{
    		_LOGGER.info("Usuario no existe en el directorio LDAP.  PrincipalBasePath: {} -  Usuario: {} ", principalBasePath, searchFilter);
    		return false;
    	}
    	else
    	{
    		Attribute financialInstitutionTypeCodeAttribute = searchResult.getAttributes().get("TipoEntidad");
    		Attribute financialInstitutionCodeAttribute = searchResult.getAttributes().get("CodigoEntidad");
    		String financialInstitutionPrincipal = searchResult.getNameInNamespace();
        	
    		if (setMetadata)
    		{
	    		if (financialInstitutionTypeCodeAttribute!= null)
	    		{
	    			try
	    			{
	    				financialInstitutionTypeCode = financialInstitutionTypeCodeAttribute.get().toString();
	    			}
	    			catch (Exception e)
	    			{
	    				financialInstitutionTypeCode = null;
	    			}
	    		}
	    		else
	    		{
	    			_LOGGER.info("El tipo de entidad financiera es nulo para el usuario: " + currentLogin);
	    			_LOGGER.info("Procedemos a extraer el tipo de entidad financiera del login utilizado para la autenticacion");
	    			financialInstitutionTypeCode = getEntityTypeCodeFromLogin(currentLogin);
	    		}
	    		
	    		if (financialInstitutionCodeAttribute!= null)
	    		{
	    			try
	    			{
	    				//Aplicar padding de ceros a la izquierda para completar a 6 dIgitos
	    				String financialInstitutionCodeTemp = financialInstitutionCodeAttribute.get().toString(); 
	    				
	    				for (int i = 0 ; i < 6 - financialInstitutionCodeAttribute.get().toString().length(); i++)
	    				{
	    					financialInstitutionCodeTemp = "0" + financialInstitutionCodeTemp;
	    				} 
	    				
	    				financialInstitutionCode = financialInstitutionCodeTemp;
	    			}
	    			catch (Exception e)
	    			{
	    				financialInstitutionCode = null;
	    			}
	    		}
	    		else
	    		{
	    			_LOGGER.info("El codigo de la entidad financiera es nulo para el usuario: " + currentLogin);
	    			_LOGGER.info("Procedemos a extraer el codigo de la entidad financiera del login utilizado para la autenticacion");
	    			
	    			financialInstitutionCode = getEntityCodeFromLogin(currentLogin);
	    			
	    			if (financialInstitutionCode == null)
	    			{
	    				return false;
	    			}
	    			else
	    			{
	    				//Aplicar padding de ceros a la izquierda para completar a 6 dIgitos
	    				String financialInstitutionCodeTemp = financialInstitutionCode; 
	    				
	    				for (int i = 0 ; i < 6 - financialInstitutionCode.length(); i++)
	    				{
	    					financialInstitutionCodeTemp = "0" + financialInstitutionCodeTemp;
	    				} 
	    				
	    				financialInstitutionCode = financialInstitutionCodeTemp;
	    			}
	    		}
    		}
    		
    		//
        	// Step No. 3. Authenticate like the current user
        	dirContext = ldapConnectionBusinessLogic (initialContextFactory, providerURL, authenticationMode, financialInstitutionPrincipal, currentPassword);
        	
        	if (dirContext == null)
        	{
        		_LOGGER.info("Clave de acceso invalida para el usuario en el directorio LDAP.  FinancialInstitutionPrincipal: {} -  Usuario: {} ", financialInstitutionPrincipal, searchFilter);
        		return false;
        	}
        	else
        	{
        		this.dirContext = dirContext;
        		this.financialInstitutionPrincipal  = financialInstitutionPrincipal;
        		return true;
        	}
    	}
	}
	
	/**
	 * @param currentLogin
	 * @return the entity code from the user login or null if this code does not exists
	 */
	private String getEntityCodeFromLogin(String currentLogin)
	{
		if (currentLogin.length() > 4)
		{
			String suffix = currentLogin.substring(currentLogin.length() - 4);
			String entityCode = suffix.substring(2);
			if (isNumeric(entityCode))
			{
				return "0000" + entityCode;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		
	}
	
	/**
	 * @param currentLogin
	 * @return the entity code type from the user login  or null if this code does not exists
	 */
	private String getEntityTypeCodeFromLogin(String currentLogin)
	{
		if (currentLogin.length() > 4)
		{
			String suffix = currentLogin.substring(currentLogin.length() - 4);
			String entityTypeCode = suffix.substring(0, 2);
			
			if (isNumeric(entityTypeCode))
			{
				return "0"+entityTypeCode;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	private boolean isNumeric(String str)
	{
		try
		{
			Double.parseDouble(str);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
	
	/**
	 * LDAP connection business logic
	 * 
	 * @param initialContextFactory
	 * @param providerURL
	 * @param authenticationMode
	 * @param principal
	 * @param password
	 * @return Directory context or null if authentication credential are wrong
	 */
	private DirContext ldapConnectionBusinessLogic (String initialContextFactory, String providerURL, String authenticationMode, String principal, String password)
	{
        try 
        {
        	Hashtable<String, String> environment = new Hashtable<String, String>();
        	
        	//
        	// Try to get in with user role
			environment.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
			environment.put(Context.PROVIDER_URL, providerURL);
			environment.put(Context.SECURITY_AUTHENTICATION, authenticationMode);
			environment.put(Context.SECURITY_PRINCIPAL, principal);
			environment.put(Context.SECURITY_CREDENTIALS,password);
			
            DirContext dirContext = new InitialDirContext(environment);
            
            return dirContext;
        }
        catch (Exception e)
        {
        	_LOGGER.info("Falla al momento de ingresar al sistema: {}", principal);
        	return null;
        }
	}
	
	/**
	 * Find account by account name
	 * 
	 * @param dirContext
	 * @param ldapSearchBase
	 * @param searchFilter
	 * 
	 * @return LDAP search result
	 */
	private SearchResult findAccountByAccountName(DirContext dirContext, String ldapSearchBase, String searchFilter)
	{
		try
		{
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> results = dirContext.search(ldapSearchBase, searchFilter, searchControls);
			
			SearchResult searchResult = null;
			
			if(results.hasMoreElements()) 
			{
				searchResult = results.nextElement ( );
				
				//
				// Make sure there is not another item available, there should be only 1 match
				if(results.hasMoreElements()) 
				{
					_LOGGER.info("Multiples usuarios encontrados para el misma cuenta de usuario: {}", searchFilter);
					return null;
				}
			}
			
			return searchResult;
		}
        catch (Exception e)
        {
        	_LOGGER.info("Falla al momento de buscar una cuenta de usuario por nombre: {}", searchFilter);
        	return null;
        }
	}
	
	/**
	 * Loads the system properties
	 */
	private void loadSystemProperties ()
	{
		this.systemProperties = propertyList.getResultList();
	}
	
	/**
	 * Returns a specific property from a given key
	 * @param key
	 * @return associated property or null
	 */
	public String getSpecificProperty (String key)
	{
		if (this.systemProperties != null)
		{
			for (Property currentProperty : this.systemProperties)
			{
				if (currentProperty.getName().equalsIgnoreCase(key))
				{
					return currentProperty.getValue();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the financial institution object according the financial institution code.
	 * Set the financial institution code to null if the user has administrator role.
	 * 
	 * @param financialInstitutionCode financial institution code
	 * @param financialInstitutionTypeCode financial institution type code (unified code)
	 * @return financial institution object
	 */
	private FinancialInstitution getFinancialInstitution (String financialInstitutionCode, String financialInstitutionTypeCode)
	{
		if (financialInstitutionCode != null && !financialInstitutionCode.trim().equals("") && financialInstitutionTypeCode != null && !financialInstitutionTypeCode.trim().equals(""))
		{
			financialInstitutionList.getFinancialInstitution().setCode(financialInstitutionCode);
			financialInstitutionList.getFinancialInstitution().getFinancialInstitutionType().setCode(financialInstitutionTypeCode);
			List<FinancialInstitution> result = financialInstitutionList.getResultList();
			
			if (result.size() > 0)
			{
				return result.get(0);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * @param oldPassword
	 * @param newPassword
	 * @return true if the password is changed successfully a false otherwise
	 */
	public boolean changePassword(String oldPassword, String newPassword)
    {
        try
        {
    		//change password is a single LDAP modify operation
            //that deletes the old password and adds the new password
            ModificationItem[] mods = new ModificationItem[2];
       
            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("userpassword", oldPassword));
            mods[1] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("userpassword", newPassword));

            // Perform the update
            this.dirContext.modifyAttributes(this.financialInstitutionPrincipal, mods);
            
            _LOGGER.info("Cambio realizado para: {}", this.financialInstitutionPrincipal);
			
            this.dirContext.close();
            
            return true;
        }
        catch (NamingException e) 
        {
        	_LOGGER.info("Problema al cambiar el password a nivel del LDAP: {}", e.getMessage());
            return false;
        }
    }
	
	/**
	 * @return the dirContext
	 */
	public DirContext getDirContext()
	{
		return dirContext;
	}

	/**
	 * @param dirContext the dirContext to set
	 */
	public void setDirContext(DirContext dirContext)
	{
		this.dirContext = dirContext;
	}

	/**
	 * @return the financialInstitution
	 */
	public FinancialInstitution getFinancialInstitution()
	{
		return financialInstitution;
	}


	/**
	 * @return the financialInstitutionCode
	 */
	public String getFinancialInstitutionCode()
	{
		return financialInstitutionCode;
	}


	/**
	 * @return the authenticationResult
	 */
	public boolean isAuthenticationResult()
	{
		return authenticationResult;
	}


	/**
	 * @return the systemProperties
	 */
	public List<Property> getSystemProperties()
	{
		return systemProperties;
	}

	/**
	 * @return the businessProcess
	 */
	public List<BusinessProcess> getBusinessProcess()
	{
		return businessProcess;
	}

	/**
	 * @param businessProcess the businessProcess to set
	 */
	public void setBusinessProcess(List<BusinessProcess> businessProcess)
	{
		this.businessProcess = businessProcess;
	}

	/**
	 * @param systemProperties the systemProperties to set
	 */
	public void setSystemProperties(List<Property> systemProperties)
	{
		this.systemProperties = systemProperties;
	}

	/**
	 * @param financialInstitution the financialInstitution to set
	 */
	public void setFinancialInstitution(FinancialInstitution financialInstitution)
	{
		this.financialInstitution = financialInstitution;
	}


	/**
	 * @param financialInstitutionCode the financialInstitutionCode to set
	 */
	public void setFinancialInstitutionCode(String financialInstitutionCode)
	{
		this.financialInstitutionCode = financialInstitutionCode;
	}


	/**
	 * @param authenticationResult the authenticationResult to set
	 */
	public void setAuthenticationResult(boolean authenticationResult)
	{
		this.authenticationResult = authenticationResult;
	}

	/**
	 * @return the financialInstitutionTypeCode
	 */
	public String getFinancialInstitutionTypeCode()
	{
		return financialInstitutionTypeCode;
	}

	/**
	 * @param financialInstitutionTypeCode the financialInstitutionTypeCode to set
	 */
	public void setFinancialInstitutionTypeCode(String financialInstitutionTypeCode)
	{
		this.financialInstitutionTypeCode = financialInstitutionTypeCode;
	}
	
}
