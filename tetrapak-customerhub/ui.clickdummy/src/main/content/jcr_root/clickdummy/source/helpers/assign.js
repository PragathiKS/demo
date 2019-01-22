module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('assign', function (element, val1, defaultVal) {
        var context = {};
        context[element] = val1 || defaultVal;
    });

    Handlebars.registerHelper('breaklines', function (text) {
        text = Handlebars.Utils.escapeExpression(text);
        text = text.replace(/(\r\n|\n|\r)/gm, '<br>');
        return new Handlebars.SafeString(text);
    });

    Handlebars.registerHelper("fetchimgurl", function (context, options) {
        if (context) {
            var tempArr = context.trim().split(options.hash["delimiter"] || "?");
            return tempArr[0];
        }
    });

};