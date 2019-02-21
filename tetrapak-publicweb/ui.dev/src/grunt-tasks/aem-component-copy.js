const config = require('../config.json');
module.exports = function (grunt) {
  var xmlBuilder = require('xmlbuilder');
  var path = require('path');

  grunt.registerMultiTask('aemcomponentcopy', 'Aem component copy', function () {
    var done = this.async();

    this.files.forEach(function (file) {

      var sourceFile = Array.isArray(file.src) ? file.src[0] : file.src;
      var destFile = Array.isArray(file.dest) ? file.dest[0] : file.dest;

      grunt.file.copy(sourceFile, destFile);
      grunt.verbose.writeln("Copy component ", sourceFile);
    });

    grunt.log.ok(this.files.length, " files processed");
    done();
  });
}
