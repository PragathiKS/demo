/**
 * Compares if first value equals than second value
 *
 * Usage:
 *
 * {{#if_eq value1 value2}}
 *   ...
 * {{/if_eq}}
 */

module.exports.register = function (Handlebars) {
  "use strict";
  Handlebars.registerHelper("if_eq", function (first, second, options) {
    if (first === second) {
      return options.fn(this);
    }
    return options.inverse(this);
  });
};
