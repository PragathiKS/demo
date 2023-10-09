const path = require('path');
const webpackConfig = require('./config').webpack;

module.exports = function (config) {
  config.set({
    browsers: ['ChromeHeadlessCustom'],
    customLaunchers: {
      ChromeHeadlessCustom: {
        base: 'Chrome',
        //debug: true,
        flags: [
          '--headless',
          '--window-size=1920,1080',
          '--remote-debugging-port=9222',
          '--no-sandbox'
        ]
      }
    },
    browserNoActivityTimeout: 60000,
    singleRun: true, //just run once by default
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    failOnEmptyTestSuite: false,
    frameworks: ['mocha', 'chai', 'sinon'],
    files: [
      'testcases.webpack.js' //just load this file
    ],
    preprocessors: {
      '**/*.js': 'coverage',
      'testcases.webpack.js': ['webpack', 'sourcemap'] //preprocess with webpack and our sourcemap loader
    },
    reporters: [
      'progress',
      'coverage-istanbul',
      'dots',
      'junit',
      'verbose'
    ], //report results in this format
    coverageIstanbulReporter: {
      reports: ['html', 'json-summary'],
      dir: 'coverage/',
      fixWebpackSourcePaths: true
    },
    webpack: {
      devtool: 'inline-source-map',
      mode: 'development',
      module: {
        rules: [
          {
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'babel-loader'
          },
          {
            enforce: 'post',
            test: /\.js$/,
            exclude: /((test-cases|node_modules|scripts)[\\/])|testcases\.webpack|\.spec/,
            loader: 'istanbul-instrumenter-loader',
            query: {
              esModules: true
            }
          },
          {
            test: /\.hbs$/,
            exclude: /node_modules/,
            loader: 'handlebars-loader',
            options: {
              helperDirs: [
                path.join(__dirname, webpackConfig.handlebars.helpersFolder)
              ],
              partialDirs: [
                path.join(
                  __dirname,
                  webpackConfig.handlebars.currentRelativeFolder
                )
              ],
              precompileOptions: {
                knownHelpersOnly: false
              }
            }
          }
        ]
      },
      node: {
        fs: 'empty'
      },
      resolve: {
        alias: {
          handlebars: 'handlebars/runtime'
        }
      }
    },
    webpackMiddleware: {
      stats: 'errors-only'
    },
    junitReporter: {
      outputDir: 'coverage/', // results will be saved as $outputDir/$browserName.xml
      outputFile: 'junit-karma-report.xml', // if included, results will be saved as $outputDir/$browserName/$outputFile
      suite: 'tetrapak-common', // suite will become the package name attribute in xml testsuite element
      useBrowserName: false, // add browser name to report and classes names
      nameFormatter: undefined, // function (browser, result) to customize the name attribute in xml testcase element
      classNameFormatter: undefined, // function (browser, result) to customize the classname attribute in xml testcase element
      properties: {}, // key value pair of properties to add to the <properties> section of the report
      xmlVersion: null // use '1' if reporting to be per SonarQube 6.2 XML format
    }
  });
};
