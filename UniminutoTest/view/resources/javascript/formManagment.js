function hideDiv(classStyle, divID)
{
    if(divID.length > 0)
    {
        for (idSpecific in divID) {
            var idValue = $('[id$=' + divID[idSpecific] + ']');
            if (idValue != null) {
                idValue.val('');
            }
        }
    }
    $(classStyle).hide();
}

function showDiv(classStyle)
{
    $(classStyle).show();    
}

function showHideData(state, classStyle, divID, caseValue)
{
    switch (caseValue)
    {
        case 'notes':
            var valueSign = $('[id$=digitalSignatureRequired]');
            if(state === 'true' && valueSign.val() === 'true')
            {
                showDiv(classStyle);
            }
            else if(state === 'true' && valueSign.val() !== 'true')
            {
                showDiv('.notes');
            }
            else
            {
                hideDiv(classStyle, divID);
            }
            break;
        case 'remote':
            var valueCopyRemote = $('[id$=remoteCopy]');
            var valueResponseRemote = $('[id$=remoteResponseRead]');
            if(valueResponseRemote.val() === 'true' || valueCopyRemote.val() === 'true')
            {
                showDiv(classStyle);
            }
            else
            {
                hideDiv(classStyle, divID);
            }
            break;
        default :
            if(state === 'true')
            {
                showDiv(classStyle);
            }
            else
            {
                hideDiv(classStyle, divID);
            }
    }
}