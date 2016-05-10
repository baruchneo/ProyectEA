function showPassword(show) {
    var elementAresShow = $('[id$=scpAresPasswordShow]');
    var elementAresHide = $('[id$=scpAresPasswordHide]');

    if (show) {
        elementAresHide.hide();
        elementAresShow.show();
        elementAresShow.val(elementAresHide.val());
        elementAresShow.attr('readonly', 'readonly');
    }
    else {
        elementAresHide.show();
        elementAresShow.hide();
    }
}

function showPasswordSCP(showSCP) {
    var elementScpShow = $('[id$=scpPasswordShow]');
    var elementScpHide = $('[id$=scpPasswordHide]');

    if (showSCP) {
        elementScpHide.hide();
        elementScpShow.show();
        elementScpShow.val(elementScpHide.val());
        elementScpShow.attr('readonly', 'readonly');
    }
    else {
        elementScpHide.show();
        elementScpShow.hide();
    }
}

