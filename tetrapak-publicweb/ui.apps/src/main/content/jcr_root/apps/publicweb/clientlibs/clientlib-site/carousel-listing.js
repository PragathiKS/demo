$(document).on('ready', function () {

    $(function(){
        $('#categories').on('change', function(){
            var val = $(this).val();
            getSubCategories(val);
        });
        $('#categories').trigger('change');        
    });

    $('#subcategories').on('change', function(){
        var val = $(this).val();        
        getCarouselListing(val);
    });

    function getSubCategories(val) {
        $.ajax({            
            url: '/bin/tetrapak/pw-subcategorytag',
            type: 'get',
            dataType: 'text',
            data: {
                categoryTag: val
            },
            success: function(data) {
                var subCategory = $("#subcategories");
                subCategory.empty();
                $.each(JSON.parse(data), function(key, value){
                    subCategory.append("<option value=\"" + value + "\">" + key + "</option>");
                }); 

                var subCategoryVal = $('#subcategories').val();
                getCarouselListing(subCategoryVal);
            }
        });
    };

    function getCarouselListing(val) {
        var productType = $("#producttype-data-prodtype").attr("data");
        var rootPath = $("#practice-data-rootpath").attr("data");
        $.ajax({            
            url: '/bin/tetrapak/pw-carousellisting',
            type: 'get',
            async: false,
            data: {
                productType: productType,
                subCategoryVal: val,
                rootPath: rootPath
            },
            success: function(data) {                
                        $('.pw-carousel__tabPane').html("<h3>Practice Line</h3>");
                        $.each(data, function(i, obj) {
                            $('.pw-carousel__tabPane').append("<p>Title : " + obj.practiceTitle + "</p><p>Description : " + obj.vanityDescription + "</p><p>Practice Image : " + obj.practiceImagePath + "</p><p>Image Alt Text : " + obj.practiceImageAltI18n + "</p><p>Link Text : " + obj.ctaTexti18nKey + "</p><p>Practice page Path : " + obj.practicePath + "</p><br/>");
                    });

            }
        });
    };
    event.preventDefault();
});
