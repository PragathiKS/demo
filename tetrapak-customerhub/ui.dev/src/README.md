# Installation

```sh
npm install
npm install -g grunt-cli
```

# Build

Development build without grunt deployment
```sh
npm run buildDev
```

Development with grunt deployment
```sh
npm run buildStart
```

Development build with watch mode
```sh
npm run buildWatch
```

Optimized production build
```sh
npm run build
```

# JavaScript framework

Accelerator JS framework is based on ES6 modules and Webpack. Latter is used for compiling and bundling ES6 modules to web compatible ES5.<br>

Conventional ES5 module:

```js
(function ($) {
    'use strict';
    ...
    console.log($.param({
        data: 'Hello World'
    }));
})(window.jQuery);
```

ES6 module:

```js
import $ from 'jquery';

console.log($.param({ ... }));
```

ES6 modules are not wrapped inside an IIFE like ES5 and does not require ``use strict`` statement. Advantage of using ES6 modules is that they don't pollute global (window) namespace.<br>

Webpack compiles ES6 modules to ES5 and create minified bundles for production use.

## Code splitting

The current framework supports code splitting using ``SplitChunks`` webpack plugin. Code splitting is crucial to avoid duplicate code when importing and exporting modules. For example, ``vendor`` chunk is split separately which contain libraries imported from ``node_modules``. The framework also support dynamic imports to load chunks at runtime without the need of creating script tags:

### Loading chunks:
<b>Using ``data-module`` attribute</b>:<br> The framework automatically imports and execute module chunks without the need of writing an explicit ``import`` statement or a script tag. Such modules are loaded on page load.<br>
For example:
```html
<div data-module="HelloWorld"></div>
```
Webpack loader will look for ``HelloWorld.js`` file in components folder and generate a dynamic chunk. When the page is loaded, webpack automatically loads this chunk to execute ``HelloWorld.js``. All of this happens automatically. Although, you do need to make sure that JS modules follow a specific pattern.<br>
<b>Using ``import()`` function</b>:<br> You can use dynamic ``import`` function lazy loading chunks. For example, you can load a separate chunk for an overlay module when user performs a click action. Such chunks can be loaded on demand.

## JavaScript module pattern

The current framework supports ``class`` based modules which is a new feature in ES2015. A simple class based module is shown below:

```js
class HelloWorld {
    init() {
        logger.log('Hello World!');
    }
}
export default HelloWorld;
```
The ``init`` method is required for the framework to load and initialize this component.<br>
The framewoek also supports object based pattern shown below:

```js
export default {
    init() {
        logger.log('Hello World!');
    }
}
```

It is recommended to use ``class`` based modules over objects.

## Create component command

The current framework provides with an npm command ``createComponent`` which creates HTML, JavaScript and SCSS files with default boilerplate code compatible with framework's runtime. You don't have to worry about writing an ``init`` method in your JavaScript component since it's already there. Just add functional logic to your component and you are good to go!<br>

```sh
npm run createComponent
```

Create component command reduces errors and speeds up the process of creating components.

### Creating atoms and molecules using createComponent

You can create ``atoms`` and ``molecules`` using ``createComponent`` command. There are two ways of doing that:<br>

Using ``createComponent``:<br>

```sh
npm run createComponent -- --atom
npm run createComponent -- --molecule
```

Using ``createAtom`` and ``createMolecule`` aliases:<br>

```sh
npm run createAtom
npm run createMolecule
```

Atoms and molecules don't have JavaScript files.

## Cache groups

By default webpack generates separate chunk for each module imported using ``data-module`` attribute. The advantage of having separate chunks is that your webpage downloads limited number of script files required for the application.<br>
Imagine loading a huge bundle (in comparison to loading smaller sized chunks) containing half of the code which is not even being used.<br>
The downside however is that it results into too many network requests for your application.<br>
Fortunately, webpack provides with an option to group two or more chunks together using ``cacheGroup`` configuration. You can find this configuration in ``config.json`` file.

### Creating cache groups

Inside ``config.json`` under ``webpack`` configuration you can create a ``cacheGroup`` as follows:

```json
{
    "webpack": {
        "cacheGroups": {
            "myComponentGroup": {
                "testMultiple": true,
                "name": "myComponentGroup",
                "enforce": true,
                "chunks": "all"
            }
        },
        "componentGroups": {
            "myComponentGroup": [
                "/path/to/first/component",
                "/path/to/second/component"
            ]
        }
    }
}
```

# CSS Framework

Accelerator CSS framework uses SASS pre-processor and Bootstrap 4 Grid System modified as below:

### Breakpoints (as per design guidelines):
xs: ``0px - 575px``
sm: ``576px - 767px``
md: ``768px - 1023px``
lg: ``1024px - 1199px``
xl: ``1200px`` and above

### Mobile and Desktop views (as closed with design team)
Mobile: ``&lt; 1024px``
Desktop: ``&gt;= 1024px``

### Breakpoint variables (tetrapak-commons)
``$mobile``: &lt; 576px
``$mobile-large``: &lt; 1024px
``$desktop``: &gt;=1024px
``$desktop-large``: &gt;=1200px
``$mobile-landscape``: Same as ``$mobile`` with orientation as ``landscape``
``$mobile-large-landscape``: Same as ``$mobile-large``

You can check the definition in ``tetrapak-commons > ui.dev > src > source > styles > global > common > _media.scss``.

## Mobile first approach

The CSS framework implements mobile first approach. Therefore, you may never have to use breapoint variables other than ``$desktop``.

```scss
.my-class {
    /* Mobile CSS */
    @media #{$desktop} {
        /* Desktop CSS overrides if required */
    }
}
```

## Base CSS and component max-width

The base CSS has already been implemented in ``tetrapak-commons``. Some components follow maximum width of ``1366px`` as per design. To set ``1366px`` max-width you need to use ``tp-container`` class.<br>
To check the base css implementation go to ``tetrapak-commons > ui.dev > src > source > styles > global > common > _global.scss``.

## Fonts

For English language we are using ``Muli`` font and it's variants. The font family definitions and variables can be found in ``tetrapak-commons > ui.dev > src > source > styles > global > common > _fonts.scss`` and ``tetrapak-commons > ui.dev > src > source > styles > global > common > _typography.scss``.

## Using REM

For units we are using ``rem``. For converting PX to REM we use mixins as follows:

```scss
.my-class {
    @include margin-top(10px);
    @include padding(10px 20px 0 0);
    @include font-size(12px);
    @include line-height(24px, 12px); // Unitless
    @include border-radius(3px);
}
```

In some cases where mixins can't be used, we can use ``convert-to-rem`` function.

```scss
.my-class {
    width: calc(100% - #{convert-to-rem(20px)});
}
```

## BEM guidelines and prefixes

For CSS we are using BEM (Block Element Modifier). Let's understand how BEM works using a simple example below:

```html
<div class="tp-comp">
  <div class="tp-comp__child">
  </div>
</div>
```

The child element has the same root class along with the suffix "__child". In SASS we can nest the child element using the partial suffix as shown below:

```scss
.tp-comp {
    color: red;
    &__child {
        color: green;
    }
}
```

The final output after compilation is shown below:

```css
.tp-comp {
    color: red; }
  .tp-comp__child {
        color: green; }
```

BEM convention allows us to perform SASS style nesting as well as reduce selector specificity. To read more about BEM please go through the link below:<br>
<a href="http://getbem.com/">Block Element Modifier</a>

### Prefixes

To differentiate between atoms, molecules and organism (components) we are using following prefixes in our CSS classes:<br>
Atoms: ``tpatom-*``
Molecules: ``tpmol-*``
Organism (Component): ``tp-*``
JS classes: ``js-*``
<br>
Please note that JS classes should be separate from classes used for styling.

## Icons

For icons we are using ``iconfonts``. Icon fonts are generated using ``svg`` icon files and ``grunt-webfont`` plugin.

## Generating Icon fonts

To generate icon fonts we use ``grunt-webfont`` plugin. To configure grunt webfont please follow the steps below:

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

# HTML Markup and templating

For server side markup (components) we are using ``HTL`` template language (formerly known as Sightly) compatible with ``AEM 6+``. A typical HTL template looks like below:

```html
<sly data-sly-template.helloworld_template="${@ data}">
    <div>${data}</div>
</sly>
```

Sightly templates are called using following syntax:

```html
<sly data-sly-use.hwtemplate="/path/to/helloworld.html" data-sly-call="${hwtemplate.helloworld_template @ data = 'Hello World!'}" />
```

To learn more about HTL please follow the link below:
<a href="https://docs.adobe.com/content/help/en/experience-manager-htl/using/getting-started/getting-started.html">HTL Templating language Getting Started</a>

## Create component command and HTL

Create component command creates a default HTL template file where you define the HTML of your component. It also creates two other files named as ``ux-model.json`` and ``ux-preview.hbs``. These files are used for assembling a page for developing HTL component and providing a mock JSON model. You can preview your component in local AEM instance using following URL:<br>

http://localhost:4502/content/customerhub-ux/&lt;component-name&gt;.ux-preview.html<br>

Component name is same as component's ``fsdId``. The ``fsdId`` field is configured in ``ux-preview.hbs`` file. This field is pre-configured. It also configures the base page layout. Layouts are defined under ``ui.dev > src > source > template > layouts``. The default layout is ``app.hbs``. You can also create custom layouts according to your needs and configure them in ``ux-preview.hbs``.

## Handlebars

For client side templating we use handlebars. A handlebar template look like below:<br>

```hbs
<div class="tp-comp">
    {{prop}}
    <div class="tp-comp__child">
        {{childProp}}
    </div>
</div>
```

To learn more about handlebars please follow the link below:<br>
<a href="https://handlebarsjs.com/">Handlebars</a>

## Handlebar atoms, molecules, and components

Handlebars atoms, molecules and components are placed under ``templates-hbs`` folder.

## Compiling handlebars

Handlebar templates are pre-compiled by webpack using ``handlebars-loader``. In front-end code (class files) you can access handlebar templates as follows:

```js
class MyComponent {
    constructor({ templates }) {
        this.templates = templates;
    }
    myMethod() {
        $(selector).html(this.templates.myTemplateFileName(/* Pass JSON data here */));
    }
}
```

There is even a better way(s) to access handlebars template file using ``render`` module.<br>

If you are rendering the template file you can use ``render.fn``:

```js
import { render } from '../../../scripts/utils/render';
class MyComponent {
    myMethod() {
        render.fn({
            template: 'myTemplateFileName',
            data: { /* JSON data */ },
            target: selector
        });
    }
}
```

If you simply want to get hbs template HTML, you can use ``render.get``.

```js
import { render } from '../../../scripts/utils/render';
class MyComponent {
    myMethod() {
        $(selector).html(render.get('myTemplateFileName')(/* JSON data */));
    }
}
```

A detailed documentation for ``render`` module is available in HANDLEBARS.md.

# JavaScript unit testing

For JS unit testing we use ``Mocha``, ``Chai`` and ``Sinon``. ``Karma`` (task runner) is used for running test suites. ``Mocha`` is the unit testing framework where as ``Chai`` is an assertion library. ``Sinon`` is used for spying and stubbing methods to change their behavior according to our testing requirements.

## Unit test "spec" file

The unit test file is created alongside component JS file using ``createComponent`` command. An example of unit test file is shown below:

### MyComponent.spec.js

```js
import MyComponent from './MyComponent';

describe('MyComponent', function () {
    before(function () {
        this.comp = new MyComponent({ el: document.body });
        sinon.spy(this.comp, 'init');
        this.comp.init(); // Initialize component
    });
    it('should initialize on first load', function () {
        expect(this.comp.init.called).to.be.true;
    }); // Test case
    ...
});
```

To learn more about ``Mocha``, ``Chai`` and ``Sinon`` please refer to the links below:<br>
<a href="https://mochajs.org/">Mocha</a>
<a href="https://www.chaijs.com/api/assert/">Chai</a>
<a href="https://sinonjs.org/">Sinon</a>
