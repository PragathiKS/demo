const webpack = require("webpack");
const path = require("path");
const webpackConfig = require("./config").webpack;

module.exports = function(config) {
  config.set({
    browsers: ["PhantomJSCustom"],
    customLaunchers: {
      PhantomJSCustom: {
        base: "PhantomJS",
        //debug: true,
        options: {
          windowName: "my-window",
          viewportSize: {
            width: 1920,
            height: 1080
          }
        }
      }
    },
    browserNoActivityTimeout: 60000,
    singleRun: true, //just run once by default
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    failOnEmptyTestSuite: false,
    frameworks: ["mocha", "chai", "sinon"],
    files: [
      "testcases.webpack.js" //just load this file
    ],
    preprocessors: {
      "**/*.js": "coverage",
      "testcases.webpack.js": ["webpack", "sourcemap"] //preprocess with webpack and our sourcemap loader
    },
    reporters: [
      "progress",
      "coverage",
      "coverage-istanbul",
      "dots",
      "junit",
      "verbose"
    ], //report results in this format
    coverageIstanbulReporter: {
      reports: ["html"],
      dir: "coverage/",
      fixWebpackSourcePaths: true,
      // enforce percentage thresholds
      // anything under these percentages will cause karma to fail with an exit code of 1 if not running in watch mode
      thresholds: {
        emitWarning: false, // set to `true` to not fail the test command when thresholds are not met
        // thresholds for all files
        global: {
          statements: 0,
          lines: 0,
          branches: 0,
          functions: 0
        }
      }
    },
    webpack: {
      devtool: "inline-source-map",
      mode: "development",
      module: {
        rules: [
          { test: /\.js$/, loader: "babel-loader" },
          {
            enforce: "post",
            test: /\.js$/,
            exclude: /((test-cases|node_modules|scripts)[\\/])|testcases\.webpack/,
            loader: "istanbul-instrumenter-loader",
            query: {
              esModules: true
            }
          },
          {
            test: /\.hbs$/,
            exclude: /node_modules/,
            loader: "handlebars-loader",
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
      }
    },
    webpackServer: {
      noInfo: true //please don't spam the console when running in karma!
    }
  });
};
