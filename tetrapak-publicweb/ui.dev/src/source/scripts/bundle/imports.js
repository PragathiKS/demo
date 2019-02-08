import 'core-js/features/promise';
import 'core-js/features/string/includes';
import { logger } from '../utils/logger';

const cImport = require.context('../../templates/components', true, /\.js$/, 'lazy');

export default function (component, execute) {
  const components = cImport.keys();
  if (Array.isArray(components)) {
    const matched = components.filter(path => path.includes(`/${component}.js`));
    if (matched.length === 1) {
      cImport(matched[0]).then(execute);
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
        cImport(latestMatch).then(execute);
      } else {
        logger.error('Something went wrong while importing bundle');
      }
    }
  }
}
