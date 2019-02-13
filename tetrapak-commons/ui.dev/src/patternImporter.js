require('colors');
const path = require('path');
const fs = require('fs-extra');
const exec = require('child_process').exec;
const { hasArgs } = require('./args');
const { wizard } = require('./ask');
// Pattern importer config
let patternImporterConfig = {};
function runWizard() {
  const questions = [
    {
      question: 'Patternlab source path (Where does your local patternlab code exists?): ',
      key: 'importPath'
    },
    {
      question: 'Patternlab destination path (Where do you want to import patternlab code?): ',
      key: 'destPath'
    },
    {
      question: 'Pattern expiry days (Expiry days of your pattern after which they become eligible for fresh import): ',
      key: 'expiry'
    }
  ];
  wizard({ questions }).then((input) => {
    input.forEach(option => {
      if (option.key === 'expiry') {
        option.answer = +option.answer;
        if (isNaN(option.answer)) {
          option.answer = 90;
        }
      }
      patternImporterConfig[option.key] = option.answer;
    });
    fs.writeFileSync('patternImporterConfig.json', JSON.stringify(patternImporterConfig, null, 2), 'utf8');
    runImporter();
  });
}
try {
  patternImporterConfig = require('./patternImporterConfig');
  if (!patternImporterConfig) {
    patternImporterConfig = {};
    runWizard();
  } else {
    runImporter();
  }
} catch (e) {
  runWizard();
}
function runImporter() {
  const { importPath, destPath, expiry } = patternImporterConfig;
  // Sources
  const patterns = `${path.resolve(importPath)}/_patterns`;
  const commonCss = `${path.resolve(importPath)}/css/scss/common`;
  const fonts = `${path.resolve(importPath)}/assets/fonts`;
  const icons = `${path.resolve(importPath)}/assets/icons`;
  // Targets
  const targetPatterns = `${path.resolve(destPath)}/templates-hbs`;
  const targetCommonCss = `${path.resolve(destPath)}/styles/global/common`;
  const targetFonts = `${path.resolve(destPath)}/assets/fonts`;
  const targetIcons = `${path.resolve(destPath)}/assets/icons`;
  const expiryMilliseconds = expiry * 24 * 60 * 60 * 1000;

  let manifest = {
    patterns: {},
    commonCss: {},
    fonts: {},
    icons: {}
  };

  if (
    hasArgs('force')
    && fs.existsSync('./patternmanifest.json')
  ) {
    // Remove manifest
    fs.removeSync('./patternmanifest.json');
  }

  // Create manifest if not already present
  if (!fs.existsSync('./patternmanifest.json')) {
    fs.writeFileSync('./patternmanifest.json', JSON.stringify(manifest, null, 2), 'utf8');
  }

  // Read existing manifest
  try {
    manifest = JSON.parse(fs.readFileSync('./patternmanifest.json', 'utf8'));
    function patternReader(manif, patterns, targetPatterns) {
      // Scan existing patterns
      const categories = fs.readdirSync(patterns).filter(pattern => (pattern !== '.gitkeep' && pattern !== '.gitignore'));

      categories.forEach(category => {
        let currentInstance = manif[category];
        let isNewEntry = true;
        if (currentInstance) {
          isNewEntry = currentInstance.newEntry;
          if (currentInstance.expiry <= Date.now()) {
            currentInstance.newEntry = isNewEntry = true;
            currentInstance.expiry = Date.now() + expiryMilliseconds;
          }
        }
        if (!currentInstance) {
          currentInstance = manif[category] = {};
          currentInstance.newEntry = isNewEntry = true;
          currentInstance.expiry = Date.now() + expiryMilliseconds;
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
          if (fs.existsSync(currentInstance.targetFilePath)) {
            fs.removeSync(currentInstance.targetFilePath);
          }
          if (currentInstance.isFile) {
            fs.copySync(currentInstance.currentFilePath, currentInstance.targetFilePath);
            console.log('\x1b[32m%s%s\x1b[0m', '[Imported]:', ` "${currentInstance.targetFilePath}"\n`.bold);
          }
          if (currentInstance.isDir) {
            fs.mkdirSync(currentInstance.targetFilePath);
            console.log('\x1b[32m%s%s\x1b[0m', '[Created directory]:', ` "${currentInstance.targetFilePath}"\n`.bold);
          }
        }
        if (currentInstance.isDir) {
          // Recursive scan
          patternReader(manif[category], currentInstance.currentFilePath, currentInstance.targetFilePath);
        }
      });
    }
    // Resolve patterns
    if (!fs.existsSync(targetPatterns)) {
      fs.mkdirsSync(targetPatterns);
    }
    if (fs.existsSync(patterns)) {
      patternReader(manifest.patterns, patterns, targetPatterns);
    } else {
      console.log('\x1b[31m%s\x1b[0m', 'Patterns do not exists in source. Please check the source path.');
    }
    // Resolve common styles
    if (!fs.existsSync(targetCommonCss)) {
      fs.mkdirsSync(targetCommonCss);
    }
    if (fs.existsSync(commonCss)) {
      patternReader(manifest.commonCss, commonCss, targetCommonCss);
    } else {
      console.log('\x1b[31m%s\x1b[0m', 'Common styles do not exists in source. Please check the source path.');
    }
    // Resolve font files
    if (!fs.existsSync(targetFonts)) {
      fs.mkdirsSync(targetFonts);
    }
    if (fs.existsSync(fonts)) {
      patternReader(manifest.fonts, fonts, targetFonts);
    } else {
      console.log('\x1b[31m%s\x1b[0m', 'Fonts do not exists in source. Please check the source path.');
    }
    // Resolve svg icons
    if (!fs.existsSync(targetIcons)) {
      fs.mkdirsSync(targetIcons);
    }
    if (fs.existsSync(icons)) {
      patternReader(manifest.icons, icons, targetIcons);
      exec('grunt webfont', function (err, stdout) {
        if (err) {
          console.log('\x1b[31m%s\x1b[0m', 'Some error occurred while generating icon fonts');
          return;
        }
        console.log(stdout);
        console.log('Icon fonts generated');
      });
    } else {
      console.log('\x1b[31m%s\x1b[0m', 'Icons do not exists in source. Please check the source path.');
    }
    fs.writeFileSync('./patternmanifest.json', JSON.stringify(manifest, null, 2), 'utf8');
  } catch (e) {
    console.log('\x1b[31m%s\x1b[0m', 'Something went wrong!');
    console.log(e);
  }
}
