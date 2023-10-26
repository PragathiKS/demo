use(function () {
    if (request.getCookie("SP-AEMCustomerName")) {
    	var cookieValue = decodeURI(request.getCookie("SP-AEMCustomerName").getValue());
    }
    return {
        userName: cookieValue,
    };
});
