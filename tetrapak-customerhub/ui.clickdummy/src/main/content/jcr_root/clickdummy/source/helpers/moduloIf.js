
'use strict';
module.exports.register = function (Handlebars) {
  Handlebars.registerHelper("moduloIf", function(index_count, mod, remainder, block) {
    if (parseInt(index_count) % mod === remainder) {
      return block.fn(this);
    }
    return block.inverse(this);
  });
}
