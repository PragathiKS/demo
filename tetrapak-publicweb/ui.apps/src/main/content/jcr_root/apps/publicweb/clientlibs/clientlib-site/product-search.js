$(document).on('ready', function () {

    var productCategory = "all";

    if (document.getElementById("pw-search-results")) {
            getProductListing();
        }

    function performSearch() {
        
        $.ajax({
            url: '/bin/tetrapak/pw-productlisting',
            type: 'get',
            async: false,
            data: {
                productCategory: productCategory
            },
            success: function(data) {    
                        var totalResults = data.length;                
                        $('.search-results').html("<h3>Search Results </h3>");
                        $('.search-results').append("<h4>" + totalResults + " results found.</h4>");
                        $.each(data, function(i, obj) {
                            $('.search-results').append("<p><a href=\"" + obj.path + ".html\" >" + obj.title + "</a></p>");
                    });

            }
        });
    };
});
