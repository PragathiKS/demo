use(function () {
    if (request.getCookie("CustomerName")) {
    	var cookiValue = decodeURI(request.getCookie("CustomerName").getValue());
    }
    return {
        userName: cookiValue,
    };
});