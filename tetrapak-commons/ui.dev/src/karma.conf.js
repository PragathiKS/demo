const webpack = require('webpack');
const path = require('path');
const webpackConfig = require('./config').webpack;

module.exports = function (config) {
  config.set({
    browsers: ['ChromeHeadlessCustom'],
    customLaunchers: {
      ChromeHeadlessCustom: {
        base: 'ChromeHeadless',
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
      'testcases.webpack.js': ['webpack', 'sourcemap'] //preprocess with webpack and our sourcemap loader
    },
    reporters: ['progress', 'coverage-istanbul', 'dots', 'junit'], //report results in this format
    coverageIstanbulReporter: {
      reports: ['html'],
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
            exclude: /((test-cases|node_modules|scripts)[\\/])|testcases\.webpack/,
            loader: 'istanbul-instrumenter-loader',
            query: {
              esModules: true
            }
          },
          {
            test: /\.hbs$/,
            exclude: /node_modules/,
            loader: "handlebars-loader",
            options: {
              helperDirs: [path.join(__dirname, webpackConfig.handlebars.helpersFolder)],
              partialDirs: [path.join(__dirname, webpackConfig.handlebars.currentRelativeFolder)],
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
    webpackServer: {
      noInfo: true //please don't spam the console when running in karma!
    }
  });
};
