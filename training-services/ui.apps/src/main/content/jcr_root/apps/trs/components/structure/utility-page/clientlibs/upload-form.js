jQuery(document).ready(function() {

	$("#upload-button").click(function(e) {
		e.preventDefault();
		var formData = new FormData($('form')[0]);
		var file = $('#file-upload-input').val().trim();
		if (file) {
			$.ajax({
				type : "POST",
				url : $('#formSubmitUrl').val(),
				contentType : false,
				processData : false,
				data : formData,
				success : function(result) {
					$("#success-message").html(result);
					$("#failure-message").css('visibility', 'hidden');
					$("#success-message").css('visibility', 'visible');

				},
				error : function(xhr, ajaxOptions, thrownError) {
					$("#success-message").css('visibility', 'hidden');
					$("#failure-message").html('Operation failed!');
					$("#failure-message").css('visibility', 'visible');
				}
			});
		} else {
			$("#success-message").css('visibility', 'hidden');
			$("#failure-message").html('Please upload a file!');
			$("#failure-message").css('visibility', 'visible');
		}
	});

	$('form input').change(function() {
		var file = $('#file-upload-input')[0].files[0].name;
		$('form p').text(file);
	});
});