var waitDialogShown = false;
var useTimerBeforeShowWaitDialog = true;
var waitDialogTimeout = 50;
var waitDialogTimer;
var modalPanelId = "ajaxLoadingModalBox";

function showWaitDialog() 
{
	if (!waitDialogShown) 
	{
		RichFaces.$(modalPanelId).show();
		waitDialogShown = true;
	}
}

function onRequestStart() 
{
	if (useTimerBeforeShowWaitDialog) 
	{
		waitDialogTimer = setTimeout("showWaitDialog();", waitDialogTimeout);
	} 
	else 
	{
		showWaitDialog();
	}
}
function onRequestEnd() 
{
	if (waitDialogShown) 
	{
		RichFaces.$(modalPanelId).hide();
		//RichFaces.hideModalPanel(modalPanelId);
		waitDialogShown = false;
	} 
	else if (useTimerBeforeShowWaitDialog && waitDialogTimer) 
	{
		clearTimeout(waitDialogTimer);
	}
}
