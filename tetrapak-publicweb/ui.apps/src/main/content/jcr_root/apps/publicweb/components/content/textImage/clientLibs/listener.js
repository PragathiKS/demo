(function ($document, gAuthor) {
    $document.on("foundation-contentloaded", showSoftConversionForm);
	$document.on("click", ".cq-dialog-submit", validatePardotURL);
 
    function showSoftConversionForm(){
        var dialog = gAuthor.DialogFrame.currentDialog,
            componentPath = (dialog != null) ? dialog.editable : null;

        if(!componentPath){
            return;
        }

        var response = (function(){ return $.ajax({
        type: 'GET',
        url: componentPath.path + '.json',
        async: false});
      })();
      var responseData = response.responseJSON;
      if((responseData.enableSoftcoversion == 'true') && ($('coral-select[name="./formType"]').val()==="enableSoftconversion")){
          $('coral-select[name="./formType"]').val("enableSoftconversion").change();
          $("div").find("[data-showhidetargetvalue='enableSoftconversion']").removeClass('hide');
	  }
    }
	
	function validatePardotURL(event){
		event.stopPropagation();
        event.preventDefault();
 
        var $form = $(this).closest("form.foundation-form"),
        dropdownValue = $form.find('coral-select[name="./formType"]').val(),
		pardotUrl = $form.find('input[name="./pardotUrl"]').val(),
		pardotUrlSubscription = $form.find('input[name="./pardotUrlSubscription"]').val(),
        patterns = {
             pardotUrlRegex: /^(https:\/\/)?go+([\-\.]{1}tetrapak+)*\.[com]{2,5}(:[0-9]{1,5})?(\/.*)?$/
        };

        if(dropdownValue==="enableSoftconversion" && !patterns.pardotUrlRegex.test(pardotUrl)) {
            gAuthor.ui.helpers.prompt({
                title: "Invalid Pardot URL - Softconversion Form",
                message: "Pardot URL for softconversion form cannot be left blank and should start with <b>https://go.tetrapak.com</b>",
                actions: [{
                    id: "CANCEL",
                    text: "Ok",
                    className: "coral-Button"
                }],
				callback: function (actionId) {
					if (actionId === "CANCEL") {
					}
				}
			});
        }else if(dropdownValue==="enableSubscription" && !patterns.pardotUrlRegex.test(pardotUrlSubscription)){
			gAuthor.ui.helpers.prompt({
				title: "Invalid Pardot URL - Subscription Form",
				message: "Pardot URL for subscription form cannot be left blank and should start with <b>https://go.tetrapak.com</b>",
				actions: [{
					id: "CANCEL",
					text: "Ok",
					className: "coral-Button"
				}],
				callback: function (actionId) {
					if (actionId === "CANCEL") {
					}
				}
			});
		}else{
            $form.submit();
        }
	}
	
}($(document), Granite.author));