package co.com.sc.nexura.superfinanciera.action.user;

import co.com.sc.nexura.superfinanciera.model.PropertyEnum;
import co.com.sc.utils.FilesCopier;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.seam.Component;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class uploadServlet extends HttpServlet
{

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	//---------------------------------------------------------------//
	//	  class constant
	//---------------------------------------------------------------//
	
	private static final String OPTION_FILE_FIELD="pOptionFile";
	
	private static final String OPTION_NOTE="note";
	
	private static final String OPTION_DEFINITIVE="definitive";
	
	//---------------------------------------------------------------//
	//	  class attributes
	//---------------------------------------------------------------//
	
	/**
	 * 	option for upload file note or definitive
	 */
	private String optionFileUploader;	 
	 
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(uploadServlet.class);

	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
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

	/**
	 * Servlet Upload files
 	 * @param request web page send file
	 * @param response generate for load file
     */
	private void doWork(HttpServletRequest request, HttpServletResponse response)
	{
		
		HttpSession session = request.getSession(false);
		if (session == null) 
		{
			return;
		}
		
		if(!(request.getContentType().contains("multipart/form-data")))
		{
			return;
		}
		
		SendingUploadBean bean = (SendingUploadBean) Component.getInstance("sendingUpload");
		if (bean.authenticator == null)
		{
			_LOGGER.info("La sesion del usuario ha caducado. El usuario debera ingresar por la interfaz de autenticacion.");
			return;
		}
		
		try
		{
			
			String idSession = request.getSession().getId();
			
			optionFileUploader=  request.getParameter(OPTION_FILE_FIELD);
			_LOGGER.info("parametroFormulario: name={}, value = {}",OPTION_FILE_FIELD,optionFileUploader);
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();

			int partitionIndex = 0;
			int partitionCount = 0;
			String idFile="";
			String fileName = "";

			while (iter.hasNext())
			{
				FileItem item = iter.next ( );
				
				// 
				// checking if it's a file or a form field
				if (item.isFormField())
				{
					if(item.getFieldName().equals("qqpartindex"))
					{
						partitionIndex = Integer.parseInt(item.getString());
					}
					else if(item.getFieldName().equals("qqtotalparts"))
					{
						partitionCount = Integer.parseInt(item.getString());
					}
					else if(item.getFieldName().equals("qquuid"))
					{
						idFile = item.getString();
					}
					else if(item.getFieldName().equals("qqfilename"))
					{
						fileName = item.getString();
					}
				}
				else
				{
					partitionCount = (partitionCount == 0)? 1 : partitionCount;

					String fieldName = item.getFieldName();
					String contentType = item.getContentType();
					boolean isInMemory = item.isInMemory();
					long sizeInBytes = item.getSize();					
					
					Object param[] = {fieldName, contentType,isInMemory,sizeInBytes};
					_LOGGER.info("FILE INFO: fieldName= {}, contentType= {}, isInMemory= {}, sizeInBytes= {}", param);
					
					String pathTmpUpload = bean.authenticator.getSpecificProperty(PropertyEnum.PROPERTY_NAME_TEMP_UPLOADED_FILES_PATH.getName());
					
					
					String fileNameFinal = optionFileUploader+idSession+idFile;
					String pathStorage = pathTmpUpload+File.separator+fileNameFinal;
					File partition = storagePartition(partitionIndex, item,pathStorage);
					
					File uploadedFile=new File(pathStorage+File.separator+fileName);
					
					FilesCopier.joinFiles(uploadedFile, partition);
					
					if (sizeInBytes > 0 && partitionIndex == (partitionCount-1))
					{
						
						File fileUnZip = extractZip(pathTmpUpload,uploadedFile,fileNameFinal);
						if(optionFileUploader.equals(OPTION_DEFINITIVE)){
							bean.getDataDefinitiveFiles().add(fileUnZip);
						}
						else if(optionFileUploader.equals(OPTION_NOTE))
						{
							if(bean.getDataNotesFile() == null)
							{
								bean.setDataNotesFile(fileUnZip);
							}
							else
							{
								bean.setDataNotesFile2(fileUnZip);
							}
							
						}
						
						File fileDelete = new File(pathStorage);
						FileUtils.deleteDirectory(fileDelete);
					}
				}
			}

			makeResponse(response, partitionCount);

		}
		catch (Exception e)
		{
			_LOGGER.info("Se ha producido un error al momento de subir los archivos. Descripcion del error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * create Json response
	 * @param response
	 */
	private void makeResponse(HttpServletResponse response, int partitionCount)
	{
		JSONObject jsonResponse;
		try
		{

			jsonResponse = new JSONObject();
			jsonResponse.put("success", true);
			jsonResponse.put(optionFileUploader, partitionCount);

			response.setContentType("application/json");
			response.getWriter().write(jsonResponse.toString());

		}
		catch (JSONException jse)
		{
			_LOGGER.info("Se ha producido un error al momento de crear el objeto json de respuesta. Descripcion del error: " + jse.getMessage());
			jse.printStackTrace();
		}
		catch (IOException e)
		{
			_LOGGER.info("Se ha producido un error al momento de escribir la respuesta a la peticiOn HttpResponse. Descripcion del error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Download a partition
	 * @param partitionIndex
	 * @param item
	 * @param pathStorage
	 * @return partition file
	 * @throws Exception
	 */
	private File storagePartition(int partitionIndex, FileItem item, String pathStorage) throws Exception
	{
			File tmpStoregePartitions = new File(pathStorage);
		if(!tmpStoregePartitions.exists())
		{
			if (!tmpStoregePartitions.mkdirs())
			{
				throw new Exception ("No se pudo crear el siguiente directorio: " + tmpStoregePartitions.getAbsolutePath());
			}
		}
		File partitionFile=new File(tmpStoregePartitions+File.separator+partitionIndex);
		item.write(partitionFile);
		return partitionFile;
	}
	
	/**
	 * extract a compressed file
	 * @param pathDestination
	 * @param fileZip
	 * @return unzipped file
	 */
	private File extractZip(String pathDestination, File fileZip, String fileName){
			byte[] buffer = new byte[1024];
			File fileUnZip = null;
			try
			{
				ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
				ZipEntry ze = zis.getNextEntry();
				if(ze==null){
					fileUnZip = createFile(pathDestination, fileName+fileZip.getName().replace(" ", ""));
					FileInputStream in = new FileInputStream(fileZip);
					FileOutputStream out = new FileOutputStream(fileUnZip);
					IOUtils.copy(in, out);
					in.close();
					out.close();
				}
				while(ze!=null){
					fileUnZip = createFile(pathDestination, fileName+ze.getName().replace(" ", ""));
					FileOutputStream fos = new FileOutputStream(fileUnZip);             
					 
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		       		fos.write(buffer, 0, len);
		            }
		 
		            fos.close();   
		            ze = zis.getNextEntry();
				}
				zis.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return fileUnZip;
	}

	private File createFile(String pathDestination, String fileName) throws IOException
	{
		File fileUnZip;
		fileUnZip = new File(pathDestination+File.separator+fileName);
		fileUnZip.createNewFile();
		return fileUnZip;
	}
	
}