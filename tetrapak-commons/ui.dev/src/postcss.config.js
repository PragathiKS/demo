module.exports = {
  plugins: [
    require('precss'),
    require('autoprefixer')({
      browsers: ['>0.5%', 'last 2 versions', 'not ie <= 10']
    })
  ]
}
