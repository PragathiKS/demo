const { wizard } = require('./ask');
const config = require('./config').createComponent;
const fs = require('fs-extra');
const questions = [
  {
    question: "Enter component name without spaces and special characters\nRules: \n1. Name should start with capital\n2. Name should not start with a number\n:",
    key: "componentName"
  },
  {
    question: "Enter component version number",
    key: "version",
    defaultValue: "v1"
  }
];

function getLatestVersion(versions) {
  if (Array.isArray(versions)) {
    versions.sort((curr, next) => {
      curr = +curr.replace('v');
      next = +next.replace('v');
      return next - curr;
    });
    return versions[versions.length - 1];
  }
}

function generateImports() {
  let importContent = fs.readFileSync(config.importsTemplate, 'utf8');
  const regex = /\{imports\}/;
  if (regex.test(importContent)) {
    let components = fs.readdirSync(config.componentsFolder);
    components.splice(components.indexOf('.DS_Store'), 1);
    if (Array.isArray(components)) {
      components.sort();
      const imports = [];
      components.forEach((component, index) => {
        let version = getLatestVersion(fs.readdirSync(`${config.componentsFolder}/${component}`));
        if (fs.existsSync(`${config.componentsFolder}/${component}/${version}/${component}/${component}.js`)) {
          let importStr = `case '${component}': import(\`../../templates/components/${component}/${version}/${component}/${component}\`).then(execute); break;\n    `;
          if (index === components.length - 1) {
            importStr = importStr.trim();
          }
          imports.push(importStr);
        }
      });
      importContent = importContent.replace(regex, imports.join(''));
      if (importContent) {
        fs.writeFileSync(config.importsJsFile, importContent, 'utf8');
      }
    }
  }
}

function createComponent(name, version) {
  // Create folder structure
  const componentPath = `${config.componentsFolder}/${name}/${version}/${name}`;
  fs.mkdirsSync(componentPath);
  // Create files
  fs.writeFileSync(`${componentPath}/${name}.scss`, '');
  fs.writeFileSync(`${componentPath}/${name}.js`, fs.readFileSync(config.componentTemplate, 'utf8').replace(/#component#/g, name));
  fs.writeFileSync(`${componentPath}/${name}-template.html`, `<sly data-sly-template.${name}_template="$\{@ data, flag\}"></sly>`);
  fs.writeFileSync(`${componentPath}/ux-model.json`, '{}');
  fs.writeFileSync(`${componentPath}/ux-preview.hbs`, `
  ---
  {
    "layout"	   : "app.hbs",
    "title"      : "",
    "fsdId"      : "${name}",
    "categories" : "components",
    "description": "",
    "components" : "${name}",
    "hiddenUX"   : true
  }
  ---
  <sly data-sly-call="$\{lib.${name}_template @data=${name}Model\}" />`);
  console.log('\x1b[32m%s\x1b[0m', `Component ${name} has been created!`);
  // Generate import
  generateImports();
}

wizard({ questions })
  .then(([data, version]) => {
    if (
      data.answer
      && !(/^\d|[^A-Za-z0-9]/).test(data.answer)
      && !(/[a-z]/).test(data.answer.charAt(0))
    ) {
      // Check if component already exists
      try {
        const componentList = fs.readdirSync(config.componentsFolder);
        if (componentList.includes(data.answer)) {
          // Check if the version number exists
          const versionList = fs.readdirSync(`${config.componentsFolder}/${data.answer}`);
          if (versionList.includes(version.answer)) {
            console.log('\x1b[31m%s\x1b[0m', `Component name and version exists!`);
          } else {
            createComponent(data.answer, version.answer);
          }
        } else {
          createComponent(data.answer, version.answer);
        }
      } catch (e) {
        console.log('\x1b[31m%s\x1b[0m', 'Something went wrong!');
      }
    } else {
      console.log('\x1b[31m%s\x1b[0m', 'You need to provide a valid component name');
    }
  })
  .catch(() => {
    console.log('\x1b[31m%s\x1b[0m', 'Something went wrong!');
  });
