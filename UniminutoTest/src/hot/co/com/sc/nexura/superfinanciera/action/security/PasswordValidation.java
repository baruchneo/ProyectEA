package co.com.sc.nexura.superfinanciera.action.security;

import javax.ejb.Local;

@Local
public interface PasswordValidation
{

	void storeNewPassword ( );

	String invalidateSession ( );
}