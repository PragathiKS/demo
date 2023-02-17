(function($document) {
    var ON_DIALOG_LOAD = "foundation-contentloaded";
    var CHANGE_EVENT = "change";
    var NO_REDIRECT_FIELD = 'coral-checkbox[name="./noRedirectOnSucess"]';
    var REDIRECT_FIELD = 'foundation-autocomplete[name="./redirectUrl"]';
    var REDIRECT_FIELD_WRAPPER = ".coral-Form-fieldwrapper";

    $document.on(ON_DIALOG_LOAD, formContainerShowHide);

    function formContainerShowHide() {
        var noRedirectField = $(NO_REDIRECT_FIELD);
       	noRedirectField.on(CHANGE_EVENT, onChangeNoRedirectFeild);
        toggleRedirectUrlField(noRedirectField[0].checked);
    }

    function onChangeNoRedirectFeild() {
        toggleRedirectUrlField(this.checked);
    }

    function toggleRedirectUrlField(value){
        if(value){
            $(REDIRECT_FIELD).parent(REDIRECT_FIELD_WRAPPER).hide();
        }else{
            $(REDIRECT_FIELD).parent(REDIRECT_FIELD_WRAPPER).show();
        }
    }

}($(document)));
