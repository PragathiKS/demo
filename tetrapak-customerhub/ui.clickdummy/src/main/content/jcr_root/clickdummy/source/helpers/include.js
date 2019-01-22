var _ = require('lodash');
var partial = require('./partial');


module.exports.register = function (Handlebars, options, params) {
    'use strict';

    var include = partial(Handlebars, options, params);

    Handlebars.registerHelper('include', function (name, context) {
        var insideText = context.fn && context.fn(this) || '';
        var result = include(name, _.extend(this, context), insideText) || '';
        // console.log('aaa',context.fn && context.fn(this));
        return new Handlebars.SafeString(result);
    });

};
