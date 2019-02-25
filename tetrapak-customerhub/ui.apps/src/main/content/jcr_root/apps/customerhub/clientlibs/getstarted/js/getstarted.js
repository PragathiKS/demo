 $(document).on("dialog-ready", function () {
    var maxMultifieldAllowed = $("coral-multifield").attr("data-validation");
     if ((typeof maxMultifieldAllowed  == "undefined") || (maxMultifieldAllowed.indexOf("multifield-max") == -1)){
         return;
     } 
    if(parseInt($('.coral3-Multifield-item').size()) == parseInt(maxMultifieldAllowed.replace("multifield-max-", ""))){
	$('button[coral-multifield-add]').hide();
    	}
    });
$(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
  selector: "[data-foundation-validation^='multifield-max']",
  validate: function(el) {
    var validationName = el.getAttribute("data-validation");
	if (typeof validationName  == "undefined"){
         return;
     } 
    if (parseInt(el.items.length) == parseInt(validationName.replace("multifield-max-", ""))) {
        $('button[coral-multifield-add]').hide();
     }else{
         $('button[coral-multifield-add]').show();
     }
  }
});
