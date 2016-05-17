package co.com.medicalUMD.exception;

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
