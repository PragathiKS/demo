module.exports = {
  linting: {
    command: 'npm run eslint && npm run sasslinter'
  },
  lintingDev: {
    command: 'npm run eslintDev && npm run sasslinterDev'
  },
  karmaTests: {
    command: 'npm run test'
  },
  webpackProd: {
    command: 'npm run prod'
  },
  webpackDev: {
    command: 'npm run dev'
  },
  clientlibify: {
    command: 'npm run createClientlib'
  }
}
