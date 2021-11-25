(function ($document, gAuthor) {
    
	$document.on("click", ".cq-dialog-submit", validatePardotURL);
 
 	const listOfResourceTypes = [
        "publicweb/components/content/businessinquiryform"
    ];
    
	
	function validatePardotURL(event){
        var $form = $(this).closest("form.foundation-form");
        if(isTargetDialog($form,listOfResourceTypes)){
            event.stopPropagation();
            event.preventDefault();
            
            pardotUrl = $form.find('input[name="./befPardotURL"]').val(),
            patterns = {
                pardotUrlRegex: /^(https:\/\/)?go+([\-\.]{1}tetrapak+)*\.[com]{2,5}(:[0-9]{1,5})?(\/.*)?$/
            };

            if(!patterns.pardotUrlRegex.test(pardotUrl)) {
                gAuthor.ui.helpers.prompt({
                    title: "Invalid Pardot URL - Business Inquiry Form",
                    message: "Pardot URL for business inquiry form cannot be left blank and should start with <b>https://go.tetrapak.com</b>",
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
	}
	
	function isTargetDialog($formElement, dlgResourceType) {
    	const resourceType = $formElement.find("input[name='./sling:resourceType']").val();
        return dlgResourceType.indexOf(resourceType) > -1 ? true : false;
	}
	
}($(document), Granite.author));