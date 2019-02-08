const config = require("./config").webpack;
const path = require("path");
const CleanPlugin = require("clean-webpack-plugin");
const MiniCSSExtractPlugin = require("mini-css-extract-plugin");
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");

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
          return !!componentGroups[cacheGroup].filter(path => module.resource.includes(path)).length;
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
  mode: config.modes.dev,
  entry: entryPoints,
  devtool: 'inline-source-map',
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
          "css-loader",
          "postcss-loader",
          "sass-loader"
        ]
      },
      {
        test: /(\.js|\.jsx)$/,
        exclude: /node_modules/,
        use: [
          "babel-loader"
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
    })
  ]
}
