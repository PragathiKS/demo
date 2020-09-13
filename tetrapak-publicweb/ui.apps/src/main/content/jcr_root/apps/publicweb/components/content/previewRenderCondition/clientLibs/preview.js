(function(document, $) {

  var fui = $(window).adaptTo("foundation-ui"),
       options = [{
                        id: "ok",
                        text: "OK",
                        primary: true
                    }
                ],
       resubmitOptions = [{
                        id: "ok",
                        text: "OK",
                        primary: true,
                  handler: function() {
                    location.reload();
                }
                    }
                ];
  var path = $('input[name="payload"]').val();



$(document).on("click", ".pw-preview-generator", function(e){
    e.preventDefault();
    alert(path);
    $(".pw-preview-generator").hide();

    $.ajax({
        type: 'POST',
        url: '/bin/publicweb/preview',
        data: {"path":path},
        success: function(data) { 
          if(data){
              if(JSON.parse(data).status == 'success')
                {
                  fui.prompt("Sucess", "Page preview link is <a href='" + JSON.parse(data).saultPagePath + "'>"+ JSON.parse(data).saultPagePath + "</a>", "default", resubmitOptions);
                }
               else {
                  fui.prompt("Error", "Request for preview URL generation failed.", "default", options);
                  $(".pw-preview-generator").show();
                }
             }
             else{
              fui.prompt("Error", "Request for preview URL generation failed.", "default", options);
              $(".pw-preview-generator").show();
             }
     
        },
        fail: function() {
          fui.prompt("Error", "Request for preview URL generation failed.", "default", options);
          $(".pw-preview-generator").show();
        }

  });

  });



})(document, Granite.$);