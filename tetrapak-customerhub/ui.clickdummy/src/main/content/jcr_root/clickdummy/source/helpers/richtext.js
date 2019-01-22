function mixin(target) {

    var arg, key,
        len = arguments.length,
        i = 1;

    for (; i < len; i++) {
        arg = arguments[i];

        if (!arg) {
            continue;
        }

        for (key in arg) {
            /* istanbul ignore else */
            if (arg.hasOwnProperty(key)) {
                target[key] = arg[key];
            }
        }
    }

    return target;

}

module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper("richtext", function (options) {
        // Mix attributes into context
        var context = Object.create(this || {});
        mixin(context, options.hash);
        return options.fn && options.fn(context) || options;
    });

};