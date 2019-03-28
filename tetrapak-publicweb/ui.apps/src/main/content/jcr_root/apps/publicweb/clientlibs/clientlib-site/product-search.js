$(document).on('ready', function () {

    var productCategory = "all";
    if (document.getElementById("pw-product-listing")) {
            getProductListing();
        }

    $('.prod-listing-data-cateogry').click(function(){
        alert("Category tab clicked");
        var prodCateg = $(this).attr("data");
        productCategory = $(this).attr("data");
        getProductListing();
    });

    function getProductListing() {
        $.ajax({            
            url: '/bin/tetrapak/pw-productlisting',
            type: 'get',
            async: false,
            data: {
                productCategory: productCategory
            },
            success: function(data) {    
                        var totalResults = data.length;                
                        $('.product-cards').html("<h3>Products</h3>");
                        $('.product-cards').append("<h4>" + totalResults + " results found.</h4>");
                        $.each(data, function(i, obj) {
                            $('.product-cards').append("<p>Title : " + obj.title + "</p><p>Description : " + obj.description + "</p><p>Product Image : " + obj.productImage + "</p><p>Image Alt Text : " + obj.imageAltText + "</p><p>Link Text : " + obj.linkText + "</p><p>Link Path : " + obj.linkPath + "</p>");
                    });

            }
        });
    };
    event.preventDefault();
});
