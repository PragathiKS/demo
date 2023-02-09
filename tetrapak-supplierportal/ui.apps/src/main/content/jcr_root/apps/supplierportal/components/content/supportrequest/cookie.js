use(function () {
    if (request.getCookie("SP-AEMCustomerEmail")) {
    	var cookieValueEmail = decodeURI(request.getCookie("SP-AEMCustomerEmail").getValue());
    }
    if (request.getCookie("SP-AEMCustomerName")) {  
        var cookieValueName = decodeURI(request.getCookie("SP-AEMCustomerName").getValue());
    }
    return {
        email: cookieValueEmail,
        name: cookieValueName
    };
});
