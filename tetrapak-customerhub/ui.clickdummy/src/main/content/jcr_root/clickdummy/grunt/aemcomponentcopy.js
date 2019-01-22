var config = require('../config.json');
module.exports = {
  uxlib: {
    options: {
      replaceTargetContentXml: true,
      useDummyContentXml: true,
      componentGroup: config.aem.componentGroup
    },
    files: [
      {
        expand: true,
        dot: true,
        cwd: config.aem.src.componentPath,
        src: ['**/*.html', '!**/*Sample.hbs'],
        dest: config.aem.target.componentPath
      }
    ]
  }
};
