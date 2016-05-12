package co.com.sc.nexura.superfinanciera.action.security;

import co.com.mic.medicalUMD.modelo.Usuario;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;

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
	 * Administrador
	 */
	private static final String _ADMIN_ROLE = "Admin";

	/**
	 * Adminitrador default password
	 */
	private static final String _USER_PASSWORD = "Admin2012";
	
	/**
	 * Administrador default application role
	 */
	private static final String _USER_ROLE = "Admin";
	
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
	Usuario usuario;
	
	/**
	 * User associated authentication response
	 */
	private boolean authenticationResult = false;
	
	
	/**
	 * Administrator user authentication flag
	 */
	private boolean adminUserAuthentication = false;

	/*
	 * @return true if the user exists and false otherwise.
	 */
	public boolean authenticate() 
	{
		return authenticate(credentials.getUsername(), credentials.getPassword());
	}

	@Override
	public String RedirectUserFromValores() {
		return null;
	}

	/**
	 * @param login user login
	 * @param password user password
	 * @return true if the user exists and false otherwise
	 */
	public boolean authenticate(String login, String password) 
	{
		if(!login.trim().isEmpty() && !password.trim().isEmpty())
		{
			_LOGGER.info("Usuario conectado por defecto {}", login);



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
		
 		//if (ldapAuthenticacion (login, password))
		if(true)
		{

		}
		else
		{
			authenticationResult = false;
		}
		return authenticationResult;
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
	 * @return the authenticationResult
	 */
	public boolean isAuthenticationResult()
	{
		return authenticationResult;
	}

	/**
	 * @param authenticationResult the authenticationResult to set
	 */
	public void setAuthenticationResult(boolean authenticationResult)
	{
		this.authenticationResult = authenticationResult;
	}

}
