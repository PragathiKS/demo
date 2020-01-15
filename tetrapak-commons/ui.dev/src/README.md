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

As a best practice a web page shouldn't load more than 3 JavaScript bundles on initial page load. If the script is lazy loaded due to some user action (e.g. click), we can have more chunks. However, since the framework generates independent chunks for each module, we might end up loading more than 3 bundles on initial page load. To avoid this, we should group these independent chunks into a named bundle. By default ``vendor`` and ``global`` bundles are loaded on page. The third bundle can be page specific which can be loaded as a separate chunk using Webpack runtime. To create page specific bundles we can use Webpack's ``cacheGroups`` configuration. Accelerator has a simple way to define these cache groups. This configuration is placed in ``config.js`` file and can be find under ``webpack > cacheGroups``. An example of cache group is shown below:<br>

```js
...
"webpack": {
    "cacheGroups": {
        ...
        "homepage": {
            "testMultiple": true, // This is equivalent to "test": fn() in webpack.
                                  // Accelerator replaces "textMultiple" with a built-in path matcher.
                                  // These paths are defined in "componentGroup" to allow user to define multiple paths.
            "name": "homepage", // Chunk name
            "minSize": 0, // Minimum size of chunk
            "chunks": "all" // Creates chunk for both static and dynamic imports
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

### Where to import SCSS partials?

By default component SCSS files are defined as partials. These partials are not auto imported at the time of creation. We need to add these imports manually in their respective ``atom``, ``molecule`` and ``component`` files placed under ``source > styles > global > default > en``.<br>
To import partials from "Commons" project, we can use ``~tpCommon/..`` alias.

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

To generate icon fonts we use ``grunt-webfont`` plugin. Configuring grunt webfont is easy.

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
<b>Disclaimer</b>: Grunt webfont plugin fails to generate font file hinting in windows machine. We are in process of replacing this tool with a webpack based plugin which will be added in next version of Accelerator.

# Creating and calling handlerbar templates

Accelerator uses handlerbar templates for client side rendering. Handlebar templates are created under ``hbs-templates`` folder in same atomic style hierarchy. To learn more abour handlerbars please refer to the link below:<br>
<a href="https://handlebarsjs.com/">Handlerbars JS</a>.

Calling handlerbar templates is very easy. We have implemented a custom utility script called ``render.js``. It automatically resolve templates using just the their file names, and provides with lifecycle methods to render ``hbs`` components.

## Getting handlebar templates

```js
import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
class HelloWorld {
    ...
    init() {
        const helloWorldTemplate = render.get('helloWorldTemplate');
        const htmlText = helloWorldTemplate({ ... }); // Pass data
        $('.target').html(htmlText);
    }
}
```

## Rendering handlebar templates

```js
import { render } from '../../../scripts/utils/render';
class HelloWorld {
    ...
    init() {
        render.fn({
            template: 'helloWorldTemplate',
            target: '.target',
            data: { ... },
            beforeRender(data) {
                // Called before any actual rendering takes place
                // Best place to perform any pre-render data modifications
            }
        }, function afterRender(data) {
            // Called after template has successfully rendered
            // Best place to perform post-render clean-ups
        });
    }
}
```

Render module can also be used for making AJAX calls.

```js
import { methods } from '../../../scripts/utils/constants';
import { render } from '../../../scripts/utils/render';
class HelloWorld {
    ...
    init() {
        render.fn({
            template: 'helloWorldTemplate',
            target: '.target',
            url: {
                path: '/path/to/url',
                data: { ... } // URL data
            },
            ajaxConfig: {
                method: methods.POST,
                beforeSend() {
                    // jQuery AJAX beforeSend method
                }
            }
            beforeRender(data) {
                // Called before any actual rendering takes place
                // Best place to perform any pre-render data modifications
            }
        }, function afterRender(data) {
            // Called after template has successfully rendered
            // Best place to perform post-render clean-ups
        });
    }
}
```

For in-depth details on how ``render`` works, refer to <b>HANDLEBARS.md</b> file.

# JavaScript unit testing

Front-end JavaScript unit testing framework comprises of ``Mocha``, ``Chai``, ``Sinon`` and ``Karma``. Karma (task runner) is used for running test suites. ``Mocha`` is the unit testing framework where as ``Chai`` is an assertion library. ``Sinon`` is a mocking library used for spying and stubbing asychronous methods.

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
