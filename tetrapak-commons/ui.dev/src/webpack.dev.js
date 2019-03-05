const commonConfig = require("./webpack.common");
const config = require("./config").webpack;

commonConfig.mode = config.modes.dev;
commonConfig.devtool = 'source-maps';

module.exports = commonConfig;
