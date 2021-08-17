const TerserPlugin = require("terser-webpack-plugin");
const commonConfig = require('./webpack.common');
const config = require('./config').webpack;
const { dev, prod } = config.modes;
commonConfig.mode = prod;
commonConfig.devtool = '(none)';
if (prod === dev) {
  commonConfig.optimization.usedExports = true;
  commonConfig.optimization.minimize = true;
  commonConfig.optimization.minimizer = [
    new TerserPlugin({
      cache: true,
      parallel: true,
      terserOptions: {
        output: {
          comments: false
        }
      }
    })
  ];
}
commonConfig.stats = {
  warnings: false
};

module.exports = commonConfig;
