module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('cleanTitle', function (inputString) {
        return inputString.replace(/ *\([^)]*\) */g, "");
    });

};