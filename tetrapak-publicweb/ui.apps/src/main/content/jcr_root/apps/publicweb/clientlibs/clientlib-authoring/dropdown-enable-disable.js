(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideHandler($(".cq-dialog-dropdown-disable-enable", e.target));
    });

    $(document).on("selected", ".cq-dialog-dropdown-disable-enable", function (e) {
        showHideHandler($(this));
    });

    function showHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-select")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("select");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("cqDialogDropdownDisableEnableTarget");

        var $target = $(target);

        if (target) {
            var value;
            if (component.value) {
                value = component.value;
            } else {
                value = component.getValue();
            }
            if(value == "hero" || value == "hero1") {                   
             	$(target +' .coral3-Select-label').text("Bright Blue");
                $('.coral3-SelectList-item').each(function() {
    				if ($(this).val() === "gray" ) {
       					 $(this).hide();
    				}
				});
            } else {
              	$(target +' .coral3-Select-label').text("Grey");
				 $('.coral3-SelectList-item').each(function() {
    				if ($(this).val() === "gray" ) {
       					 $(this).show();
    				}
				});              
            }
        }
    }

})(document, Granite.$);