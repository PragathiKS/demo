(function ($) {
    var EAEM_MAX_TAGS_VALIDATOR = "eaem.max.tags",
        EAEM_MAX_TAGS = "eaemMaxTags",
        foundationReg = $(window).adaptTo("foundation-registry");
    var ui = $(window).adaptTo("foundation-ui");

    foundationReg.register("foundation.validation.validator", {
        selector: "[data-validation='" + EAEM_MAX_TAGS_VALIDATOR + "'] input",
        validate: function(el) {
            var $tagsPicker = $(el).closest(".js-cq-TagsPickerField"),
                maxTagsAllowed = $tagsPicker.data(EAEM_MAX_TAGS.toLowerCase());

            if(!maxTagsAllowed){
                console.log(EAEM_MAX_TAGS + " number not set");
                return;
            }

            var $tagList = $tagsPicker.next(".coral-TagList");

            if ($tagList.find(".coral-TagList-tag").length > maxTagsAllowed){
                ui.alert("Warning", "Maximum " + maxTagsAllowed + " tag allowed!", "notice");
                return true;
            }
        }
    });
}(jQuery));