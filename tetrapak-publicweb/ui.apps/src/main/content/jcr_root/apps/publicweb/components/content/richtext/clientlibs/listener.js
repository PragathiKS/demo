/*
 * Experiencing Adobe Experience Manager blog
 *
 * Licensed under a Creative Commons Attribution 4.0 International License.
 * You may obtain a copy of license at
 *
 *     https://creativecommons.org/licenses/by/4.0/
 *
 * Code snippet at: http://experience-aem.blogspot.com/2018/01/aem-63-sp1-touch-ui-extend-rich-text-link-dialog-remove-parent-frame-option-from-target-select.html
 * Changes in the below code:
 * Removed the Target Frame as well as Top frame option from Link#modifylink dropdown menu.
 *
 */
(function ($) {
    "use strict";

    var _ = window._,
        Class = window.Class,
        CUI = window.CUI,
        RTE_LINK_DIALOG = "rtelinkdialog";

    if(CUI.rte.ui.cui.CuiDialogHelper.eaemExtended){
        return;
    }

    var EAEMLinkBaseDialog = new Class({
        extend: CUI.rte.ui.cui.CQLinkBaseDialog,
        toString: "EAEMLinkBaseDialog",
        initialize: function(config) {
            this.superClass.initialize.call(this, config);
            this.$rteDialog = this.$container.find("[data-rte-dialog=link]");

            var $target = this.$rteDialog.find("coral-select");

            //removing frame options from link dropdown
            $target[0].items.remove($target[0]);
        }
    });

    CUI.rte.ui.cui.CuiDialogHelper = new Class({
        extend: CUI.rte.ui.cui.CuiDialogHelper,
        toString: "EAEMCuiDialogHelper",
        instantiateDialog: function(dialogConfig) {
            var type = dialogConfig.type;

            if(type !== RTE_LINK_DIALOG){
                this.superClass.instantiateDialog.call(this, dialogConfig);
                return;
            }

            var $editable = $(this.editorKernel.getEditContext().root),
                $container = CUI.rte.UIUtils.getUIContainer($editable),
                dialog = new EAEMLinkBaseDialog();

            dialog.attach(dialogConfig, $container, this.editorKernel);
            return dialog;
        }
    });

    CUI.rte.ui.cui.CuiDialogHelper.eaemExtended = true;
})(jQuery);