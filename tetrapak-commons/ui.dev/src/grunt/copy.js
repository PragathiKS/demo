var config = require('../config.json').copy;
module.exports = {
  jsonData: {
    files: config.jsonData
  },
  fonts: {
    files: config.fonts
  }
};
