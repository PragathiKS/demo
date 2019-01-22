module.exports.register = function (Handlebars) {
  Handlebars.registerHelper('greaterThanFive', function (index) {
    if (index > 4) {
      return 'hidden';
    }
  });

  Handlebars.registerHelper('equalToFive', function (index) {
    if (index === 5) {
      let html = '<li class="list-group-item js-display-hidden">\n' +
        '  <div class="radio">\n' +
        '    <label class="c-filter__label">\n' +
        '      <span class="customRadio"></span>\n' +
        window.Granite.I18n.get('rdoe_facet.seeMore') +
        '    </label>\n' +
        '  </div>\n' +
        '</li>';
      return html;
    }
  });

  Handlebars.registerHelper('i18keyToValue', function (key) {
    if (key) {
      return window.Granite.I18n.get(key);
    }
  });
}
