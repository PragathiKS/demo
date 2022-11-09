module.exports.register = function (Handlebars) {
    'use strict';
    Handlebars.registerHelper('if_eq', function () {
        const args = Array.prototype.slice.call(arguments, 0, -1);
        return args.every(function (expression) {
            return args[0] === expression;
        });
    });
}