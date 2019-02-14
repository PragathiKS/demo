module.exports = function (config) {
  config.set({
    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: 'source',
    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['mocha', 'chai', 'sinon'],
    files: [
      'tests/*.spec.js'
    ],
    // list of files to exclude
    exclude: [],
    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'scripts/core/core.js': ['webpack', 'sourcemap'],
      'tests/*.spec.js': ['webpack', 'sourcemap']
    },
    webpack: webpackConfig,
    reporters: ['progress', 'coverage', 'dots', 'junit'],
    // web server port
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
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
    singleRun: true,
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

