module.exports = {
  linting: {
    command: 'npm run eslint && npm run sasslint'
  },
  lintingDev: {
    command: 'npm run eslintDev && npm run sasslintDev'
  },
  webpackProd: {
    command: 'npm run prod'
  },
  webpackDev: {
    command: 'npm run dev'
  },
  removeChunkHash: {
    command: 'npm run removeHash'
  }
}
