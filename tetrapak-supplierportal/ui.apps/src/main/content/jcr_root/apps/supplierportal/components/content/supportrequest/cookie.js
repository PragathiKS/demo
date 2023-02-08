use(function () {
    if (request.getCookie("SP-AEMCustomerEmail")) {
    	var cookieValue = decodeURI(request.getCookie("SP-AEMCustomerEmail").getValue());
    }
    return {
        email: cookieValue,
    };
});
