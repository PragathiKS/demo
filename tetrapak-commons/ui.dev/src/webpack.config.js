const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
const commonConfig = require("./webpack.common");
const config = require("./config").webpack;

commonConfig.mode = config.modes.prod;
commonConfig.devtool = '(none)';
commonConfig.optimization.minimizer = [
  new UglifyJsPlugin({
    cache: true,
    parallel: true,
    uglifyOptions: {
      output: {
        comments: false
      }
    }
  }),
  new OptimizeCSSAssetsPlugin({})
];
commonConfig.stats = {
  warnings: false
};

module.exports = commonConfig;
