// Karma configuration
// Generated on fri Apr 21 2017
let path = require('path');

module.exports = function (config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: 'source',

    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['mocha', 'chai', 'sinon', 'fixture'],

    // list of files / patterns to load in the browser
    files: [],

    // list of files to exclude
    exclude: [
      '**/*.min.js',
      'node_modules/'
    ],

    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'templates/**/*.js': ['coverage'],
      '../app/jcr_root/etc/designs/roche/pharma/customerportal/jsonData/*.json': ['json_fixtures']
    },

    jsonFixturesPreprocessor: {
      stripPrefix: path.join(__dirname, 'app/jcr_root/etc/designs/roche/pharma/customerportal/jsonData/').replace(/[\\]/g, '/'),
      variableName: 'tpData'
    },

    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress', 'coverage', 'dots', 'junit'],

    // web server port
    port: 9876,

    // enable / disable colors in the output (reporters and logs)
    colors: true,

    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,

    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['PhantomJSCustom'],

    customLaunchers: {
      PhantomJSCustom: {
        base: 'PhantomJS',
        //debug: true,
        options: {
          windowName: 'my-window',
          viewportSize: {
            'width': 1920,
            'height': 1080
          }
        }
      }
    },

    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: true,

    // Concurrency level
    // how many browser should be started simultaneous
    concurrency: Infinity,
    coverageReporter: {
      type: 'html',
      dir: 'coverage/'
    },

    junitReporter: {
      outputFile: 'test-results.xml'
    }

  })
}

