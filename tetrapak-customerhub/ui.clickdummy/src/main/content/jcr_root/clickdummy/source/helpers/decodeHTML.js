module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('decodeHTML', function (data, options) {
    	var decodedData = new Handlebars.SafeString(data);
        return decodedData;
    });

};