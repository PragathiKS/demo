module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('code', function (name) {
        // console.log(name,Handlebars.partials[name]())
        var partial = Handlebars.partials[name];
        return partial();
    });

};