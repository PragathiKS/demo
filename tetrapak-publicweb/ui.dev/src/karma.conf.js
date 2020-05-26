const path = require('path');
const webpackConfig = require('./config').webpack;
const argv = require('yargs').argv;

const mode = argv.mode || 'development';

module.exports = function (config) {
  config.set({
    browsers: ['ChromeHeadlessCustom'],
    customLaunchers: {
      ChromeHeadlessCustom: {
        base: 'Chrome',
        flags: [
          '--headless',
          '--window-size=1920,1080',
          '--remote-debugging-port=9222',
          '--no-sandbox'
        ]
      }
    },
    browserNoActivityTimeout: 60000,
    browserDisconnectTolerance: 2,
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
      reports: ['html'],
      dir: 'coverage/',
      fixWebpackSourcePaths: true,
      // enforce percentage thresholds
      // anything under these percentages will cause karma to fail with an exit code of 1 if not running in watch mode
      thresholds: {
        emitWarning: (mode === 'development'), // set to `true` to not fail the test command when thresholds are not met
        // thresholds for all files
        global: {
          statements: 80,
          lines: 80,
          branches: 50,
          functions: 80
        }
      }
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
                path.join(__dirname, webpackConfig.handlebars.helpersFolder),
                path.resolve(webpackConfig.handlebars.commonHelpersFolder)
              ],
              partialDirs: [
                path.join(
                  __dirname,
                  webpackConfig.handlebars.currentRelativeFolder
                ),
                path.resolve(webpackConfig.handlebars.commonRelativeFolder)
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
        mainFields: ['main', 'module'],
        alias: {
          jquery: path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/jquery'),
          bootstrap: path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/bootstrap'),
          handlebars: path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/handlebars/runtime'),
          'core-js': path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/core-js')
        }
      }
    },
    webpackMiddleware: {
      stats: 'errors-only'
    }
  });
};
