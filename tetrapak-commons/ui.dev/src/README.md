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

Development with grunt deployment
```sh
npm run buildStart
```

Development environment with watch mode
```sh
npm run buildWatch
```

Production
```sh
npm run build
```

# About Accelerator

Accelerator works in two parts: ``ui.apps`` and ``ui.dev``<br>

### UI.Apps

AEM HTL components with dialog goes under this folder. Components are typically created under ``up.apps > src > main > content > jcr_root > apps``.

### UI.Dev

This folder is dedicated for front-end development. The front-end source code is present under ``ui.dev > src`` folder.

### Maven build

Maven build compiles ``ui.apps`` and ``ui.dev`` and create two separate packages which eventually get merged together at the time of deployment. The merging process automatically integrates front-end code with AEM HTL components.

### Splitting HTL component

HTL component is split into ``<component>.html`` and ``<component>-template.html``. First part is created under ``ui.apps`` folder and second part is placed in ``ui.dev`` folder. Second template is also used by front-end code for prototyping with mock json models and data.<br><br>

First template calls the second template as well as the actual Sling model required for rendering the component. Splitting HTL components in two parts allows front-end and back-end teams to work independently.

### How automatic integration works?

Let's assume we created a header component in following folder structure:<br><br>
``ui.apps > src > main > content > jcr_root > apps > customerhub > components > header.html``<br><br>

This component calls ``header-template.html`` which contains the actual HTML code and ``HeaderModel.java`` which provides data to the template file. This component is now expecting presence of ``header-template.html`` during the final build in order to render HTML correctly. At the time of deployment, ``ui.dev`` merges with ``ui.apps`` and places the ``header-template.html`` file inside the source folder of ``header.html``. Since both files are now present in same folder, component is now fully integrated.

# Front-end development

### Creating "Hello World" component

To create component you need to navigate to ``ui.dev > src`` folder and run ``npm run createComponent`` command from terminal. You will be asked to enter component name. The created component will be placed with empty structure inside ``ui.dev > src > source > templates > components``. Following files are created as part of ``createComponent`` command: <br><br>
1. ``<component>-template.html``: Empty HTL component
2. ``<component>.js``: JS file for the component
3. ``_<component>.scss``: CSS partial for the component
4. ``ux-model.json``: Mock JSON model for the component
5. ``ux-preview.html``: Page layout partial which is assembled as a page for running in local AEM instance

Create a component with name ``HelloWorld``.

### Adding "Hello World"

Edit the template file to add "Hello World" as HTML code:

```html
<sly data-sly-template.HelloWorld_template="${@ data, flag}">
    <div class="app">
        Hello World!
    </div>
</sly>
```

### Changing "Hello World" to "Hello Earth" using JavaScript

Edit JavaScript file and add following code:

```js
import $ from 'jquery';
class HelloWorld {
    constructor({ el }) {
        this.app = $(el);
    }
    init() {
        this.app.text('Hello Earth!');
    }
}
```

To call JavaScript code, add a ``data-module`` attribute to hello world app.

```html
<sly data-sly-template.HelloWorld_template="${@ data, flag}">
    <div class="app" data-module="HelloWorld">
        Hello World!
    </div>
</sly>
```

<b>Note:</b> The current reference of ``[data-module]`` element is passed as a root reference. This reference can be used to find elements present within current instance of sightly component. It's is recommended to always use this root reference to find elements within component. Using direct selectors can cause runtime issues in scenarios where a component is included more than once.

### Testing "HelloWorld" in browser

Start local AEM server on port 4502. Compile front-end code using ``npm run buildDev`` command and open <a href="http://localhost:4502/content/customerhub-ux/HelloWorld.ux-preview.html">http://localhost:4502/content/customerhub-ux/HelloWorld.ux-preview.html</a> in your browser. You should be able to view your component.

### Optimizing component

The component you just created will result into creation of a separate JavaScript chunk. For a single component per page this is fine. However, as you add more components in one page, the number of files loaded in runtime would increase. Hence it's important we bundle them up.<br><br>

Open ``config.json`` file and create a ``cacheGroup`` under ``webpack > cacheGroups``. Since we may be dealing with multiple components, it's important we make a cache group with ``testMultiple`` option as shown below:<br><br>

```json
{
    "webpack": {
        "cacheGroups": {
            "helloworld": {
                "testMultiple": true,
                "name": "helloworld",
                "enforce": true,
                "chunks": "all"
            }
        }
    }
}
```

Setting ``testMultiple`` to ``true`` allows us to group multiple components in a single bundle. To create a bundle we need to add another setting called ``componentGroups``.

```json
{
    "webpack": {
        "cacheGroups": {
            "helloworld": {
                "testMultiple": true,
                "name": "helloworld",
                "enforce": true,
                "chunks": "all"
            }
        },
        "componentGroups": {
            "helloworld": [
                "source/templates/components/HelloWorld/"
            ]
        }
    }
}
```

<b>Please note</b> that ``testMultiple`` option doesn't work with ``webpack`` configuration. This setting is used internally. To learn more about how this setting is used, please refer to ``webpack.common.js`` configuration.

### Splitting components into atoms and molecules:

A component can be further split into atoms and molecules. An ``atom`` is a smallest entity of an application. A group of atoms make a ``molecule``.<br><br>

Example of an atom:<br><br>

```html
<button>Search</button>
```

Example of a molecule:<br><br>
```html
<div class="input-wrapper">
    <label>Name:</label> <!-- Label atom -->
    <input type="text" name="name"> <!-- Input atom -->
</div>
```

Breaking components into atoms and molecules allows us to re-use same code in multiple components. To create atoms and molecules run following commands. Atoms and molecules are placed in their respective directories under ``source > templates``.

```sh
npm run createAtom
npm run createMolecule
```

### Creating handlebars components, atoms and molecules

For front-end applications which requires client-side rendering we use handlebars templating library. Handlebars files are pre-compiled by webpack and becomes available as an interface to JavaScript components. Handlebar files are created under ``source > templates-hbs`` using the same component hierarchy as HTL templates. Handlebar templates are called as shown in examples below:

```js
class HelloWorld {
    constructor({ templates, el }) {
        this.templates = templates;
        this.app = $(el);
    }
    init() {
        this.app.html(this.templates.helloWorld()); // <-- Method "helloWorld" is same as hbs file name
    }
}
```

There should be a ``helloWorld.hbs`` inside ``source > templates-hbs``.<br>
Please note that there is no automated way of creating handlebars components. You need to create them manually.

### Using render.js to for handlebar components

Render library has created to handle complex scenarios when rendering handlebar components. It makes it easier to render one or more than one templates together at one place. A simple example to show how ``render.js`` works is shown below:

```js
import { render } from '../../../scripts/utils/render';
class HelloWorld {
    constructor({ el }) {
        this.app = $(el);
    }
    init() {
        render.fn({
            template: 'helloWorld',
            target: this.app
        });
    }
}
```

To learn more about ``render.js`` please refer to ``HANDLEBARS.md``.

### Implementing SASS

SASS is a CSS pre-processor which allows us to modularize CSS files. We use SASS for our CSS files and ``Bootstrap`` as our base CSS framework. Each component, atom or molecule has it's own independent ``scss`` file. These files are then imported in ``source > styles > global > default > en > base.scss``.

### SASS guidelines

We use BEM (Block Element Modifier) guidelines for CSS. As per BEM guidelines we should structure HTML and CSS code in following manner:

```html
<div class="root">
    <div class="root__child">
        <div class="root__child__leaf"></div>
    </div>
</div>
```

```css
.root {
    &__child {
        &__leaf {
            color: red;
        }
    }
}
```

BEM allows us to nest children under parent node without increasing the overall spcificity.

### Using prefixes

To identify classes for atoms, molecules and components (organism) we prefer using following prefixes:

```css
.tpatom-* {} /* Atom */
.tpmol-* {} /* Molecule */
.tp-* {} /* Component */
.js-* {} /* Prefixes for JS specific classes */
```

### Using modifiers

A button can have multiple states. For example a button can be active or disabled. Disabled state is a modifier for button. In CSS we add a modifier to the button class to distinguish disabled button from active button using ``--`` separator shown below:

```html
<button class="atom-btn">Click Me</button>
<button class="atom-btn--disabled">I am disabled!</button>
```

# Linting
The linting rules have been enabled for both es6 and sass. The build does not proceed if a linting error is detected.

# Unit testing
For unit tests we use Karma, Mocha, Chai and Sinon.<br><br>
<b>Karma:</b> Task runner for test cases
<b>Mocha:</b> Testing framework
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

# Generating Icon fonts
Pattern importer imports SVGs which are required for generating icon fonts. Icon fonts are automatically generated by pattern importer. However, if fonts generator is not configured properly, this step might fail. Don't worry! Your patterns would still be imported.<br><br>

To generate icon fonts we use ``grunt webfont`` plugin. To configure grunt webfont please follow the steps below:

1. Download the dependencies:
<br>Windows:
- <a href="https://www.python.org/ftp/python/2.7.14/python-2.7.14.msi">Python</a>
- <a href="https://www.freetype.org/ttfautohint/#download">TTF Auto Hint</a>
- <a href="http://fontforge.github.io/en-US/downloads/windows/">Font forge</a>
<br>Mac OSX:
```sh
brew install ttfautohint fontforge --with-python
```
<br>Linux:
```sh
sudo apt-get install fortforge ttfautohint
```
2. Set PATH variable: Make sure that ``PATH`` variable is set for installed dependencies.<br><br>
3. Install webfont plugin: This should be installed already! However, if it doesn't work, run following command:
```sh
npm install --save-dev grunt-webfont
```

The webfont plugin should start working now.
