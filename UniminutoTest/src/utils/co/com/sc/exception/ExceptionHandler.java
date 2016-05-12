package co.com.sc.exception;

import java.util.Iterator;

import javax.ejb.EJBException;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;

import org.jboss.seam.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import co.com.sc.nexura.superfinanciera.action.user.SendingUploadBean;

/** 
 * Generic exception handler
 * 
 * @author Alex Vicente Chacon Jimenez (alex.chacon@software-colombia.com)
 */
public class ExceptionHandler extends ExceptionHandlerWrapper
{
	// ---------------------------------------------------------------//
	// Class constants
	// ---------------------------------------------------------------//

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

	private final javax.faces.context.ExceptionHandler wrapped;
	
	private final static String _EXCEPTION_SIMPLE_NAME = "exceptionSimpleName";
	
	private final static String _EXCEPTION = "exception";
	
	private final static String _ERROR_PAGE = "/error.seam";
	
	// ---------------------------------------------------------------//
	// Class constructors
	// ---------------------------------------------------------------//

	public ExceptionHandler(final javax.faces.context.ExceptionHandler wrapped)
	{
		this.wrapped = wrapped;
	}

	@Override
	public javax.faces.context.ExceptionHandler getWrapped()
	{
		return this.wrapped;
	}

	// ---------------------------------------------------------------//
	// Business methods
	// ---------------------------------------------------------------//
	
	@Override
	public void handle() throws FacesException 
	{
		for (final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext();) 
		{
			Throwable t = it.next().getContext().getException();
			
			while ((t instanceof FacesException || t instanceof EJBException || t instanceof ELException) && t.getCause() != null) 
			{
				t = t.getCause();
			}
			
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			final ExternalContext externalContext = facesContext.getExternalContext();
			try 
			{
                facesContext.renderResponse();  
				
				LOG.info("{}: {}", t.getClass().getSimpleName(), t.getMessage());
				LOG.error("Descripcion del error:", t);
				
				externalContext.getSessionMap().put(_EXCEPTION_SIMPLE_NAME, t.getClass().getSimpleName());
				externalContext.getSessionMap().put(_EXCEPTION, t);
				externalContext.redirect(externalContext.getRequestContextPath() + _ERROR_PAGE);
				facesContext.responseComplete();
				//SendingUploadBean sendingUpload = (SendingUploadBean) Component.getInstance(SendingUploadBean.class,Boolean.TRUE);
				//sendingUpload.cleanForm();
			}
			catch (Exception e) 
			{
				LOG.error("Error no controlado", e);
			}
			finally 
			{
				it.remove();
			}
		}
		
		getWrapped().handle();
	}
}
