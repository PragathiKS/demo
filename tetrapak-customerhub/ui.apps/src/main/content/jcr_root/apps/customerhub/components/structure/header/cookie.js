use(function () {
    if (request.getCookie("CustomerName")) {
    	var cookiValue = request.getCookie("CustomerName").getValue();
    }
    return {
        userName: cookiValue,
    };
});