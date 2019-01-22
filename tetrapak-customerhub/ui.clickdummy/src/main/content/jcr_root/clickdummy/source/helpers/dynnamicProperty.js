module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('dynamicProperty', function (name, context) {
        return context[name];
    });

};