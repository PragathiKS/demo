module.exports.register = function (Handlebars) {
    'use strict';

     Handlebars.registerHelper("each_with_index", function(array, options) {
        var buffer = "";
        for (var i = 0, j = array.length; i < j; i++) {
            var data = Handlebars.createFrame(options.data);
            data.index = i;
            options.data = data;
            buffer += options.fn(array[i], { "data": data });
        }
        return buffer;
    });
};