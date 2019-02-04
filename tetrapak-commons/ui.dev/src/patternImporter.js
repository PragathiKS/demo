const path = require('path');
const fs = require('fs-extra');
const { importPath, destPath, expiry } = require('./config').patternImporter;
const patterns = `${path.resolve(importPath)}/_patterns`;
const targetPatterns = `${path.resolve(destPath)}/templates/hbs-templates`;
const commonCss = `${path.resolve(importPath)}/css/scss/imports`;
const expiryMilliseconds = expiry * 24 * 60 * 60 * 1000;

let manifest = {
  patterns: {}
};

// Create manifest if not already present
if (!fs.existsSync('./patternmanifest.json')) {
  fs.writeFileSync('./patternmanifest.json', JSON.stringify(manifest, null, 2), 'utf8');
}

// Read existing manifest
try {
  manifest = JSON.parse(fs.readFileSync('./patternmanifest.json', 'utf8'));
  (function patternReader(manif, patterns, targetPatterns) {
    // Scan existing patterns
    const categories = fs.readdirSync(patterns).filter(pattern => (pattern !== '.gitkeep' && pattern !== '.gitignore'));

    categories.forEach(category => {
      let currentInstance = manif[category];
      let isNewEntry = true;
      if (currentInstance) {
        isNewEntry = currentInstance.newEntry;
      }
      if (!currentInstance) {
        currentInstance = manif[category] = {};
        currentInstance.newEntry = isNewEntry = true;
      }
      if (fs.statSync(`${patterns}/${category}`).isDirectory()) {
        currentInstance.isDir = true;
      }
      currentInstance.isFile = !currentInstance.isDir;
      if (isNewEntry) {
        currentInstance.currentPath = patterns;
        currentInstance.currentFilePath = `${patterns}/${category}`;
        currentInstance.targetPath = targetPatterns;
        currentInstance.targetFilePath = '';
      }
      if (!currentInstance.targetFilePath) {
        currentInstance.targetFilePath = `${currentInstance.targetPath}/${category}`;
        currentInstance.newEntry = false;
        if (currentInstance.isFile) {
          fs.copySync(currentInstance.currentFilePath, currentInstance.targetFilePath);
          console.log('\x1b[32m%s\x1b[0m', `Copied "${currentInstance.currentFilePath}" to "${currentInstance.targetPath}"`);
        }
        if (currentInstance.isDir) {
          fs.mkdirSync(currentInstance.targetFilePath);
          console.log('\x1b[32m%s\x1b[0m', `Created directory "${currentInstance.targetFilePath}"`);
        }
      }
      if (currentInstance.isDir) {
        // Recursive scan
        patternReader(manif[category], currentInstance.currentFilePath, currentInstance.targetFilePath);
      }
    });
  })(manifest.patterns, patterns, targetPatterns);
  fs.writeFileSync('./patternmanifest.json', JSON.stringify(manifest, null, 2), 'utf8');
} catch (e) {
  console.log('\x1b[31m%s\x1b[0m', 'Something went wrong!');
  console.log(e);
}
