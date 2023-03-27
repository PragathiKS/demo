/**
 * Compares if first value equals than second value
 *
 * Usage:
 *
 * {{#if_noteq value1 value2}}
 *   ...
 * {{/if_noteq}}
 */

module.exports.register = function (Handlebars) {
  "use strict";
  Handlebars.registerHelper("if_noteq", function (first, second, options) {
    if (first !== second) {
      return options.fn(this);
    }
    return options.inverse(this);
  });
};