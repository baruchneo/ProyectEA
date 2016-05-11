package co.com.sc.nexura.superfinanciera.action.security;

import co.com.sc.nexura.superfinanciera.action.admin.FinancialInstitutionList;
import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
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

			identity.addRole(_USER_ROLE);

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
		
 		//if (ldapAuthenticacion (login, password))
		if(true)
		{
			if (financialInstitutionCode != null && !financialInstitutionCode.trim().equals("") && financialInstitutionTypeCode != null && !financialInstitutionTypeCode.trim().equals(""))
			{	
				//setExternalUserEnvironment(login);
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
						
						//setExternalUserEnvironment(login);
						
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
	
	//---------------------------------------------------------------//
	// Business methods
	//---------------------------------------------------------------//


	
	
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
