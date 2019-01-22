var _ = require('lodash');

module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('sortPages', function (pages, field) {
        return _.sortBy(pages, field);
    });
};
