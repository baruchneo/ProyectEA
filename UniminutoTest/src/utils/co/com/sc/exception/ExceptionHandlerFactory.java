package co.com.sc.exception;

/**
 * Generic exception handler factory
 * @author Alex Vicente Chacon Jimenez (alex.chacon@software-colombia.com)
 */
public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory
{
	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	private final javax.faces.context.ExceptionHandlerFactory parent;

	//---------------------------------------------------------------//
	// Class constructors
	//---------------------------------------------------------------//

	public ExceptionHandlerFactory(final javax.faces.context.ExceptionHandlerFactory parent) 
	{
		this.parent = parent;
	}
	 
	 @Override
	 public ExceptionHandler getExceptionHandler() 
	 {
		 return new ExceptionHandler(this.parent.getExceptionHandler());
	 }
}
