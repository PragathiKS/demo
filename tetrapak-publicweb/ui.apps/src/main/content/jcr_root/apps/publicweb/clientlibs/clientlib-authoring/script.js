$(document).on("dialog-ready", function () {
$(".js-coral-Multifield-add").click(function() {
    var field = $(this).parent();
    var size = field.attr("data-maxlinksallowed");
    if (size) {
        var ui = $(window).adaptTo("foundation-ui");
        var totalLinkCount = $(this).prev('ol').children('li').length;
        if (totalLinkCount >= size) {
            ui.alert("Warning", "Maximum " + size + " fields are allowed!", "notice");
            return false;
        }
    }
});
});