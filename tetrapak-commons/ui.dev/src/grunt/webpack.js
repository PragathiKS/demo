module.exports = {
  options: {
    stats: !process.env.NODE_ENV || process.env.NODE_ENV === 'development'
  },
  prod: require('../webpack.config'),
  dev: require('../webpack.dev')
};
