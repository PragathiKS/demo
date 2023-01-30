module.exports = function (api) {
    api.cache(true);
    return {
      babelrcRoots: [
        '.',
        '../../../tetrapak-commons/ui.dev/src'
      ],
      presets: [
        '@babel/preset-env'
      ],
      plugins: [
        '@babel/plugin-proposal-class-properties',
        '@babel/plugin-transform-runtime',
        '@babel/plugin-syntax-dynamic-import',
        "@babel/plugin-proposal-object-rest-spread",
        "@babel/plugin-transform-modules-commonjs"
      ],
      env: {
        test: {
          plugins: [
            'istanbul'
          ]
        }
      }
    }
  }
  