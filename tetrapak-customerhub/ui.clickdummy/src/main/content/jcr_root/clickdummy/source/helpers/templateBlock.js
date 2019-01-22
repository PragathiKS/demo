var _ = require('lodash');

module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('templateBlock', function (templateName) {
        // console.log(templateName, hash, this);
        var name = this[templateName + 'Sample'].title;
        // console.log(templateName, this[templateName+'Sample'].title);
        var component = _.filter(this.components, {'component': templateName});
        // console.log(component[0].pages[0].relativeLink);
        return '<a class="template-block" href="/#!/' + component[0].pages[0].relativeLink + '" target="_top">' + name + '</a>';
    });

};