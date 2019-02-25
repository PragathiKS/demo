 var jqueryObj = $;
 jqueryObj(document).on("dialog-ready", function () {
    var maxMultifieldAllowed = jqueryObj("coral-multifield").attr("data-validation");

     if ((typeof maxMultifieldAllowed  === "undefined") || (maxMultifieldAllowed.indexOf("multifield-max") === -1)){
         return;
     } 
    if(parseInt(jqueryObj('.coral3-Multifield-item').size()) === parseInt(maxMultifieldAllowed.replace("multifield-max-", ""))){
		 jqueryObj('button[coral-multifield-add]').attr('disabled',true);
    	}
    });
jqueryObj(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
  selector: "[data-foundation-validation^='multifield-max']",
  validate: function(el) {
    var validationName = el.getAttribute("data-validation");
	if (typeof validationName  === "undefined"){
         return;
     } 
    if (parseInt(el.items.length) === parseInt(validationName.replace("multifield-max-", ""))) {
    	jqueryObj('button[coral-multifield-add]').attr('disabled',true);
     }else{
    	 jqueryObj('button[coral-multifield-add]').removeAttr("disabled");
     }
  }
});
