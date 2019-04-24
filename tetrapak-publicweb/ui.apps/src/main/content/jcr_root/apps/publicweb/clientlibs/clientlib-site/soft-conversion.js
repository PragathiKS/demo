$(document).on('ready', function () {

    $('#soft-conversion-form').hide();
    $('#whitepaper-details').hide();

    $('.soft-conversion-form-init').on('click', function(){
        if (!$.cookie('softConvUserExists')){
			$('#soft-conversion-form').show();		
        } else {
            $('#whitepaper-details').show();
        }
    });
});
