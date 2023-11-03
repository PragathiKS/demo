const webpack = require("webpack");
const config = require("./config").webpack;
const clientlibs = require("./config").chunkrename;
const path = require("path");
const CleanPlugin = require("clean-webpack-plugin");
const MiniCSSExtractPlugin = require("mini-css-extract-plugin");
const ChunkRename = require("webpack-chunk-rename-plugin");
const { StatsWriterPlugin } = require("webpack-stats-plugin");

const resolve = {
  extensions: ['.js', '.ts']
};

// Resolve entry points
const entryPoints = (function () {
  let entryPoints = {};
  Object.keys(config.entry).forEach(key => {
    entryPoints[key] = path.resolve(__dirname, config.entry[key]);
  });
  return entryPoints;
}());

const componentGroups = config.componentGroups;

// Resolve cache groups
const cacheGroups = (function () {
  let cacheGroups = config.cacheGroups;
  Object.keys(cacheGroups).forEach((cacheGroup) => {
    let currentGroup = cacheGroups[cacheGroup];
    if (currentGroup.modifiers) {
      currentGroup.test = new RegExp(currentGroup.test, currentGroup.modifiers);
    } else if (currentGroup.testMultiple) {
      currentGroup.test = function (module) {
        if (module.resource) {
          return !!componentGroups[cacheGroup].filter(path => {
            const moduleResource = module.resource.replace(/[\\]/g, '/');
            return (moduleResource.includes(path) && !(/\.spec\.js$/).test(moduleResource));
          }).length;
        }
        return false;
      }
    } else {
      currentGroup.test = new RegExp(currentGroup.test);
    }
    delete currentGroup.modifiers;
    delete currentGroup.testMultiple;
  });
  return cacheGroups;
}());

module.exports = {
  output: {
    filename: config.filePath,
    chunkFilename: config.chunkFilePath,
    path: path.resolve(__dirname, config.root),
    publicPath: '/'
  },
  entry: entryPoints,
  optimization: {
    splitChunks: {
      cacheGroups: cacheGroups
    }
  },
  module: {
    rules: [
      {
        test: /\.(sc|sa|c)ss$/,
        exclude: /node_modules/,
        use: [
          MiniCSSExtractPlugin.loader,
          {
            loader: "css-loader"
          },
          {
            loader: "postcss-loader"
          },
          {
            loader: "resolve-url-loader"
          },
          {
            loader: "sass-loader",
            options: {
              sourceMap: true,
              sourceMapContents: false
            }
          }
        ]
      },
      {
        test: /\.(woff2?|ttf|otf|eot|svg)$/,
        exclude: /node_modules/,
        loader: 'file-loader',
        options: {
          name: `${config.fonts.fontPath}/[name].[ext]`,
          emitFile: false
        }
      },
      {
        test: /(\.js|\.jsx)$/,
        exclude: /node_modules/,
        use: [
          {
            loader: "babel-loader"
          },
          {
            loader: 'glob-import-loader',
            options: {
                resolve: resolve
            }
          }
        ]
      },
      {
        test: /\.hbs$/,
        exclude: /node_modules/,
        loader: "handlebars-loader",
        options: {
          helperDirs: [
            path.join(__dirname, config.handlebars.helpersFolder),
            path.resolve(config.handlebars.commonHelpersFolder)
          ],
          partialDirs: [
            path.join(__dirname, config.handlebars.currentRelativeFolder),
            path.resolve(config.handlebars.commonRelativeFolder)
          ],
          precompileOptions: {
            knownHelpersOnly: false
          }
        }
      }
    ]
  },
  plugins: [
    new CleanPlugin([
      config.clean
    ]),
    new MiniCSSExtractPlugin({
      filename: config.cssPath,
      chunkFilename: config.cssChunkPath
    }),
    new ChunkRename(clientlibs),
    new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
    new StatsWriterPlugin({
      stats: {
        all: true,
        assets: true
      }
    })
  ],
  node: {
    fs: 'empty'
  },
  resolve: {
    mainFields: ['main', 'module'],
    alias: {
      jquery: path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/jquery'),
      bootstrap: path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/bootstrap'),
      handlebars: path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/handlebars/runtime'),
      'core-js': path.resolve('../../../tetrapak-commons/ui.dev/src/node_modules/core-js'),
      tpCommon: path.resolve('../../../tetrapak-commons/ui.dev/src/source')
    }
  }
}