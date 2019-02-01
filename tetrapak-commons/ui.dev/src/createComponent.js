const { wizard } = require('./ask');
const config = require('./config').createComponent;
const fs = require('fs-extra');
const questions = [
  {
    question: "Enter component name\nRules: \n1. Name for class component should start with capital\n2. Name should not start with a number\n3. Name can contain an underscore\n:",
    key: "componentName"
  },
  {
    question: "Enter component version number",
    key: "version",
    defaultValue: "v1"
  }
];

function createComponent(name, version) {
  // Create folder structure
  const componentPath = `${config.componentsFolder}/${name}/${version}/${name}`;
  fs.mkdirsSync(componentPath);
  // Create files
  fs.writeFileSync(`${componentPath}/_${name}.scss`, '');
  fs.writeFileSync(`${componentPath}/${name}.js`, fs.readFileSync(config.componentTemplate, 'utf8').replace(/#component#/g, name));
  fs.writeFileSync(`${componentPath}/${name}-template.html`, `<sly data-sly-template.${name}_template="$\{@ data, flag\}"></sly>`);
  fs.writeFileSync(`${componentPath}/ux-model.json`, '{}');
  const previewHtml = fs.readFileSync(config.pageTemplate, 'utf8').replace(/#name#/g, name);
  fs.writeFileSync(`${componentPath}/ux-preview.hbs`, previewHtml);
  console.log('\x1b[32m%s\x1b[0m', `Component ${name} has been created!`);
}

wizard({ questions })
  .then(([data, version]) => {
    if (
      data.answer
      && !(/^\d|[^A-Za-z0-9_]/).test(data.answer)
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
