use(function () {
    if (request.getCookie("CustomerName")) {
    	var cookiValue = request.getCookie("CustomerName").getValue();
    	cookieValue = decodeURIComponent(cookieValue);
    }
    return {
        userName: cookiValue,
    };
});