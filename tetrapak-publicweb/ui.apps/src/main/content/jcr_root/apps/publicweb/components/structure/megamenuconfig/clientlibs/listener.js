// This implementation is with reference to
// http://experience-aem.blogspot.com/2018/01/aem-6310-touch-ui-add-min-max-limit-to-coral-3-multifield.html
// It is licensed under a Creative Commons Attribution 4.0 International License | https://creativecommons.org/licenses/by/4.0/
// Customized this script to make it work for nested multifield and disabling add button for max limits.
(function($, $document, gAuthor) {
    var PW_MAX_ITEMS = "pw-max-items",
        PW_MIN_ITEMS = "pw-min-items",
        DATA_MF_NAME = "data-granite-coral-multifield-name",
        RS_MULTIFIELD = "granite/ui/components/coral/foundation/form/multifield",
        SLING_RES_TYPE = "sling:resourceType";

    // Adding this method to validate dynamically generated dialog sections
    function onClickValidate() {
        $("coral-multifield button").on("click", function(e) {
            getMultifieldDialog();
            onDeleteClick();
        });
    }

    // On click of delete button, enable the add button, if the limit is less than max limit
    function onDeleteClick() {
        $("coral-multifield-item button").on("click", function(e) {
            var maxItems = $(this).parents("coral-multifield").attr("max-items");
            if ($(this).closest("coral-multifield").children("coral-multifield-item").length <= maxItems) {
                $(this).parents("coral-multifield").children("button[coral-multifield-add]").removeAttr('disabled');
            }
        });
    }

    function multifieldHandler(data) {
        var mfProperties = {};
        fillItemsOfMultifield(data, mfProperties);

        _.each(mfProperties, function(mfProps, mfName) {
            $("[" + DATA_MF_NAME + "='" + mfName + "']").attr("max-items", mfProps[PW_MAX_ITEMS]);
            var multifield = $("[" + DATA_MF_NAME + "$='" + mfName + "']");
            _.each(multifield, function(field, index) {
                validateMultifieldLimit(field, mfProps[PW_MAX_ITEMS], mfProps[PW_MIN_ITEMS]);
            });
        });
    }

    function getMultifieldDialog() {
        $.ajax(getDialogPath() + ".infinity.json").done(multifieldHandler);
    }

    $document.on("dialog-ready", getMultifieldDialog);
    $document.on("dialog-ready", onClickValidate);

    function validateMultifieldLimit($multifield, maxItems, minItems) {
        if (maxItems) {
            maxItems = parseInt(maxItems);
        }

        if (minItems) {
            minItems = parseInt(minItems);
        }

        $.validator.register({
            selector : "#" + $multifield.id,
            validate : validate
        });

        function disableAddButton(mfId) {
            $('#' + mfId).children('button[coral-multifield-add]').attr('disabled', 'true');
        }

        // Adding this method so that add button is disabled on dialog ready if the limit is reached
        function disableAddOnMaxLimit(count, maxItems, mfId) {
            if (maxItems && (count >= maxItems)) {
                disableAddButton(mfId);
            }
        }

        disableAddOnMaxLimit($multifield._items.length, maxItems, $multifield.id);

        function validate() {
            var count = $multifield._items.length;

            if (maxItems && (count == maxItems)) {
                disableAddButton($multifield.id);
            } else if (minItems && (count < minItems)) {
                return "Minimum required : " + minItems + " items";
            } else {
                $('#' + $multifield.id).children('button[coral-multifield-add]').removeAttr('disabled');
                return null;
            }
            return null;
        }
    }

    function getDialogPath() {
        var gAuthor = Granite.author, currentDialog = gAuthor.DialogFrame.currentDialog, dialogPath;

        if (currentDialog instanceof gAuthor.actions.PagePropertiesDialog) {
            var dialogSrc = currentDialog.getConfig().src;
            dialogPath = dialogSrc.substring(0, dialogSrc.indexOf(".html"));
        } else {
            var editable = gAuthor.DialogFrame.currentDialog.editable;

            if (!editable) {
                return;
            }
            dialogPath = editable.config.dialog;
        }
        return dialogPath;
    }

    function fillItemsOfMultifield(data, mfProperties) {
        if (!_.isObject(data) || _.isEmpty(data)) {
            return mfProperties;
        }

        _.each(data, function(value, key) {
            if (_.isObject(value) && !_.isEmpty(value)) {
                mfProperties = fillItemsOfMultifield(value, mfProperties);
            } else {
                if ((key == SLING_RES_TYPE) && (value == RS_MULTIFIELD)) {
                    mfProperties[data.field.name] = data;
                }
            }
        });
        return mfProperties;
    }
}(jQuery, jQuery(document), Granite.author));