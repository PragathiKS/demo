(function ($, $document) {
    "use strict";

    $document.on("dialog-ready", function() {
      setTimeout(function(){
        var $tabLayouts = $('.pw-tablist-multi');
        if($tabLayouts){
           $tabLayouts.each(function (i, tabLayout) {
               var $tabLayoutCurrVal = $(tabLayout).val();
               var $listContainers = $(tabLayout).parent().parent().find(".tabLayout-showhide-target");
               if($listContainers){
                   $listContainers.each(function (i, listContainer) {
                   	var $showHideValue = $(listContainer).data("showhidetargetvalue");
                   	if($showHideValue ==  $tabLayoutCurrVal){
                        $(listContainer).removeClass("hide");
                    }
                   });
               }
          });

        }
        var $vedioTypes = $('.pw-vedio-showhide');
        if($vedioTypes){
           $vedioTypes.each(function (i, vedioType) {
               var $vedioTypeCurrVal = $(vedioType).val();
               var $vedioContainers = $(vedioType).parent().parent().find(".videoType-showhide-target");
               if($vedioContainers){
                   $vedioContainers.each(function (i, vedioContainer) {
                   	var $showHideValue = $(vedioContainer).data("showhidetargetvalue");
                   	if($showHideValue ==  $vedioTypeCurrVal){
                        $(vedioContainer).removeClass("hide");
                    }
                   });
               }
          });

        }
       }, 500);
    });

    $(document).on('foundation-contentloaded', function() {
        var $tabLayouts = $('.pw-tablist-multi');
        var $tabManual = $(".pw-tablist-manual-multi")
        if($tabManual){
            var $tabManualAddButton =  $tabManual.children('.coral3-Button');
           $tabManualAddButton.bind( "click", function(e) {
            setTimeout(function(){
         		$tabManual.children(".coral3-Multifield-item").last().find(".pw-tablist-multi").change();
                $tabManual.children(".coral3-Multifield-item").last().find(".pw-vedio-showhide").change();
            }, 500);
        });
        }

        if($tabLayouts){
             $tabLayouts.on('change', function(event){
               var $listContainers = $(this).parent().parent().find(".tabLayout-showhide-target");
               if($listContainers){
                   $listContainers.each(function (i, listContainer) {
                   	var $showHideValue = $(listContainer).data("showhidetargetvalue");
                   	if($showHideValue ==  event.target.value){
                        $(listContainer).removeClass("hide");
                    }
                     else{
                         $(listContainer).addClass("hide");
                      }
                   });
               }
             });
        }

		 var contentTypes = $('.tablist-contenttype');

         if(contentTypes){
             contentTypes.on('change', function(event){
                 var contentTypesCurrVal = $(contentTypes).val();
                   var tabsData = $(this).parent().parent().find(".contentType-showhide-target");
                    tabsData.each(function (i, tabData) {
                        if($(tabData).data("showhidetargetvalue") != contentTypesCurrVal){
                            $(tabData).find(".coral3-Multifield-item").remove();
                        }
                        $(".pw-tablist-manual-multi").children('.coral3-Button').removeAttr("disabled");
                        $(".pw-tablist-semi-multi").children('.coral3-Button').removeAttr("disabled");
                    });
             });
        }

        var $vedioTypes = $('.pw-vedio-showhide');
        if($vedioTypes){
             $vedioTypes.on('change', function(event){
               var $vedioContainers = $(this).parent().parent().find(".videoType-showhide-target");
               if($vedioContainers){
                   $vedioContainers.each(function (i, vedioContainer) {
                   	var $showHideValue = $(vedioContainer).data("showhidetargetvalue");
                   	if($showHideValue ==  event.target.value){
                        $(vedioContainer).removeClass("hide");
                    }
                     else{
                         $(vedioContainer).addClass("hide");
                      }
                   });
               }
             });
        }

    });

})($, $(document));