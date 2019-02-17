const fs = require('fs-extra');

if (!fs.existsSync('source/test-cases')) {
  fs.mkdirsSync('source/test-cases');
}
