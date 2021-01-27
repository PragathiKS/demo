(function($, ns, channel, window, undefined) {
	
    $(document).on('foundation-contentloaded', function (e) {
        if ($('.pw-datepicker').length > 0 && $('.pw-datepicker').hasClass("cq-msm-lockable-field")) {
			var articleDate = $('.pw-datepicker').attr("value");
            if(articleDate != undefined && articleDate != ""){
             var articleDateWithOutTime = articleDate.split('T')[0];
             $('.pw-datepicker').find("input[name='./articleDate']").attr("value",articleDate);
             $('.pw-datepicker').find("input[is='coral-textfield']").attr("placeholder",articleDateWithOutTime);
            }
        }
    });
}(jQuery, Granite.author, jQuery(document), this));