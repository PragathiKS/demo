$(document).on('ready', function () {

    if (document.getElementById("pw-search-results")) {
            performSearch();
        }

    var urlValues = getUrlVars();  

    $('#f_search_inp').bind("enterKey",function(e){
        var urlValue = window.location.href;
        var queryTerm = $('#f_search_inp').val();

        $("#f_search_inp").val("");              
        var updatedURLAfterQueryTerm = updateQueryStringParameter(urlValue, "q", queryTerm);
        
        var state = {
            "search": true
        };    

        if (!!(window.history && history.pushState)) {
            e.preventDefault();
            window.history.pushState(state, document.title, updatedURLAfterQueryTerm);
        }

        window.location = $("#search-data-resultspath").attr("data") + ".html?q=" + queryTerm;

    });

    $('#f_search_inp').keyup(function(e){
        if(e.keyCode == 13)
        {
          $(this).trigger("enterKey");
        }
    });

    function performSearch() {
        var searchRootPath = $("#search-data-rootpath").attr("data");
        var searchResultsPath = $("#search-data-resultspath").attr("data");

        var queryTerm = "";
        var urlVals = getUrlVars(); 
        var encodedQueryTerm = "";
        
        for (var i = 0; i < urlVals.length; i++) {  
            var urlVal = urlVals[i];
            if (urlVal === 'q'){
                queryTerm = decodeURI(getUrlVars()[urlVal]);
            } 
        }
        encodedQueryTerm = encodeURIComponent(queryTerm);

        $.ajax({
            url: '/bin/tetrapak/pw-search',
            type: 'get',
            async: false,
            data: {
                searchRootPath: searchRootPath,
                searchResultsPath: searchResultsPath,
                fulltextSearchTerm : encodedQueryTerm,
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

//  Read a page's GET URL variables and return them as an associative array.
function getUrlVars() {
    var vars = [], hash;
    if(window.location.href.indexOf("?") > -1) {
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');

            if($.inArray(hash[0], vars)>-1) {
                vars[hash[0]]+=","+hash[1];
            } else {
                vars.push(hash[0]);
                vars[hash[0]] = hash[1];
            }
        }
    }
    return vars;
}

//  Send URL without Query String
function getPathFromUrl(url) {
    return url.split(/[?#]/)[0];
}

function updateQueryStringParameter(uri, key, value) {
    var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    if (uri.match(re)) {
        return uri.replace(re, '$1' + key + "=" + value + '$2');
    } else {
        return uri + separator + key + "=" + value;
    }
}
