(function ($document, gAuthor) {
    $document.on("foundation-contentloaded", showSoftConversionForm);
 
    function showSoftConversionForm(){
        var dialog = gAuthor.DialogFrame.currentDialog,
            componentPath = dialog.editable;

        if(!componentPath){
            return;
        }

        var response = (function(){ return $.ajax({
        type: 'GET',
        url: componentPath.path + '.json',
        async: false});
      })();
      var responseData = response.responseJSON;
      if(responseData.enableSoftcoversion == 'true'){
          $('coral-select[name="./formType"]').val("enableSoftconversion").change();
          $("div").find("[data-showhidetargetvalue='enableSoftconversion']").removeClass('hide');
	  }
    }
}($(document), Granite.author));