module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('rawJSON', function (data, options) {

        return new Handlebars.SafeString(JSON.stringify(data));

    });

};