(function(window, $) {
  var SELECT_REQUIRED_VALIDATOR = "pw.semiauto",
    foundationReg = $(window).adaptTo("foundation-registry");
  foundationReg.register("foundation.validation.validator", {
    selector: 'input[data-foundation-validation="pw.semiauto"]',
    validate: function(el) {
      var value = $(el).val();

      if (!value || !value.match("^/content")) {
        return "Please enter valid existing relative page path only.";
      }else{
		var status = (function(){ return $.ajax({
        type: 'GET',
        url: value + '.json',
        async: false}).status;
      })();
      if (status !== 200) {
			 return "Please enter valid existing relative page path only.";
      }
      }

    }
  });
})(window, jQuery);