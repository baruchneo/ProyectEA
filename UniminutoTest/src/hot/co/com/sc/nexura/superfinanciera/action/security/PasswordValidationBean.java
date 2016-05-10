package co.com.sc.nexura.superfinanciera.action.security;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.web.Session;
import co.com.sc.nexura.superfinanciera.action.admin.FinancialInstitutionHome;
import co.com.sc.utils.PasswordUtils;

@ManagedBean
@Name("validatePassword")
public class PasswordValidationBean implements PasswordValidation, Serializable 
{
	 
	
    //---------------------------------------------------------------//
  	// Class attributes
  	//---------------------------------------------------------------//
    
    
	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	

	@In
	Credentials credentials;
     
    /**
     * User login 
     */
    private String user = null;
    
    /**
     * Success operation
     */
    private boolean successfullyOperation = false;
    
    /**
     * Current password of the user
     */
    private String oldPassword = null; 
    
    @In
    AuthenticatorBean authenticator;
    
    @In (create=true)
    FinancialInstitutionHome financialInstitutionHome;
    
    /**
     * New password
     */
    @Size(min = 8, max = 25, message = "La longitud de la contrase\u00f1a debe ser entre {min} y {max} caracteres.")
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*()+=._-]+)*$", message = "Contrase\u00f1a inv\u00e1lida contiene caracteres no permitidos, como acentos y tildes o e\u00f1es")
    private String newPassword = null;
    
    /**
     * Re type new password
     */
    @Size(min = 8, max = 25, message = "La longitud de la contrase\u00f1a debe ser entre {min} y {max} caracteres.")
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*()+=._-]+)*$", message = "Contrase\u00f1a inv\u00e1lida contiene caracteres no permitidos, como acentos y tildes o e\u00f1es")
    private String reTypePassword = null;
    
    //---------------------------------------------------------------//
  	// Class getters methods
  	//---------------------------------------------------------------//
    
    /** 
     * @return newPassword
     */
    public String getNewPassword() 
    {
        return newPassword;
    }
    
    /**
     * @return reTypePassword
     */
    public String getReTypePassword() 
    {
        return reTypePassword;
    }
    
    /**
	 * @return the user
	 */
	public String getUser() 
	{
		return user;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() 
	{
		return oldPassword;
	}
    
    //---------------------------------------------------------------//
  	// Class setters methods
  	//---------------------------------------------------------------//
    
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) 
	{
		this.user = user;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) 
	{
		this.oldPassword = oldPassword;
	}

	/**
     * @param newPassword the newPassword to set 
     */
    public void setNewPassword(String newPassword) 
    {
        this.newPassword = newPassword;
    }
    
    /**
     * @param reTypePassword the reTypePassword to set
     */
    public void setReTypePassword(String reTypePassword) 
    {
        this.reTypePassword = reTypePassword;
    }
 
    //---------------------------------------------------------------//
  	// Business methods
  	//---------------------------------------------------------------//
    
    @AssertTrue(message = "Las contrase\u00f1as ingresadas son diferentes!")
    public boolean isPasswordsEquals() 
    {
        return newPassword.equals(reTypePassword);
    }
    
    @AssertFalse(message = "La contrase\u00f1a nueva no puede ser igual a la actual!")
    public boolean isPasswordsEqualsOld() 
    {
        return oldPassword.equals(newPassword);
    }
 
    /* (non-Javadoc)
	 * @see co.com.sc.nexura.superfinanciera.action.security.PasswordValidation#storeNewPassword()
	 */
    @Override
	public void storeNewPassword() 
    {
    	//boolean vOldPasswd = false;
    	boolean vnewPasswd = false;
    	boolean vRTPasswd = false;
    	String sha1NewPassword = "";
    	 	
    	
    	//1. Verificar el usuario y clave actuales.
    	   	
    	if (user.equals(credentials.getUsername()))
    	{
	    	if (newPassword.equals(reTypePassword))
	    	{
		    	if(authenticator.authenticate(user, oldPassword))
		    	{
		    		// 2. Verificar que la clave nueva cumpla con los requisistos correctos al igual que la verificacion.
			    	vnewPasswd = validatePasswordMinimunRequeriments(newPassword, "newPassword");
			    	vRTPasswd  = validatePasswordMinimunRequeriments(reTypePassword, "reTypePassword");
			    	
			    	// 3. calcular el resumen SHA1 de la clave
			    	sha1NewPassword = PasswordUtils.getSha1HashFromPassword(newPassword);
			    	
			    	// 4. Validar que la nueva clave no este en el historial de claves. 
			    	if(vnewPasswd && vRTPasswd)
			    	{
			    		if (!authenticator.getFinancialInstitution().checkPasswordsRepository(user, sha1NewPassword))
			    		{
			    			// 5. Realizar cambio contraseNa en capa media
		    				try
			    			{
		    					//Rollback case. Cant LDAP change password
		    					String rollbackDateHistoryPasswd = authenticator.getFinancialInstitution().getLastPasswordChange();
		    					String rollbackHistoryPasswd = authenticator.getFinancialInstitution().getPasswordHistory();
		    					
		    					authenticator.getFinancialInstitution().addLastDateForChangePassword(user);
		    					authenticator.getFinancialInstitution().addNewPasswordByUser(user, newPassword);
			    			
			    			
			    				financialInstitutionHome.setFinancialInstitutionId(authenticator.getFinancialInstitution().getId());
			    				financialInstitutionHome.getInstance().setLastPasswordChange(authenticator.getFinancialInstitution().getLastPasswordChange());
			    				financialInstitutionHome.getInstance().setPasswordHistory(authenticator.getFinancialInstitution().getPasswordHistory());
			    				financialInstitutionHome.update();
			    				
			    				// 6. Realizar cambio de clave contra Openldap
				    			///if(true)
				    			if(authenticator.changePassword(oldPassword, newPassword))  		
					    		{
				    				this.successfullyOperation = true;
				    			}
				    			else
				    			{
				    				this.successfullyOperation = false;
				    				
				    				financialInstitutionHome.getInstance().setLastPasswordChange(rollbackDateHistoryPasswd);
				    				financialInstitutionHome.getInstance().setPasswordHistory(rollbackHistoryPasswd);
				    				financialInstitutionHome.update();
				    				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error internos de autenticaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador.", "Error internos de autenticaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador."));
				    			}
			    			}
			    			catch(ConstraintViolationException e)
			    			{
			    				this.successfullyOperation = false;
			    				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de la aplicaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador.", "Error de la aplicaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador."));
			    			}
			    			catch(ValidationException e2)
			    			{
			    				this.successfullyOperation = false;
			    				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de la aplicaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador.", "Error de la aplicaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador."));
			    			}
			    			catch(Exception e3)
			    			{
			    				this.successfullyOperation = false;
			    				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error general de la aplicaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador.", "Error general de la aplicaci\u00f3n. Por favor p\u00f3ngase en contacto con el administrador."));
			    			}
			    		}
			    		else
			    		{
			    			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "La nueva contrase\u00f1a ya ha sido utilizada anteriormente. Recuerde que la nueva contrase\u00f1a no puede ser igual a las \u00DCltimas 5 contrase\u00f1as utilizadas.", "La nueva contrase\u00f1a ya ha sido utilizada anteriormente. Recuerde que la nueva contrase\u00f1a no puede ser igual a las \u00DCltimas 5 contrase\u00f1as utilizadas."));
			    		}
			    	}
		    	}
		    	else
				{
					FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contrase\u00f1a actuales no son correctas!", "Usuario o contrase\u00f1a actuales no son correctas!"));
				}
	    	}
	    	else
			{
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "La nueva contrase\u00f1a y su confirmaci\u00f3n no concuerdan.", "La nueva contrase\u00f1a y su confirmaci\u00f3n no concuerdan."));
			}
    	}
    	else
		{
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario especificado no corresponde con el usuario actualmente autenticado.", "El usuario especificado no corresponde con el usuario actualmente autenticado."));
		}
    }
    
    /* (non-Javadoc)
     * @see co.com.sc.nexura.superfinanciera.action.security.PasswordValidation#invalidateSession()
     */
    @Override
    public String invalidateSession()
    {
    	Session.instance().invalidate();
		return "home";
    }
    
    /**
     * This checks to see if the password is 8 chars min and contains letters
     * numbers and special chars
     * 
     * @param password
     * @return
     */
    public boolean validatePasswordMinimunRequeriments(String password, String id) 
    {
		String numeros      = "0123456789";
		String lUpper       = "QWERTYUIOPASDFGHJKLZXCVBNM";
		String lLower       = "qwertyuiopasdfghjklzxcvbnm";
		String lLatin       = "áéíóúÁÉÍÓÚñÑüÜ|";
		String message      = "La contrase\u00f1a debe poseer m\u00ednimo: ";
		boolean pn          = false;
		boolean pu          = false;
		boolean pl          = false;
		boolean pt          = true;
		boolean returnValue = true;
		
		for(int i=0; i<password.length(); i++)
		{
			if (numeros.indexOf(password.charAt(i),0)!=-1)
			{
				pn=true; 
			}
			if (lUpper.indexOf(password.charAt(i),0)!=-1)
			{
				pu=true;
			}
			if (lLower.indexOf(password.charAt(i),0)!=-1)
			{
				pl=true;
			}
			if (lLatin.indexOf(password.charAt(i),0)!=-1)
			{
				pt=false;
			}
		} 
		
		if (!pn) 
		{
			message = message + "un d\u00edgito, ";
			returnValue = false;
		}
		  
		if (!pu) 
		{
			message = message + "una letra may\u00fascula, ";
			returnValue = false;
		}
		
		if (!pl) 
		{
			message = message + "una letra min\u00fascula, ";
			returnValue = false;
		}
		
		if (!pt) 
		{
			message = "La contrase\u00f1a no debe poseer caracteres latinos, como tildes o e\u00f1es";
			returnValue = false;
		}
		
		if(!returnValue)
		{
			if(message.endsWith(", "))
			{
				message = message.substring(0, message.length()-2);
			}
			FacesContext.getCurrentInstance().addMessage(id,new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
		}
		return returnValue;
    }

	/**
	 * @return the successfullyOperation
	 */
	public boolean isSuccessfullyOperation()
	{
		return successfullyOperation;
	}

	/**
	 * @param successfullyOperation the successfullyOperation to set
	 */
	public void setSuccessfullyOperation(boolean successfullyOperation)
	{
		this.successfullyOperation = successfullyOperation;
	}
}
