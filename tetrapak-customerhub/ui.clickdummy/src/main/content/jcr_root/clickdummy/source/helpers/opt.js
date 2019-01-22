module.exports.register = function (Handlebars, options) {

    Handlebars.registerHelper('opt', function (key) {
        return options[key] || '';
    });

};