package co.com.sc.nexura.superfinanciera.action.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.Component;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.sc.nexura.superfinanciera.action.admin.SendingList;
import co.com.sc.nexura.superfinanciera.model.Sending;
import co.com.sc.utils.TransferFileSCP;

public class DownloaderResponse extends HttpServlet{
	
	
	//---------------------------------------------------------------//
	//	  class constant
	//---------------------------------------------------------------//
	private static final String _PARAM_SENDING="idSending";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(uploadServlet.class);
	
	
	// ---------------------------------------------------------------//
	// Class business methods
	// ---------------------------------------------------------------//

	protected void service(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException
	{
		

		new ContextualHttpServletRequest(request)
		{
			@Override
			public void process() throws Exception
			{
				doWork(request, response);
			}
		}.run();
	}
	
	private void doWork(HttpServletRequest request, HttpServletResponse response)
	{
		String idSending=  request.getParameter(_PARAM_SENDING);
		
		SendingList sendingList = (SendingList) Component.getInstance("sendingList");
		sendingList.getSending().setId(Long.parseLong(idSending));
		
		Sending sending =  sendingList.getSingleResult();
		
		downloadFile(sending,response, request);
		
	}
	

	public void downloadFile(Sending sending, HttpServletResponse response, HttpServletRequest request)
	{
		
		String pathResponse = sending.getSendingForValidation() ? sending.getResponseValidationPath():sending.getResponseFinalPath();
		
		byte[] buf = TransferFileSCP.fileToByteArray(pathResponse);
		if(buf!=null){
			String nameFile = TransferFileSCP.getNameFile(pathResponse);
			showFileResponse(buf, nameFile,response, request);
		}else{
			_LOGGER.error("El archivo de respuesta no esta disponible: {}",pathResponse);
			FacesMessages.instance().add("El archivo de respuesta no esta disponible");
		}

	}

	public static void showFileResponse(byte[] bytes, String outName, HttpServletResponse response, HttpServletRequest request)
	{
		response.setHeader("Cache-Control", "public");
		response.setHeader("Pragma", "public");
		
		/*if (request.isSecure())
		{
			response.setHeader("Cache-Control", "public");
			response.setHeader("Pragma", "public");
		}
		else
		{
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
		}*/
		response.setDateHeader("Expires", 0);
		response.setContentType("text/plain");
		response.addHeader("Content-Disposition", "attachment;filename="+ outName);
		response.setContentLength(bytes.length);
		ServletOutputStream servletOutputStream = null;

		try
		{
			servletOutputStream = response.getOutputStream();
			servletOutputStream.write(bytes, 0, bytes.length);
			servletOutputStream.flush();
			servletOutputStream.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (servletOutputStream != null)
				try
				{
					servletOutputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}

}
