(function ($document) {

   $document.on("click", ".cq-dialog-submit", validateColumns);

   function validateColumns(event) {
      var $form = $(this).closest("form.foundation-form");
      var columns = $(".megamenucolumns").val();
      var contentPath = $form.attr("action");
       $.ajax({
          method: "POST",
          url: contentPath+".updatemegamenucolumns.json",
          data: { columns: columns, contentPath: contentPath }
        }).done(function( msg ) {

          });
   }
}($(document)));
