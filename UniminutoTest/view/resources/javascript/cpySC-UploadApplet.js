/**
 * <p>Javascript bridge implemented in order to facilitate the use of the uploader applet</p>
 *
 * @author Diego Alejandro Duenas (diego.duenas@software-colombia.com)
 * @author Alex Vicente ChacOn JimEnez (alex.chacon@software-colombia.com)
 */

//---------------------------------------------------------------//
//Java script Constants
//---------------------------------------------------------------//


/**
 * Global applet code base
 */
var globalCodeBase = ".";

/**
 * Flag used to show alert message if an error occurs
 */
var makeNoise = true;

/**
 * uploader applet tag id name
 */
var uploaderAppletId = "jumpLoaderApplet";

/**
 * uploader applet main class
 */
var uploaderAppletMainClass = "jmaster.jumploader.app.JumpLoaderApplet.class";

/**
 * uploader applet JNLP class
 */
var uploaderAppletJNLPFile = "jumploader_z.jnlp";

/**
 * uploader applet JAR class
 */
var uploaderAppletJARFile = "jumploader_z.jar";

/**
 * uploader URL 
 */
var urlHandler = "../user/uploadServlet.do";

/**
 * uploader note tag id name  
 */
var noteDiv = "notePanel";

/**
 * uploader definitive tag id name
 */
var definitiveDiv = "definitivePanel";


/**
 * constant note file
 */
var noteOption = "note";

/**
 * constant definitive file
 */
var definitiveOption = "definitive";

var isUploadDefinitiveFile=false;

var isUploadNoteFile = false;

var hasNote = false;

var indexFileAdd=0;

var indexFileUploaded=0;

//---------------------------------------------------------------//
//Java script public helper methods
//---------------------------------------------------------------//

/**
 * Sets the value to the make noise value
 * @param newMakeNoiseValue value assigned to the flag makeNoise.
 */
function setMakeNoise(newMakeNoiseValue)
{
	makeNoise = newMakeNoiseValue;
}



/**
 * Shows an alert message if the flag attribute makeNoise is on
 * @param message Message to show
 */
function showAlertMessages(message)
{
	if (makeNoise)
	{
		alert(message);
	}
}

//---------------------------------------------------------------//
//Java script applet constructor
//---------------------------------------------------------------//

/**
 * Creates the uploader applet tag inside a HTML document. If the applet is already initialized and his current state is stopped, this method change the current state to started.
 * @param codebase Applet path code base
 * @param startRecordingFlag Start recording film flag
 * @returns True if the applet was correctly initialized. False otherwise.
 */
function createUploaderApplet(codebase, startRecordingFlag, optionFile, maxfile)
{
	try
	{
		setMakeNoise(startRecordingFlag);
		
		globalCodeBase = codebase;
		
		//
		//Step No. 0: Checks if Java is enabled
		if (!checkJavaPlugin())
		{
			return false;
		}
		else
		{
			//
			//Step No. 1: Creates applet configuration variables
			var parentObject;
			var paramFile;
			
			if(definitiveOption == optionFile)
			{
				parentObject = definitiveDiv;
			}
			else if(noteOption == optionFile)
			{
				parentObject = noteDiv;
			}
			
			
			var attributes =
			{
					id:uploaderAppletId,
					code:uploaderAppletMainClass,
					width:600,
					height:400
			};
			
			var parameters =
			{
					jnlp_href: codebase + uploaderAppletJNLPFile,
					uc_uploadUrl: urlHandler + "?pOptionFile="+optionFile,
					uc_resumeCheckUrl: urlHandler + "?pOptionFile="+optionFile,
					ac_fireUploaderFileRemoved:true,
					ac_fireUploaderFileAdded:true,
					ac_fireUploaderFilePartitionUploaded: true,
					uc_MaxFiles:maxfile,
					ac_messagesZipUrl:'messages_es.zip',
					uc_compressionMode:'zipOnAdd',
					uc_zipDirectoriesOnAdd:true,
					uc_directoriesEnabled:true,
					uc_partitionLength:2048000,
					uc_zipPartitions:false,
					uc_directoriesEnabled:false
			};
			
			var version = '1.8';
			
			//
			// Step No. 2: Creates the applet
			var intercepted = '';
			var got = document.write;
			document.write = function(arg){intercepted += arg;};
			
			deployJava.runApplet(attributes, parameters, version);
			
			document.write = got;
			//
			// Step No. 4: Shows the HTML div tag applet container
			showAppletPlaceTag(intercepted, parentObject);
			
			return true;
		}
	}
	catch (e)
	{
		showAlertMessages("Error en el modulo de carga de archivos: " + e.message);
		return false;
	}
}


//---------------------------------------------------------------//
// Applet event
//---------------------------------------------------------------//

/**
 * File partition uploaded notification
 */
function uploaderFilePartitionUploaded( uploader, file ) 
{
	
	var dataResponse = null;
	
	try
	{
		dataResponse = file.getResponseContent().split(':');
	}
	catch (e)
	{
		showAlertMessages("Error eal momento de enviar los archivos al servidor: " + e.message); 
		showUpdateJREMessage();
		return;
	}
	
	if(dataResponse[0]==definitiveOption)
	{
		if(dataResponse[1] == file.getUploadedPartitionCount())
		{
			isUploadDefinitiveFile = true;
			indexFileUploaded = indexFileUploaded+1;
		}
	}
	
	if(hasNote && dataResponse[0]==noteOption)
	{
		if(dataResponse[1] == file.getUploadedPartitionCount())
		{
			isUploadNoteFile = true;
		}
	}
	
	if(isUploadDefinitiveFile && !hasNote)
	{
		if(indexFileUploaded >= indexFileAdd)
		{
			showSendButton(true);
		}
		else
		{
			showSendButton(false);
		}
		
	}
	else if (isUploadDefinitiveFile && (hasNote && isUploadNoteFile))
	{
		showSendButton(true);
	}
	else
	{
		showSendButton(false);
	}
}

/**
 * file removed notification
 */
function uploaderFileRemoved( uploader, file ) 
{
	indexFileAdd = indexFileAdd - 1;
	isUploadDefinitiveFile=false;
	isUploadNoteFile = false;
	showSendButton(false);	
}

function uploaderFileAdded( uploader, file ) 
{
	indexFileAdd = indexFileAdd + 1;
}



//---------------------------------------------------------------//
// Private helper methods
//---------------------------------------------------------------//


/**
 * Shows the applet place div tag.
 * @param content applet generated content
 */
function showAppletPlaceTag(content,parentObject)
{
	try
	{
		var appletPlaceDivTag = document.getElementById(parentObject);
		
		if (appletPlaceDivTag != null)
		{
			appletPlaceDivTag.innerHTML = content;
		}
		else
		{
			appletPlaceDivTag = document.createElement('div');
			appletPlaceDivTag.innerHTML = content;
			appletPlaceDivTag.setAttribute('id', parentObject);
			document.body.appendChild(appletPlaceDivTag);
		}
	}
	catch (e)
	{
		showAlertMessages("Error al momento de desplegar el tag HTML contenedor del applet de firma digital: " + e.message);
	}
}

function showNote(showFlag)
{
	if(showFlag)
	{
		var appletPlaceDivTag = document.getElementById('notePanel');
		appletPlaceDivTag.parentNode.parentNode.style.display = 'block';
		createUploaderApplet('../uploaderComponent/', true,'note',2);
		hasNote = true;
	}
	else
	{
		var appletPlaceDivTag = document.getElementById('notePanel');
		appletPlaceDivTag.parentNode.parentNode.style.display = 'none';
		hasNote = false;
	}
}

function showbusinessCode(showFlag)
{
	if(showFlag)
	{
		var businessCodeDiv = document.getElementById('businessCodeSection');
		businessCodeDiv.style.display = 'block';
		businessCodeDiv.style.visibility = 'visible';
	}
	else
	{
		var businessCodeDiv = document.getElementById('businessCodeSection');
		businessCodeDiv.style.display = 'none';
		businessCodeDiv.style.visibility = 'hidden';
	}
}

function showSendButton(showFlag)
{
	if(showFlag)
	{
		var buttonDivTag = document.getElementById('sendForm');
		buttonDivTag.style.display = 'block';
		buttonDivTag.style.visibility = 'visible';
		
		var buttonDivTag2 = document.getElementById('sendFormPopUp');
		buttonDivTag2.style.display = 'block';
		buttonDivTag2.style.visibility = 'visible';
		
		var buttonDivTag3 = document.getElementById('sendFormIframe');
		buttonDivTag3.style.display = 'block';
		buttonDivTag3.style.visibility = 'visible';
		
	}
	else
	{
		var buttonDivTag = document.getElementById('sendForm');
		buttonDivTag.style.display = 'none';
		buttonDivTag.style.visibility = 'hidden';
		
		var buttonDivTag2 = document.getElementById('sendFormPopUp');
		buttonDivTag2.style.display = 'none';
		buttonDivTag2.style.visibility = 'hidden';
		
		var buttonDivTag3 = document.getElementById('sendFormIframe');
		buttonDivTag3.style.display = 'none';
		buttonDivTag3.style.visibility = 'hidden';
		
	}
}

/**
 * Checks if Java is available in the client browsers.
 * @returns True if JRE is available in the client. False otherwise
 */
function checkJavaPlugin()
{
	try
	{
		if (deployJava.versionCheck('1.8+'))
		{
			return true;
		}
		else
		{
			showUpdateJREMessage();
			return false;
		}
	}
	catch (e)
	{
		showAlertMessages("Error al momento de verificar la mAquina virtual de Java instalada: " + e.message);
		return true;
	}
	
}

/**
 * Shows the JRE update messages and options
 */
function showUpdateJREMessage()
{
	var jreMessage = "El sistema no encontrO la Ultima versiOn de la mAquina virtual de Java (JRE) instalada en su equipo o habilitada en su navegador.\n\n";
	jreMessage += "Por favor descargue e instale la mAquina virtual de la siguiente direcciOn: \n";
	jreMessage += "http://www.java.com.\n\n";
	jreMessage += "Adicionalmente tambiEn podrA verificar la mAquina virtual disponible en su navegador en la siguiente direcciOn: \n";
	jreMessage += "http://java.com/es/download/testjava.jsp";
	
	showAlertMessages(jreMessage);
	
	if (makeNoise)
	{
		userInput = confirm( "Desea actualizar su mAquina virtual de Java ahora mismo?");       
		if (userInput)
		{    
			//
			// Set deployJava.returnPage to make sure user comes back to
			// your web site after installing the JRE
			deployJava.returnPage = location.href;
			
			//
			// Install latest JRE or redirect user to another page to get JRE
			deployJava.installLatestJRE();
		}
	}
}

function showNotBlockPanel(isBlock)
{
	var buttonDivTag = document.getElementById('notBlockPanel');
	if(isBlock)
	{
		buttonDivTag.style.display = 'none';
		createUploaderApplet('../uploaderComponent/', true,'definitive',20);
	}
	else
	{
		buttonDivTag.style.display = 'block';
		createUploaderApplet('../uploaderComponent/', true,'definitive',1);	
	}
}