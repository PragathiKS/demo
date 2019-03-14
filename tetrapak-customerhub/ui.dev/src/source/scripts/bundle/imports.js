import 'core-js/features/promise';
import 'core-js/features/string/includes';
import { logger } from '../utils/logger';

export const allImports = [];
const cImport = require.context('../../templates/components', true, /^(?!.*(?:spec.js$)).*\.js$/, 'lazy');

export default function (component, execute) {
  let components = cImport.keys();
  if (Array.isArray(components)) {
    components = components.filter(component => !(/\.spec\.js$/).test(component));
    const matched = components.filter(path => path.includes(`/${component}.js`));
    let cImportPr = null;
    if (matched.length === 1) {
      allImports.push(cImportPr = cImport(matched[0]));
      cImportPr.then(execute);
    } else if (matched.length > 1) {
      const latestVersion = 'v' + matched.map(match => {
        let dir = match.replace(`/${component}.js`);
        return dir.substring(dir.lastIndexOf('/', dir.lastIndexOf('/') - 1) + 1, dir.lastIndexOf('/'));
      }).map(version => +version.replace('v', '')).sort((v1, v2) => (v2 - v1))[0];
      if (
        latestVersion
        && (/v\d+$/).test(latestVersion)
      ) {
        const latestMatch = matched.filter(path => path.includes(`/${latestVersion}/${component}/${component}.js`))[0];
        allImports.push(cImportPr = cImport(latestMatch));
        cImportPr.then(execute);
      } else {
        logger.error('Something went wrong while importing bundle');
      }
    }
  }
}
