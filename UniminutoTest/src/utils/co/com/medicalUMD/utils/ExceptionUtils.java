package co.com.medicalUMD.utils;

import javax.faces.context.FacesContext;
import java.io.PrintWriter;
import java.io.StringWriter;
public class ExceptionUtils
{
	public static String exceptionGetStackTrace(String parameter) 
	{
	    StringWriter stringWriter = new StringWriter();
	    Exception exception = (Exception)getParameterSession(parameter);
	    exception.printStackTrace(new PrintWriter(stringWriter, true));
	    return stringWriter.toString();
	}
	
	public static String exceptionGetMessage(String parameter) 
	{
		Exception e = (Exception)getParameterSession(parameter);
		return e.getMessage();
	}
	
	public static String exceptionGetLocalizedMessage(String parameter) 
	{
		Exception exception = (Exception)getParameterSession(parameter);
		delParameterSession(parameter);
	    return exception.getLocalizedMessage();
	}
	
	public static Object getParameterSession(String key){
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
	}
	
	public static void delParameterSession(String key){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key); 
	}
}