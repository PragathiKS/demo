(function(window, $) {
  var SELECT_REQUIRED_VALIDATOR = "pw.businessForm.tags.validator",
    foundationReg = $(window).adaptTo("foundation-registry");
  foundationReg.register("foundation.validation.validator", {
    selector: 'input[data-foundation-validation="pw.businessForm.tags.validator"]',
    validate: function(el) {
      var value = $(el).val();

      if (!value || !value.match("^/content/cq:tags/pardot-system-config")) {
        return "Please enter valid tag path only.";
      }else{
		var status = (function(){ return $.ajax({
        type: 'GET',
        url: value + '.json',
        async: false}).status;
      })();
      if (status !== 200) {
			 return "Please enter valid tag path only.";
      }
      }

    }
  });
})(window, jQuery);