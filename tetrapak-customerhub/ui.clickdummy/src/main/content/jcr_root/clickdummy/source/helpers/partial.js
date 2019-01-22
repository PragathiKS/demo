/**
 * Markdown Helper {{partial}}
 * Copyright (c) 2014 Jon Schlinkert, contributors
 * Licensed under the MIT License.
 */
'use strict';

var _ = require('lodash');


/**
 * Convenience method for creating `include`/`partial`
 * helpers, using the correct context.
 */

module.exports = function (Handlebars) {

    return function (name, context, insideText) {
        // console.log(insideText && 'insideText'+insideText);
        var hash = context.hash || {};
        var insideHtml = insideText && {"insideHtml": insideText} || {};
        var ctx = _.extend({}, context || {}, hash, insideHtml);
        var template = Handlebars.partials[name];
        var fn = Handlebars.compile(template);
        return fn(ctx || '');
    };

};