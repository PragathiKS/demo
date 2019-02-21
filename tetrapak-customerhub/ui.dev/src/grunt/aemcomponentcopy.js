var config = require('../config.json');
module.exports = {
  uxlib: {
    options: {
      replaceTargetContentXml: false,
      useDummyContentXml: false,
      componentGroup: config.aem.componentGroup
    },
    files: [
      {
        expand: true,
        dot: true,
        cwd: config.aem.src.componentPath,
        src: ['**/*.html', '!**/*Sample.hbs'],
        dest: config.aem.target.componentPath
      },
      {
        expand: true,
        dot: true,
        cwd: config.aem.src.atomPath,
        src: ['**/*.html', '!**/*Sample.hbs'],
        dest: config.aem.target.atomPath
      }
    ]
  }
};
