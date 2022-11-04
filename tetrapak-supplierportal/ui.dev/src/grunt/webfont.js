var config = require("../config.json").webfont;
var pw_config = require("../config.json").pw_webfont;
module.exports = {
  common_icons: {
    src: config.src,
    dest: config.target,
    destScss: config.scss.target,
    options: {
      font: config.fontFile,
      syntax: "bootstrap",
      stylesheets: ["scss"],
      relativeFontPath: config.relativePath
    }
  },
  pw_icons: {
    src: pw_config.src,
    dest: pw_config.target,
    destScss: pw_config.scss.target,
    options: {
      font: pw_config.fontFile,
      syntax: "bootstrap",
      stylesheets: ["scss"],
      relativeFontPath: pw_config.relativePath
    }
  }
};
