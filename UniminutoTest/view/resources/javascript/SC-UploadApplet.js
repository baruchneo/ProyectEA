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
 * uploader note tag id name
 */
var noteDiv = "notesPanel";

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

/**
 *  send include files default false
 */
var hasFiles = false;

/**
 *  send include notes default false
 */
var hasNotes = false;

/**
 * contains box uploader
 */
var uploaderBoxFiles;

/**
 * contains box uploader
 */
var uploaderBoxNotes;

/**
 * Contains type  notes, validation and definitive or ""
 */
var typeGlobal;

/*
 * Constrant for fine uploader files
 */
var fileUploaded = false;

/*
 * Constrant for fine uploader notes
 */
var noteUploaded = false;

//---------------------------------------------------------------//
//Java script public helper methods
//---------------------------------------------------------------//

function showNote(showFlag)
{
	var appletPlaceDivTag = document.getElementById(noteDiv);
	if(showFlag)
	{
		appletPlaceDivTag.parentNode.parentNode.style.display = 'block';
		loadUploader('NOTAS', false, noteDiv);
		hasNote = true;
	}
	else
	{
		appletPlaceDivTag.parentNode.parentNode.style.display = 'none';
		hasNote = false;
	}
}

function showbusinessCode(showFlag)
{
	var businessCodeDiv = document.getElementById('businessCodeSection');
	if(showFlag)
	{
		businessCodeDiv.style.display = 'block';
		businessCodeDiv.style.visibility = 'visible';
	}
	else
	{
		businessCodeDiv.style.display = 'none';
		businessCodeDiv.style.visibility = 'hidden';
	}
}

function showSendButton(showFlag)
{
	var buttonDivTag = document.getElementById('sendForm');
	var buttonDivTag2 = document.getElementById('sendFormPopUp');
	var buttonDivTag3 = document.getElementById('sendFormIframe');

	if(showFlag)
	{
		buttonDivTag.style.display = 'block';
		buttonDivTag.style.visibility = 'visible';

		buttonDivTag2.style.display = 'block';
		buttonDivTag2.style.visibility = 'visible';

		buttonDivTag3.style.display = 'block';
		buttonDivTag3.style.visibility = 'visible';

	}
	else
	{
		buttonDivTag.style.display = 'none';
		buttonDivTag.style.visibility = 'hidden';

		buttonDivTag2.style.display = 'none';
		buttonDivTag2.style.visibility = 'hidden';

		buttonDivTag3.style.display = 'none';
		buttonDivTag3.style.visibility = 'hidden';

	}
}

function showNotBlockPanel(isBlock, type)
{
	var buttonDivTag = document.getElementById('notBlockPanel');
	if(isBlock)
	{
		buttonDivTag.style.display = 'none';
	}
	else
	{
		buttonDivTag.style.display = 'block';
	}
	loadUploader(type, isBlock, definitiveDiv);
}


//---------------------------------------------------------------//
// Private helper methods
//---------------------------------------------------------------//


/**
 * Load uploads components in view
 * @param type send definitive for definitive files, send validation for validation files, send notes for notes files.
 * In first execution this value is empty
 * @param isBlock true files send for superbancaria files, false for other process
 * @param name element when uploader is created
 */
function loadUploader(type, isBlock, name) {
	var limits = 1;
	typeGlobal = type;

	if (isBlock) {
		limits = 20;
	}

	//La primer vez que se carga el componente que incluye opcion de validacion type es null
	if (typeGlobal != null && (typeGlobal == "ENVIO_DEFINITIVO" || typeGlobal == "ENVIO_VALIDACION")) {
		uploaderBoxFiles = null;
		hasFiles = true;
		hasNotes= false;
	}
	else if (typeGlobal != null && typeGlobal == "NOTAS") {
		limits = 2;
		hasNotes= true;
		if(uploaderBoxNotes == null) {
			uploaderBoxNotes = new qq.FineUploader(propertiesUploader(noteOption, name, limits, 'qq-template-notes'));
			qq(document.getElementById("trigger-upload-notes")).attach("click", function () {
				noteUploaded = true;
				uploaderBoxNotes.uploadStoredFiles();
			});
		}
	}

	if(uploaderBoxFiles == null) {
		uploaderBoxFiles = new qq.FineUploader(propertiesUploader(definitiveOption, name, limits, 'qq-template-files'));
		qq(document.getElementById("trigger-upload-files")).attach("click", function () {
			fileUploaded = true;
			uploaderBoxFiles.uploadStoredFiles();
		});
	}
}

/**
 * Load properties for fine uploader ni JSON FORMAT
 * @param typeSending if definitive for files attached, note for notes files attached otherwise loaded file attached only
 * @param name element when uploader is created
 * @param limits limit file add per uploader
 * @param template id for specific fine uploader
 * @returns {{debug: boolean, element: Element, autoUpload: boolean, request: {endpoint: string}, template: *, deleteFile: {enabled: boolean, endpoint: string}, chunking: {enabled: boolean}, callbacks: {onAllComplete: completedLoaded}, validation: {itemLimit: *}}}
 */
function propertiesUploader(typeSending, name, limits, template)
{
	return {
		debug: true,
		element: document.getElementById(name),
		autoUpload: false,
		request: {
			endpoint: '../user/uploadServlet.do?pOptionFile=' + typeSending
		},
		template: template,
		deleteFile: {
			enabled: true,
			endpoint: '/uploads'
		},
		chunking: {
			enabled: true,
			partSize: 50000
		},
		callbacks: {
			"onAllComplete": completedLoaded
		},
		validation: {
			itemLimit: limits
		},
		messages: {
			emptyError : '{file} estA vacIo, por favor elija otro(s) archivo(s).',
			minSizeError: '{file} is too small, minimum file size is {minSizeLimit}.',
			noFilesError: 'No hay archivos para subir.',
			tooManyItemsError: 'Demasiados archivos para subir ({netItems}). El lImite de archivos es {itemLimit}.',
			typeError: '{file} has an invalid extension. Valid extension(s): {extensions}.',
		}
	};
}

//noinspection JSUnusedLocalSymbols
/**
 * When files are loaded in server
 * @param arrSucceeded array for success sends
 * @param arrFailed array for errors sends
 */
function completedLoaded(arrSucceeded, arrFailed) {

	/*Valida y modifica la transmisión ya sea para notas y para el evento final.*/
	if(hasFiles && hasNotes)
	{
		if(fileUploaded && noteUploaded)
		{
			clearUploaders();
			showSendButton(true);
		}
		else
		{
			showSendButton(false);
		}
	}
	else if(hasFiles && !hasNotes)
	{
		if(fileUploaded)
		{
			clearUploaders();
			showSendButton(true);
		}
		else
		{
			showSendButton(false);
		}
	}
	else
	{
		if(typeGlobal == "" && fileUploaded)
		{
			clearUploaders();
			showSendButton(true);
		}
		else
		{
			showSendButton(false)
		}

	}
}

/**
 * Clear uploads an options
 */
function clearUploaders()
{
	hasFiles = false;
	hasNotes = false;
	uploaderBoxNotes = null;
	uploaderBoxFiles = null;
}