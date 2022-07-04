(function($) {
	var registry = $(window).adaptTo("foundation-registry");
	/* Validator for Tag Field - Package Shape */
	registry.register("foundation.validation.validator", {

		selector : "[data-validation=package-shape]",
		validate : function(el) {
			var element = $(el);
			var elementVal = element.val();
			if (elementVal) {
				var value = elementVal.split("/");
				if (value.length != 3) {
					return "Please select a Package Shape for Keylines";
				}
				var packageShapes = document.querySelectorAll('[data-validation=package-shape] input:not([type="hidden"]');
				var packageTypeElement = document.querySelector('[data-package-type=fetch-value]');
				var packageType = "";
				var type = new Set();
				packageShapes.forEach(element =>{
					var elementVal = element.value;
					packageType = elementVal.substring(0, elementVal
							.lastIndexOf("/"));
					type.add(packageType);
				});
				if(type.size>1){
					return "Package Shape should belong to the same Package Type. Please make sure the same Package Type is selected";
				}else{
					packageTypeElement.value = packageType;
				}
			}
		}
	});
}(jQuery))