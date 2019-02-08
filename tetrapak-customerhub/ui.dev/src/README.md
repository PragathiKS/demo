# Installation

```sh
npm install
npm install -g grunt-cli
```

# Build

Development environment
```sh
npm run buildDev
```

Production
```sh
npm run build
```

# About

UI.Dev uses the power of <b>Webpack</b> and <b>Grunt</b> both. Webpack is used for compiling JS and SCSS. Grunt is used for deploying the changes to AEM server.

# Creating components/molecules/atoms

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

<br><br>
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

<br>
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
