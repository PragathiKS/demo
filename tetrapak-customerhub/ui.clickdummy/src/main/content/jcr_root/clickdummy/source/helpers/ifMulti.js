var _ = require('lodash');

module.exports.register = function (Handlebars) {
    'use strict';

    Handlebars.registerHelper('ifMulti', function(v1, v2, options) {

          v1 = v1.split(',');
          if (_.indexOf(v1, v2) !== -1) {
              return options.fn(this);
          }
          return options.inverse(this);
      });
};
