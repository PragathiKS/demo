var fs = require('fs-extra'),
  config = require('./config'),
  logger = require('./logger').logger,
  templates = require('./affiliateTemplates').templates,
  demoFolder = config.demoFolder,
  assetFolder = config.assetFolder,
  clientLibs = config.scss.clientlibs,
  sassComponentFolder = config.scss.components.path,
  supportedAffiliates = config.scss.components.regionCodes,
  defaultCopyPath = config.scss.components.defaultCopyPath,
  sassMap = {},
  demoContentDir = [];
Object.prototype[Symbol.iterator] = function* () {
  for (let key in this) {
    yield this[key];
  }
};

function getCategory(dirPart) {
  let categoryParts = dirPart.split('/');
  return (categoryParts.splice(categoryParts.length - 1)).concat(categoryParts).join('.');
}

function getFileName(dirPart) {
  let categoryParts = dirPart.split('/');
  return categoryParts.splice(categoryParts.length - 1)[0].split('.')[0];
}

function createClientlibSass(clientLib, affiliateFolder) {
  let clientlibSassPath = `${assetFolder}/styles/${affiliateFolder.join('/')}`,
    clientlibSrc = `${clientlibSassPath}/${clientLib.src}`,
    copyClientlibSrc = `${defaultCopyPath}/${clientLib.src}`;
  if (!fs.existsSync(clientlibSrc)) {
    // Create affiliate structure inside asset folder
    if (!fs.existsSync(clientlibSassPath)) {
      fs.mkdirsSync(clientlibSassPath);
    }
    // Create includes partial if it does not exist
    let includesPartial = `${clientlibSassPath}/_includes.scss`;
    if (!fs.existsSync(includesPartial)) {
      fs.createFileSync(includesPartial);
    }
    // Copy all the folders from default copy folder
    try {
      let filesAndFolders = fs.readdirSync(defaultCopyPath);
      if (filesAndFolders && filesAndFolders.length) {
        filesAndFolders = filesAndFolders.filter((file) => !(/.\.scss$/).test(file));
        filesAndFolders.forEach((folder) => {
          // Check if folder exists in current clientlib sass path
          let folderPath = `${clientlibSassPath}/${folder}`,
            copyFolderPath = `${defaultCopyPath}/${folder}`;
          if (!fs.existsSync(folderPath)) {
            fs.copySync(copyFolderPath, folderPath);
          }
        });
      }
    } catch (e) {
      logger('Some error occurred in folder copy');
      logger(e);
    }
    // Create clientlib sass files
    fs.createFileSync(clientlibSrc);
    // Check if current clientlibs sass file exists in copy folder then copy its content
    if (fs.existsSync(copyClientlibSrc)) {
      fs.copySync(copyClientlibSrc, clientlibSrc);
    }
  }
}

function createClientlibConfig() {
  // Create missing clientlibs configurations
  demoContentDir.forEach((folder) => {
    try {
      fs.readdirSync(folder.path);
    } catch (missing) {
      if (missing && missing.path) {
        // If directories are not found then create one with configurations
        fs.mkdirsSync(missing.path);
        // Create .content.xml
        let dirParts = missing.path.match(/\/clientlibs\/(.+)$/);
        if (dirParts) {
          let contentPath = missing.path + '/.content.xml',
            contentPathConfig = templates['.content.xml'].join('\n').replace(/\{category\}/, getCategory(dirParts[1]));
          fs.writeFileSync(contentPath, contentPathConfig);
        }
        // Create css.txt
        let textPath = missing.path + '/css.txt',
          textPathConfig = templates['css.txt'].join('\n').replace(/\{filename\}/, getFileName(dirParts[1]));
        fs.writeFileSync(textPath, textPathConfig);
      }
    }
  });
}

function createRegionHierarchy(regionCode) {
  return regionCode.toLowerCase().split('_').reverse();
}

function getLatestVersion(versions) {
  let verNos = versions.map((v) => +v.replace('v', ''));
  verNos.sort((v1, v2) => (v2 - v1));
  return `v${verNos[0]}`;
}

function generateSassImport(listOfFiles) {
  let inclusionTemplate = templates.sassImport.join('\n'),
    sassComponents = listOfFiles.map((f) => {
      if (f.startsWith('_')) {
        f = f.substring(1);
      }
      f = f.replace(/\.scss$/, '');
      return inclusionTemplate.replace(/\{component\}/, f);
    });
  return sassComponents.join('\n');
}

function addToSassMap(aff, filePath) {
  if (Array.isArray(sassMap[aff])) {
    sassMap[aff].push(filePath);
  } else {
    sassMap[aff] = [filePath];
  }
}

function generateImports() {
  for (let affPath in sassMap) {
    let includesFilePath = `${assetFolder}/styles/${affPath}/_includes.scss`,
      globalSassImportTemplate = templates.globalSassImport.join('\n');
    sassMap[affPath].forEach((path) => {
      fs.appendFileSync(includesFilePath, `${globalSassImportTemplate.replace(/\{importPath\}/, path)}\n`);
    });
  }
}

function generateCompAffiliateFolders(componentPath, component) {
  // Check if complete component path exists
  if (fs.existsSync(componentPath)) {
    try {
      let listOfFiles = fs.readdirSync(componentPath);
      if (listOfFiles && listOfFiles.length) {
        listOfFiles = listOfFiles.filter((f) => (/.\.scss$/).test(f));
        if (listOfFiles.length) {
          // Get inclusion text
          let sassImport = generateSassImport(listOfFiles);
          // Check affiliate folder existence
          supportedAffiliates.forEach((aff) => {
            let affiliateHierarchy = createRegionHierarchy(aff).join('/'),
              affiliatePath = `${componentPath}/${affiliateHierarchy}`,
              sassFileName = `${affiliatePath}/_c_${component}.scss`,
              sassFileImportPath = `${affiliatePath}/c_${component}`;
            if (!fs.existsSync(affiliatePath)) {
              // Create affiliate folder path
              fs.mkdirsSync(affiliatePath);
              // Write sass inclusion file
              fs.writeFileSync(sassFileName, sassImport);
              // Add sass file path into sass map for further processing
              addToSassMap(affiliateHierarchy, sassFileImportPath);
            }
          });
        } else {
          throw {};
        }
      }
    } catch (e) {
      logger(`SKIPPED: No valid sass files found for component ${component}`);
    }
  }
}

function createSassComponents() {
  try {
    let components = fs.readdirSync(sassComponentFolder);
    if (components && components.length) {
      components.forEach(function (component) {
        // Check if component has version folder(s)
        try {
          let componentPath = `${sassComponentFolder}/${component}`,
            versions = fs.readdirSync(componentPath);
          versions = versions.filter((v) => (/v\d+$/).test(v));
          if (versions.length) {
            // Get latest version
            let ver = getLatestVersion(versions),
              completeCompPath = `${componentPath}/${ver}/${component}`;
            generateCompAffiliateFolders(completeCompPath, component);
          } else {
            throw {};
          }
        } catch (e) {
          logger(`SKIPPED: Component ${component} doesn't have a valid version folder`);
        }
      });
    }
  } catch (e) {
    logger('Components unavailable');
  }
}

function resolveAffiliates() {
  // Create directory paths for supported clientlibs
  for (let clientLib of clientLibs) {
    if (clientLib.processRegions) {
      clientLib.regionCodes.forEach((regionCode) => {
        let affiliateFolders = createRegionHierarchy(regionCode);
        demoContentDir.push({
          path: `${demoFolder}/${affiliateFolders.join('/')}/${clientLib.category}`,
          affiliateFolders
        });
        createClientlibSass(clientLib, affiliateFolders);
      });
    }
  }
  createClientlibConfig();
  createSassComponents();
  generateImports();
}

resolveAffiliates();

exports.resolveAffiliates = resolveAffiliates;
