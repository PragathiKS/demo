(function ($document, gAuthor) {
    $document.on("foundation-contentloaded", bannerDialogChanges);

    function bannerDialogChanges(){
        var dialog = (gAuthor != null) ? gAuthor.DialogFrame.currentDialog : null;
        var componentPath = (dialog != null) ? dialog.editable : null;
		if(!componentPath){
            return;
        }
		var response = (function(){ return $.ajax({
        	type: 'GET',
        	url: componentPath.path + '.json',
        	async: false});
      	})();
		var responseData = response.responseJSON;
		if(responseData.pwCardTheme == undefined && responseData.bannerType == 'hero'){
          $('coral-select[name="./pwCardTheme"]').val('sky-blue').change();          
	  	}

        var componentName = (dialog != null) ? dialog.editable.name : null;
        if(componentName!=null && componentName.indexOf('banner') == 0){
            var bannerTypeDropDown = $('coral-select[name="./bannerType"]');
            var pwCardThemeDropDown = $('coral-select[name="./pwCardTheme"]');
            bannerTypeDropDown.change(bannerTypeOnChange);
        }
    }

    function bannerTypeOnChange(event) {
        var selectedValue = this.selectedItem._value;
        var pwCardThemeDropDown = $('coral-select[name="./pwCardTheme"]');
        if((selectedValue.indexOf('hero') >= 0) || (selectedValue.indexOf('hero-wide') >= 0)){
            pwCardThemeDropDown.val('sky-blue').change();
        }else{
            pwCardThemeDropDown.val('gray').change();
        }
    }

}($(document), Granite.author));