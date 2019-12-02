use(function () {
    if (request.getCookie("AEMCustomerName")) {
    	var cookiValue = decodeURI(request.getCookie("AEMCustomerName").getValue());
    }
    return {
        userName: cookiValue,
    };
});
