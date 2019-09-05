# Installation

```sh
npm install
npm install -g grunt-cli
```

# Build

Prototype deployment for local development
```sh
npm run buildStart
```

With watch mode
```sh
npm run buildWatch
```

Unoptimized build for dev environment
```sh
npm run buildDev
```

Optimized build for test, staging and production environment
```sh
npm run build
```

# Accelerator

This application uses accelerator version 2 (which combines Webpack and Grunt). Accelerator framework is based on decoupled front-end architecture.<br>
For more details of decoupled architecture please follow the link below:<br>
<a href="https://tools.publicis.sapient.com/confluence/download/attachments/797481010/Accelerator%20FE%20Framework.pptx?version=1&modificationDate=1549614956000&api=v2">Accelerator FE Framework</a> [Please contact Sachin Singh (sacsingh2) if the link doesn't work]<br>
We use ES6 modules as part of this version. The framework is designed in a way that each module is treated as independent chunk (unless the chunk is bundled to a cache group). For more details on chunks and cache groups please refer to this link: <a href="https://webpack.js.org/guides/code-splitting/">Code Splitting</a>. Therefore, keep a tab on how many output chunk files you are generating as part of the build. Later we will learn how to create cache groups.<br><br>

# Creating a component

Creating a component is easy. Navigate to ``ui.dev > src`` of your current AEM project and type ``npm run createComponent``. You will be asked to enter a component name. Make sure to use camel case for the name. The script will take care of rest of the naming conventions. The script generates following project files:<br>
```
_<componentName>.scss
ComponentName.js
ComponentName.spec.js
componentname-template.html
ux-model.json
ux-preview.hbs
```

These files are placed under ``ui.dev > src > source > templates > components``.<br>
The script places some initial boilderplate code which can be easily compiled and tested. We use class based JavaScript component which means each instance of this component in AEM will have it's own copy of JS code to work with. This allows better error handling compared to compared to object based components in Accelerator 1. The class based component is shown below:<br>

```js
import $ from 'jquery';

class HelloWorld {
    constructor({ el }) {
        this.root = $(el);
    }
    init() {
        // Write your code here
    }
}

export default HelloWorld;
```

We can still create object based components like the old way and it still works.<br>

```js
export default {
    init() {
        // Write your code here
    }
}
```

To call this component, we use ``data-module`` attribute in our HTML file. Accelerator uses class file name to identify which module needs to be loaded. The framework will automatically fetch appropriate chunks after reading available modules on page. This way we can control number of JS files required for that given page. As a standard practice we should have <b>class name same as file name</b>. However, it doesn't matter if you change the name of class.

```html
...
<div class="tp-hello-world" data-module="HelloWorld">...</div>
```

## Bundling chunks

As a best practice a web page shouldn't load more than 3 JavaScript bundles on initial page load. If the script is lazy loaded due to some user action (e.g. click), we can have a fourth chunk. However, since the framework generates independent chunks for each an every module, we might end up loading more than 3 bundles on initial page load. To avoid this, we can group these independent chunks into a single bundle. By default ``vendor`` and ``global`` bundles are always loaded on page. The third bundle can be page specific. To create page specific bundles we can use Webpack's ``cacheGroups`` concept. Accelerator has a simple way to define cache groups. The configuration is placed in ``config.js`` file under ``webpack > cacheGroups``. You can add a new cache group similar to one shown below:<br>

```js
...
"webpack": {
    "cacheGroups": {
        ...
        "homepage": {
            "testMultiple": true, // This is equivalent to "test": fn() in webpack. In accelerator we don't need to define this function.
                                  // Accelerator uses a built-in function that matches individual component paths defined under "componentGroups"
                                  // configuration below. It makes it easier to bundle components using component path.
            "name": "homepage",
            "minSize": 0,
            "chunks": "all"
        }
        ...
    },
    "componentGroups": {
        ...
        "homepage": [
            "/source/templates/components/helloworld/"
        ]
    }
}
```

# Atomic design

We have structured components following atomic design principles. Each component is broken into smallest individual unit possible known as atom. An example of an atom can be an ``input`` field. Atoms are combined together to form ``molecules``. Multiple molecules are then combined to form a ``component`` or (``organism``).

<b>Atom</b>
```html
<input type="text" />
```

<b>Molecule</b>
```html
<div class="calendar-input-group">
    <input type="text" />
    <button class="date-picker">
        <span class="sr-only">Select date</span>
        <i class="icon-calendar icon"></i>
    </div>
</div>
```

<b>Component (Organism)</b>
```html
<form action="/" method="POST" data-module="DateSelectorForm">
    ...
    <div class="calendar-input-group">
        <input type="text" />
        <button class="date-picker">
            <span class="sr-only">Select date</span>
            <i class="icon-calendar icon"></i>
        </div>
    </div>
    ...
</form>
```

Each atom and molecule can have their own SCSS and HTML implementation. JavaScript however is placed only at component level for simplicity.

## Shortcuts for creating atoms and molecules

Atoms and molecules can be created easily using ``createAtom`` and ``createMolecule`` scripts.<br>

```sh
npm run createAtom
npm run createMolecule
```

<b>Please note that</b> for HTL atoms and molecules, script adds a ``-template`` suffix to it.

# CSS Framework

Accelerator CSS framework uses SASS pre-processor and Bootstrap 4 Grid System modified as below:

### Breakpoints (as per guidelines):
xs: ``0px - 575px``
sm: ``576px - 767px``
md: ``768px - 1023px``
lg: ``1024px - 1199px``
xl: ``1200px`` and above

### Mobile and Desktop views
Mobile: ``&lt; 1024px``
Desktop: ``&gt;= 1024px``

### Breakpoint variables (tetrapak-commons)
``$mobile``: &lt; 576px
``$mobile-large``: &lt; 1024px
``$desktop``: &gt;=1024px
``$desktop-large``: &gt;=1200px
``$mobile-landscape``: Same as ``$mobile`` with orientation as ``landscape``
``$mobile-large-landscape``: Same as ``$mobile-large``

These variable are defined in "Commons" project: ``tetrapak-commons > ui.dev > src > source > styles > global > common > _media.scss``.

## Mobile first approach

To use mobile first approach you must write your code in format below:<br>

```scss
.my-class {
    /* Mobile CSS */
    @media #{$desktop} {
        /* Desktop CSS overrides if required */
    }
}
```

## Base CSS and component max-width

The base CSS is been implemented in "Commons" project. The max width of any individual component (except for header, footer and side navigation) is ``1440px``. To place your component within ``1440px`` column you should use ``tp-container`` wrapping div where ``tp-container`` is the class.

The base CSS is implemented in ``tetrapak-commons > ui.dev > src > source > styles > global > common > _global.scss``.

## Fonts

For English language we are using ``Avenir`` font and it's variants. The font family definitions and variables can be found in ``tetrapak-commons > ui.dev > src > source > styles > global > common > _fonts.scss`` and ``tetrapak-commons > ui.dev > src > source > styles > global > common > _typography.scss``.

## Using REM

Accelerator defines custom mixins to convert pixel values to rem.<br>

```scss
.my-class {
    @include margin-top(10px);
    @include padding(10px 20px 0 0);
    @include font-size(12px);
    @include line-height(24px, 12px); // Unitless
    @include border-radius(3px);
}
```

In cases where mixins can't be used, we use ``convert-to-rem`` function.

```scss
.my-class {
    width: calc(100% - #{convert-to-rem(20px)});
}
```

## BEM guidelines and prefixes

BEM stands for Block Element Modifier. BEM allows us to control specificity, hence we use it as a standard in our CSS code. Let's understand how BEM works using a simple example below:

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

To read more about BEM please follow the link below:<br>
<a href="http://getbem.com/">Block Element Modifier</a>

### Class Prefixes

To differentiate between atoms, molecules and organism (components) we are using following prefixes in our CSS classes:<br>
Atoms: ``tpatom-*``
Molecules: ``tpmol-*``
Organism (Component): ``tp-*``
JS classes: ``js-*``
<br>
As a standard we separate JS classes from style classes to make debugging a lot more easier.

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
