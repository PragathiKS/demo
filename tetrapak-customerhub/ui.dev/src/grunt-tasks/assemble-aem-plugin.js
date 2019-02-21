var path = require('path');
var xmlBuilder = require('xmlbuilder');
const config = require('../config.json');

/**
 * @param  {Object}   params
 * @param  {Function} callback
 */
module.exports = function (params, callback) {
  'use strict';

  var options = params.context;
  var grunt = params.grunt;

  var aemuxlib = options.aemuxlib || {};
  var pages = options.pages;
  var page = options.page;

  var async = grunt.util.async;

  aemuxlib.dest = aemuxlib.dest || page.filePair.orig.dest + '/tmp';
  aemuxlib.contentSection = aemuxlib.contentSection || '';
  aemuxlib.wrapJsonData = aemuxlib.wrapJsonData || false;

  //create demo content root
  grunt.file.mkdir(aemuxlib.contentRoot);

  async.forEachSeries(pages, function (file, next) {
    if (page.src !== file.src) { next(); return; }
    //create demo component for pages and component in the uxlib
    grunt.verbose.ok('Generating content descriptor for: '.yellow + file.dest);
    

    var dataJson = grunt.file.expand({ matchBase: true }, path.dirname(file.src) + path.sep + '*.json');
    for (var i = 0; i < dataJson.length; i++) {
      var dataJsonFile = dataJson[i];
      if (aemuxlib.wrapJsonData) {
        grunt.file.copy(dataJsonFile,
          path.join(path.dirname(file.dest),
            path.basename(dataJsonFile, '.json')) + '.js', {
            process: function (content) {
              return "use(function() {\n"
                + " var data = " + content + ";\n"
                + "data.requestURI = request.requestURI;\n"
                + "data.nodePathWithSelector = resource.path + '.ux-preview.html';\n"
                + "data.pagePathWithSelector = resource.path + '.ux-preview.html';\n"
                + " return data;\n"
                + "\n});"
            }
          });
      } else {
        grunt.file.copy(dataJsonFile, path.join(path.dirname(file.dest), path.basename(dataJsonFile, '.json')) + '.json');
      }
    }
    //demo content
    var componentResourcePath = file.dest.replace(aemuxlib.jcrFileRoot, '/');
    var componentSlingResource = path.dirname(componentResourcePath);
    var pageName = path.basename(path.dirname(file.src));
    if (file.basename == "ux-components" || file.basename == "ux-pages") {
      pageName = file.basename;
    }
    var demoPageFolder = path.join(aemuxlib.contentRoot, aemuxlib.contentSection, pageName);
    grunt.file.mkdir(demoPageFolder);
    next();
  }, function () {
    callback();
  });


};
