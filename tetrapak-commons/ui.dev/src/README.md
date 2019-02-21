# Installation

```sh
npm install
npm install -g grunt-cli
```

# Build

Development
```sh
npm run buildDev
```

Production
```sh
npm run build
```

# About

UI.Dev uses the power of <b>Webpack</b> and <b>Grunt</b> both. Webpack is used for compiling JS and SCSS. Grunt is used for deploying the changes to AEM server.

# Creating components, molecules and atoms

To create component run ``npm run createComponent``. Components are created under ``source/templates/components`` folder.<br><br>
For JavaScript we use ES6 modules. Each component is defined as a class with a mandatory init method. Each component is defined at organism level.

To create atoms run ``npm run createAtom``. Atoms are created under ``source/templates/atoms`` folder.<br><br>

To create molecules run ``npm run createMolecule``. Molecules are created under ``source/templates/molecules`` folder.<br><br>
<b>Note:</b> Atoms and Molecules do not have JS, ux-model and ux-preview files which are required for rendering component. However, they do have their own SASS files to defined CSS at atom/molecule level.<br><br>

JS Class Example:
```js
class MyComponent {
    init() {
        // Initializing component
    }
}
```

To execute ``MyComponent`` you need to define a ``data-module`` attribute inside HTL template file at component level.

```html
<div class="my-component" data-module="MyComponent">My Component renders here</div>
```

Please note that ``data-module`` should be same as your class name. You can also pass multiple comma separated modules.<br><br>

By default ``data-module`` will load a separate chunk for your component at runtime. This is done to keep front-end scripts light at first page load. However, we might require logical grouping of chunks in order to avoid multiple server requests. To learn more about it, refer to the <b>bundling</b> section of this page. <br><br>

# Bundling

We have a ``config.json`` file which holds all the configurations of different build tasks. Please refer to the ``webpack`` section for ``splitChunks`` and ``cacheGroups`` configurations. Cache groups are used by webpack's ``splitChunks`` configuration.<br>
A bundle can be configured in two ways:<br><br>
1. Using regex:<br><br>
We need to provide a test parameter, bundle name, minimum size and chunking logic (all, initial, or async). Please refer to <a href="https://webpack.js.org/plugins/split-chunks-plugin/">Webpack</a>'s official documentation for more details.<br><br>
2. Using multiple file names or paths:<br><br>
This is in addition to option 1. We can enable multiple files pattern matching by using ``testMultiple: true`` configuration. This is our internal implementation and not used by webpack. With ``testMultiple: true`` the test regex option will not work. In fact we need provide a list of component paths. To configure that, a separate section has been created called ``componentGroups``. Here against each chunk name we provide a list of components (paths) which we need to bundle together.

# CSS (Sass)

For styles we use Sass and for responsiveness we use Bootstrap framework. Webpack build compiles styles into two different chunks ``vendor`` and ``global``. There is no separate component based chunking for styles. The bootstrap being a vendor framework is outputted to ``vendor`` chunk. This is done to avoid compile and runtime linting checks on vendor specific files.

# SASS rules

1. For writing SASS we use BEM naming conventions.<br><br>
Example:<br><br>
```html
<div class="component">
    <div class="component__child">This is a child</div>
</div>
```

```css
.component {
    border: 1px solid;
    &__child {
        color: green;
    }
}
```

2. For classes that are referenced in JavaScript, it is adviceable to add a ``js-*`` prefix to the class. There should be separate classes for CSS and JS as shown below:<br>
```html
<div class="component js-component"></div>
```
<br><br>
3. There should not be more than 3 levels of nesting of classes in our sass code. Following example shows nesting of more than 3 levels which is wrong:<br>
```css
.component {
    .child__component {
        .child2__component {
            .child3__component {
                color: green;
            }
        }
    }
}
```

We have linting rules enabled which will detect these errors and stop the build.

# Linting
The linting rules have been enabled for both es6 and sass. The build does not proceed if a linting error is detected.

# Unit testing
For unit tests we use Karma, Mocha, Chai and Sinon.<br><br>
<b>Karma:<b> Task runner for test cases
<b>Mocha:<b> Testing framework
<b>Chai:</b> Assertion library
<b>Sinon:</b> Stub library<br><br>

Example of a test case:<br>
```js
describe('MyComponent', function () {
    it('should initialize on page load', function () {
        // ... perform an assertion
    });
});
```

Example of an assertion:<br>
```js
describe('MyComponent', function () {
    it('should initialize on page load', function () {
        expect(myComponent.init.called).to.be.true;
    });
});
```

To check if init was called we need ``sinon`` to spy ``myComponent.init``.<br>
```js
describe('MyComponent', function () {
    // Runs before tests
    before(function () {
        // ...
        this.myComponent = new MyComponent();
        sinon.spy(this.myComponent, 'init');
    });
    it('should initialize on page load', function () {
        expect(this.myComponent.init.called).to.be.true;
        // ...
    });
    // ...
});
```

Sometimes we may need to ``stub`` a method to change it's behavior entirely. For example, we want to test app behavior only on desktop. Again we need ``sinon`` for that.<br>
```js
describe('MyComponent', function () {
    // Runs before tests
    before(function () {
        // ...
        this.myComponent = new MyComponent();
        sinon.spy(this.myComponent, 'init');
        sinon.stub(commonUtils, 'isDesktop').returns(true);
    });
    it('should initialize on page load', function () {
        expect(this.myComponent.init.called).to.be.true;
        // ...
    });
    // ...
});
```

# Importing patterns from patternlab

Patterns (atoms, molecules, organisms, etc.) are created in patternlab as prototype versions. These patterns are then imported into accelerator for component development. To use patternlab, we need to have a local version of it since we need to refer to the patternlab source code. To clone a local version of patternlab please checkout following repository:<br><br>
<a href="https://del.tools.publicis.sapient.com/bitbucket/projects/TET/repos/patternlab/browse">https://del.tools.publicis.sapient.com/bitbucket/projects/TET/repos/patternlab/browse</a>
<br><br>
To import patterns from ``Patternlab`` you need to run following command:<br><br>
```sh
npm run importPatterns
```

Everytime you run this command, all patterns will be imported into the project regardless of whether a pattern already exists in the system. To avoid that, we need to run ``npm run importPatternsSafe`` command to ensure that only new patterns are imported and existing ones are remained untouched.<br><br>

### Configuration
When running the command, you will be asked to configure patternlab ``source`` and ``destination`` directories. This is a one time activity as pattern importer saves these configuration settings. To change the settings again, you need to change the ``patternImporterConfig.json`` file.<br><br>

### How to use imported patterns?
Imported patterns are ``handlebar`` files which are saved under ``source/templates-hbs`` directory. Handlebar patterns are used for client-side rendering of components in a headless manner. To render them, ``webpack`` pre-compiles these templates and save them as functions. To test how these templates work, follow the steps below:<br><br>

1. Create a component using ``npm run createComponent`` command.
2. Provide the name of component. Since we are creating a test component, we will name it as ``TestComponent``.
3. It will create a ``TestComponent`` component under ``source/templates/components`` directory including a ``TestComponent`` js file.
4. Let's assume the imported pattern was ``buttons.hbs`` atom. Webpack would have already pre-compiled this atom as a handlebars partial.
5. To access the ``buttons.hbs`` partial, create a handlebar component under ``source/templates-hbs/components`` folder. Let's name this component as ``testHbsComponent``.
6. Call the ``buttons.hbs`` inside ``testHbsComponent`` as follows:
```html
{{> atoms/buttons/buttons }}
```

<b>Note that</b> if button atom requires some data, we need to pass that using a valid handlebars partial syntax.

7. Update the component JS file as follows:
```js
import $ from 'jquery';
class TestComponent {
    constructor({ templates }) {
        this.templates = templates;
    }
    init() {
        $('#testApp').html(this.templates.testHbsComponent());
    }
}
```

Notice that we are calling testHbsComponent as a function. Webpack already does this pre-compilation work for us.

8. Last, but not the least, we need to add ``#testApp`` div inside html template file created as part of component to check if handlebars template renders correctly. We also need to add a ``data-module`` to execute the JavaScript component.

### How to create patterns in Sightly (HTL)?
At the moment, patternlab does not support sightly patterns. Therefore, we need to create them manually by referring to existing handlebars pattern. For example, if we wish to create ``buttons`` atom in sightly, we need to <b>manually</b> convert handlebars code into sightly code and then use it as a HTL partial inside component template file. To make it a bit easier, we have added following commands for you:<br><br>

```sh
npm run createAtom
npm run createMolecule
npm run createComponent
```

These commands create a boilerplate for implementing atoms and molecules in sightly.

### How to import styles?
Pattern importer imports styles for each and every pattern created inside patternlab. To use these styles we need to import partial scss files inside ``styles/default/en/base.scss``. Since ``base.scss`` is the entry point for project styles, all imports are managed inside this file. We would recommend to segregate these imports into separate partials. For example, styles for all atoms can be imported inside a ``_atomImports.scss`` partial file. If this file does not already exists, feel free to create one.<br><br>

# Generating Icon fonts
Pattern importer imports SVGs which are required for generating icon fonts. Icon fonts are automatically generated by pattern importer. However, if fonts generator is not configured properly, this step might fail. Don't worry! Your patterns would still be imported.<br><br>

To generate icon fonts we use ``grunt webfont`` plugin. To configure grunt webfont please follow the steps below:

1. Download the dependencies:
Windows:
- <a href="https://www.python.org/ftp/python/2.7.14/python-2.7.14.msi">Python</a>
- <a href="https://www.freetype.org/ttfautohint/#download">TTF Auto Hint</a>
- <a href="http://fontforge.github.io/en-US/downloads/windows/">Font forge</a>
Mac OSX:
```sh
brew install ttfautohint fontforge --with-python
```
Linux:
```sh
sudo apt-get install fortforge ttfautohint
```
2. Set PATH variable: Make sure that ``PATH`` variable is set for installed dependencies.<br><br>
3. Install webfont plugin: This should be installed already! However, if it doesn't work, run following command:
```sh
npm install --save-dev grunt-webfont
```

The webfont plugin should start working now.
