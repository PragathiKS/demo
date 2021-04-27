jQuery(document).ready(
		function() {

			var s7assetFile = $("#s7assetFile").val();
			var dmDomain = $("#dmdomain").val();

			var videoViewer = new s7viewers.VideoViewer({
				"containerId" : "s7viewer",
				"params" : {
					"asset" : s7assetFile,
					"serverurl" : dmDomain + "/is/image/",
					"videoserverurl" : dmDomain + "/is/content/"
					//"caption" : dmDomain
					//		+ "/is/content/tetrapakdev/upc-video-subtitles-en"
				}
			}).init();

			$("#s7assetFile").remove();
			$("#dmdomain").remove();

		});