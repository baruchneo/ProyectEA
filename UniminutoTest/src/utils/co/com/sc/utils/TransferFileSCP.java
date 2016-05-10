package co.com.sc.utils;

import com.jcraft.jsch.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferFileSCP {
	
	// ---------------------------------------------------------------//
	// Class attributes
	// ---------------------------------------------------------------//
	String host;
	int port;
	String user;
	String password;
	Integer timeout=30000;
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(TransferFileSCP.class);
	

	// ---------------------------------------------------------------//
	// Class constructors methods
	// ---------------------------------------------------------------//
	public TransferFileSCP(String host, int port, String user, String password)
	{
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}


	/**
	 * Copy file to Remote server
	 * 
	 * @param pathFile Remote directory file
	 * @param nameFile File Name
	 * @param pathLocal Local file path
	 */
	public void transferingRemoteFile(String pathFile,String nameFile, String pathLocal) 
	{

		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel=null;
		Channel channel = null;

		try 
		{
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.setTimeout(timeout);
			session.connect();
			channel = session.openChannel("sftp");
			_LOGGER.info("Conectandose al servidor:{}:{}",host,port);
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			File file = new File(pathLocal+"//"+nameFile);
			sftpChannel.cd(pathFile);
			FileInputStream in = new FileInputStream(file);
			_LOGGER.info("Enviando el archivo:{}",nameFile);
			sftpChannel.put(in,nameFile);
			sftpChannel.exit();
			sftpChannel.disconnect();
			channel.disconnect();
			session.disconnect();
			in.close();
		} 
		catch (JSchException e) 
		{
			_LOGGER.error("Error al momento de enviar el archivo al servidor remoto : {}", e.getMessage());
		} 
		catch (SftpException e) 
		{
			if(ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id)
			{
				_LOGGER.error("El archivo '{}' no existe", pathFile+"//"+nameFile);
			}
			else
			{
				_LOGGER.error("Error al momento de enviar el archivo al servidor remoto : {}", e.getMessage());
			}
		} 
		catch (FileNotFoundException e) 
		{
			_LOGGER.error("Error al momento de enviar el archivo al servidor remoto (Archivo ni encontrado) : {}", e.getMessage());
		} 
		catch (Exception e) 
		{
			_LOGGER.error("Error al momento de enviar el archivo al servidor remoto (General Exception) : {}", e.getMessage());
		} 
		finally
		{
			if(sftpChannel!=null)
			{
				sftpChannel.exit();
				sftpChannel.disconnect();
			}
			if(channel!=null)
				channel.disconnect();
			if(session!=null)
				session.disconnect();
		}
	}

	/**
	 * Reading Remote file
	 * @param pathRemoteFile
	 * @return file Content String 
	 */
	public String readingRemotefile(String pathRemoteFile, String destinationPath) 
	{

		JSch jsch = new JSch();
		Session session = null;
		String resultReadeing = null;
		InputStream inputStream = null;
		ChannelSftp sftpChannel = null;
		Channel channel = null;
		OutputStream outputStream = null;
		
		try 
		{
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.setTimeout(timeout);
			
			_LOGGER.info("Conectandose al servidor:{}:{} ",host + "- Usuario: " + user + " - Password: [informacion cifrada]" ,port );
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			
			_LOGGER.info("Copiando el archivo remoto al directorio local:{}",pathRemoteFile);
			File file =  new File(destinationPath);
			outputStream = new FileOutputStream(file);
			inputStream = sftpChannel.get(pathRemoteFile);
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
			outputStream.close();
		
			_LOGGER.info("Leyendo el archivo local como una cadena de texto:{}",destinationPath);
			resultReadeing = FileUtils.readFileToString(file, "UTF-8");
			
			sftpChannel.rm(pathRemoteFile);
		}
		catch (JSchException e) 
		{
			_LOGGER.error("Error al momento de leer un archivo del servidor remoto : {}", e.getMessage());
		} 
		catch (SftpException e) 
		{
			if(ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id)
			{
				_LOGGER.error("El archivo '{}' no existe", pathRemoteFile);
			}
			else
			{
				_LOGGER.error("Error al momento de leer un archivo del servidor remoto : {}", e.getMessage());
			}
		} 
		catch (FileNotFoundException e) 
		{
			_LOGGER.error("Error al momento de leer un archivo del servidor remoto (Archivo no encontrado) : {}", e.getMessage());
		} 
		catch (IOException e)	
		{
			_LOGGER.error("Error al momento de leer un archivo del servidor remoto (Problemas de lectura) : {}", e.getMessage());
		} 
		catch (Exception e) 
		{
			_LOGGER.error("Error al momento de leer un archivo del servidor remoto (General Exception): {}", e.getMessage());
		} 
		finally
		{
			try
			{
				if(sftpChannel!=null)
				{
					sftpChannel.exit();
					sftpChannel.disconnect();
				}
				if(channel!=null)
					channel.disconnect();
				if(session!=null)
					session.disconnect();
				if (inputStream != null)
					inputStream.close();
			}
			catch (Exception e)
			{
				_LOGGER.error("Error al momento de cerrar los recursos: {}", e.getMessage());
			}
			
		}
		return resultReadeing;
	}
	
	public Boolean removeFile(String path, Boolean remote)
	{
		Boolean deleted = Boolean.FALSE;
		ChannelSftp sftpChannel=null;
		Channel channel = null;
		
		if(path!=null && path.length() > 0)
		{
			if(!remote)
			{
				File file = new File(path);
				deleted = file.delete();
			}
			else
			{
				JSch jsch = new JSch();
				Session session = null;
	
				try 
				{
					session = jsch.getSession(user, host, port);
					session.setConfig("StrictHostKeyChecking", "no");
					session.setPassword(password);
					session.setTimeout(timeout);
					_LOGGER.info("Conectandose al servidor:{}:{}",host,port);
					session.connect();
					channel = session.openChannel("sftp");
					channel.connect();
					sftpChannel = (ChannelSftp) channel;
					_LOGGER.info("Borrando archivo:{}",path);
					sftpChannel.rm(path);
					deleted = Boolean.TRUE;
					sftpChannel.exit();
					sftpChannel.disconnect();
					channel.disconnect();
					session.disconnect();
				} 
				catch (JSchException e) 
				{
					_LOGGER.error("Error al momento de borrar un archivo del servidor remoto : {}", e.getMessage());
				} 
				catch (SftpException e) 
				{
					if(ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id)
					{
						_LOGGER.error("El archivo '{}' no existe", path);
					}
					else
					{
						_LOGGER.error("Error al momento de borrar un archivo del servidor remoto : {}", e.getMessage());
					}
				} 
				catch (Exception e) 
				{
					_LOGGER.error("Error al momento de borrar un archivo del servidor remoto (General Exception) : {}", e.getMessage());
				}
				finally
				{
					if(sftpChannel!=null)
					{
						sftpChannel.exit();
						sftpChannel.disconnect();
					}
					if(channel!=null)
						channel.disconnect();
					if(session!=null)
						session.disconnect();
				}
		
			}
		}
		return deleted;
	}
	
	
	public void move(String originalPath, String targetPath)
	{
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel=null;
		Channel channel = null;

		try 
		{
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.setTimeout(timeout);
			session.connect();
			channel = session.openChannel("sftp");
			
			_LOGGER.info("Conectandose al servidor:{}:{}",host,port);
			
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			sftpChannel.rename(originalPath, targetPath);
			
			_LOGGER.info("Moviendo el archivo desde  {} a {}",originalPath, targetPath);
			
			sftpChannel.exit();
			sftpChannel.disconnect();
			channel.disconnect();
			session.disconnect();
		} 
		catch (JSchException e) 
		{
			_LOGGER.error(e.getMessage()) ;
		} 
		catch (SftpException e) 
		{
			if(ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id)
			{
				_LOGGER.error("El archivo '{}' no existe", originalPath);
			}else
			{
				_LOGGER.error(e.getMessage()) ;
			}
		}  
		catch (Exception e) 
		{
			_LOGGER.error(e.getMessage()) ;
		} 
		finally
		{
			if(sftpChannel!=null)
			{
				sftpChannel.exit();
				sftpChannel.disconnect();
			}
			if(channel!=null)
				channel.disconnect();
			if(session!=null)
				session.disconnect();
		}
	}
	
	
	
	static public void copyFileLocal(String pathName,String FileName, File dataFile)
	{
		copyFile(pathName,FileName, dataFile, null, false, null, 0, null, null);
	}
	
	
	static public void copyFile(String pathName,String FileName, File dataFile, String pathReference, boolean remoteCopy, String scpMachine, int scpPort, String scpUser, String scpPassword)
	{
		FileOutputStream fos;
		FileChannel out;
		FileChannel in;
		try
		{
			// remote copy
			if (remoteCopy)
			{
				 TransferFileSCP transferFileSCP = new TransferFileSCP(scpMachine, scpPort, scpUser, scpPassword);
				 transferFileSCP.transferingRemoteFile(pathName,FileName,pathReference);
			}
			else
			{
				File file = new File(pathName);
				file.mkdirs();
				if(!file.exists()){
					_LOGGER.error("No existe el directorio con el nombre {}", pathName);
				}
				else
				{					
					if(dataFile==null && pathReference!=null)
					{
						dataFile = new File(pathReference+"//"+FileName);
					}
					fos = new FileOutputStream(pathName+"//"+FileName);
					out = fos.getChannel();
					assert dataFile != null;
					in = new FileInputStream(dataFile).getChannel();
					in.transferTo(0, in.size(), out);
					in.close();
					out.close();
					fos.close();
				}
			}
				
		}
		catch (Exception e)
		{
			_LOGGER.error("Problemas al tratar de copiar un archivo. Descripcion : {}", e);
		}

	}
	
	static public byte[] fileToByteArray(String path)
	{
		byte[] buf = null;
		File file = new File(path);
		InputStream fis;
		try
		{
			fis = new FileInputStream(file);

			buf = IOUtils.toByteArray(fis);
			fis.close();
			
		}
		catch (FileNotFoundException e)
		{
			_LOGGER.error("Error al momento de recuperar los bytes de un archivo (Archivo no encontrado) : {}", e.getMessage());
		}
		catch (IOException e)
		{
			_LOGGER.error("Error al momento de recuperar los bytes de un archivo (Permisos de lectura) : {}", e.getMessage());
		}	
		return buf;
	}
	
	/**
	 * Test a remote connection
	 * @param host host param
	 * @param port port param
	 * @param user user param
	 * @param passwd password param
	 * @throws JSchException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void testRemoteConection ( String host, int port, String user, String passwd ) throws Exception
	{
		JSch jsch = new JSch();
		Session session;
		Channel channel;
		session = jsch.getSession(user, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(passwd);
		session.setTimeout(3000);
		session.connect();
		channel = session.openChannel("sftp");
		_LOGGER.info("Conectandose al servidor:{}:{}",host,port);
		channel.connect();
		if(!channel.isConnected())
		{
			channel.disconnect();
			throw new IOException("Error en los datos de conexi\u00f3n.");
			
		}
		channel.disconnect();
	}
	
	/**
	 * Verify remote directory Exist, and permissions in this directory
	 * @param host host param
	 * @param port port param
	 * @param user usera param
	 * @param passwd password param
	 * @param directory directories param
	 * @throws SftpException
	 * @throws JSchException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void validateDirectoryPermission ( String host, int port, String user, String passwd, String directory, String permissionsER ) throws Exception
	{
		JSch jsch = new JSch();
		Session session;
		Channel channel;
		ChannelSftp sftpChannel;
		SftpATTRS sftpATTRS;
		String permisions;
		session = jsch.getSession(user, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(passwd);
		session.setTimeout(3000);
		session.connect();
		channel = session.openChannel("sftp");
		channel.connect();
		sftpChannel = (ChannelSftp) channel;
		sftpATTRS = sftpChannel.stat(directory);
		permisions = sftpATTRS.getPermissionsString();
		_LOGGER.info("permisos del directorio:{} :[{}]",directory,permisions);
		Pattern pattern = Pattern.compile(permissionsER);
		Matcher matcher = pattern.matcher(permisions);
		if(!matcher.find())
		{
			throw new SftpException(ChannelSftp.SSH_FX_PERMISSION_DENIED, "el directorio '" + directory + "' no tiene permisos de escritura para el usuario: " + user);
		}
		
		
	}

	/**
	 * Get the name file in linux fs
 	 * @param path path with name
	 * @return name in linux fs
     */
	static public String getNameFile (String path)
	{
		String nameFile = path.replace("//", "/");
		nameFile = nameFile.replace("\\", "/");
		int i = nameFile.lastIndexOf("/");
		nameFile = nameFile.substring(i+1, nameFile.length());
		return nameFile;
	}

	/**
	 * Get the path in linux fs
	 * @param path path param
	 * @return path in linux fs
     */
	static public String getPathFile (String path)
	{
		String pathFile = path.replace("//", "/");
		pathFile = pathFile.replace("\\", "/"); 
		int i = pathFile.lastIndexOf("/");
		pathFile = pathFile.substring(0,i);
		return pathFile;
	}


	/**
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}




	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host = host;
	}




	/**
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}




	/**
	 * @param port the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}




	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}




	/**
	 * @param user the user to set
	 */
	public void setUser(String user)
	{
		this.user = user;
	}




	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}




	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}




	/**
	 * @return the timeout
	 */
	public Integer getTimeout()
	{
		return timeout;
	}




	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}
	
	
	
}
