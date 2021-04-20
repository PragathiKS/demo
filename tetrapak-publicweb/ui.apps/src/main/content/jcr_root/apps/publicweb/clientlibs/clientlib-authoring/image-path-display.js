(function ($, $document) {
    "use strict";
    $document.on("dialog-ready", function() {
        if ($('coral-fileupload').hasClass('coral3-FileUpload')) {
            $('.cq-FileUpload-thumbnail-dropHere').each(function(index) {
                if($('.cq-FileUpload-thumbnail:eq('+index+') button[data-cq-fileupload-filereference]').length >0 ){
                    $( "<div class ='current-Image-Path'>"+$('.cq-FileUpload-thumbnail:eq('+index+') button').data('cq-fileupload-filereference')+"</div>" ).insertAfter( $('.cq-FileUpload-thumbnail-dropHere').eq(index));
                }
            });
        }
        $(".current-Image-Path").attr('title', 'Above image path');
    });
})($, $(document));
