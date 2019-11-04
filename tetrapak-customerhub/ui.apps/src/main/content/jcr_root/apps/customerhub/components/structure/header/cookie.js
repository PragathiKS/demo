use(function () {
    if (request.getCookie("CustomerName")) {
    	var cookiValue = decodeURI(request.getCookie("AEMCustomerName").getValue());
    }
    return {
        userName: cookiValue,
    };
});
