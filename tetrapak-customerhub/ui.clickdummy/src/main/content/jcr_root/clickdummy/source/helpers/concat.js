module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('concat', function () {
        // console.log(arguments)
        var arrayItems = Array.prototype.slice.call(arguments, 0, arguments.length - 1);
        return arrayItems.join('');
    });

};