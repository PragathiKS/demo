
module.exports.register = function (Handlebars) {
  Handlebars.registerHelper("increment", function(value, options)
  {
      return parseInt(value) + 1;
  });
}
