const config = require("./config").webpack;
const path = require("path");
const CleanPlugin = require("clean-webpack-plugin");
const MiniCSSExtractPlugin = require("mini-css-extract-plugin");

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
          "css-loader",
          "postcss-loader",
          "sass-loader"
        ]
      },
      {
        test: /\.(woff2?|ttf|otf|eot|svg)$/,
        exclude: /node_modules/,
        loader: 'file-loader',
        options: {
          name: '[path][name].[ext]'
        }
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
          helperDirs: [path.join(__dirname, config.handlebars.helpersFolder)],
          partialDirs: [path.join(__dirname, config.handlebars.currentRelativeFolder)],
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
};
