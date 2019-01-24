# Installation

```sh
npm install
npm install -g grunt-cli
```

# About

Clickdummy uses the power of <b>Webpack</b> and <b>Grunt</b> both. Webpack is used for compiling JS and SCSS. Grunt is used for deploying the changes to AEM server.

# Creating components

To create component run ``npm run createComponent``. Components are created under ``source/templates/components`` folder.<br><br>
For JavaScript we use ES6 modular approach to create components. Each component is defined as a class with a mandatory init method.

```js
class MyComponent {
    init() {
        // Initializing component
    }
}
```

To execute ``MyComponent`` created above, you need to define a ``data-module`` attribute inside your HTL template file.

```html
<div class="my-component" data-module="MyComponent">My Component renders here</div>
```

Please note that ``data-module`` should be same as your class name.<br><br>

By default ``data-module`` will load a separate chunk for your component at runtime. Yes! You guessed it right. We don't need to write a script tag for each an every bundle we create. This is good in scenarios where we need to lazy load chunks based on some condition.<br><br>

In route based chunking we need to add the component to a bundle to minimize network requests. To learn more about it, refer to the <b>bundling</b> section of this page.

# Bundling

In ``config.json`` please refer to the webpack section. There we provide the configuration for webpack. Within this section you will find a configuration for ``cacheGroups``. Cache groups are used by webpack's ``splitChunks`` configuration.<br>
A bundle can be configured in two ways:<br><br>
1. Using regex:<br><br>
We need to provide a test parameter, bundle name, minimum size and chunking logic (all, initial, or async). Please refer to <a href="https://webpack.js.org/plugins/split-chunks-plugin/">Webpack</a>'s official documentation for more details.<br><br>
2. Using multiple file names or paths:<br><br>
This is in addition to option 1. We can enable multiple files pattern matching by using ``testMultiple: true`` configuration. This is our internal implementation and not used by webpack. With ``testMultiple: true`` the test option will not work. In fact we need provide a list of component paths. To configure that, a separate section has been created called ``componentGroups``. Here against each chunk name we provide a list of components (paths) which we need to bundle into a single output file.

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
<br>
```
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
<div class="component js-component">...</div>
```
<br><br>
3. There should not be more than 3 levels of nesting of classes in our sass code. Following example shows nesting of more than 3 levels which is wrong:<br>
```
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
We have enabled linting rules for both es6 and sass. The build does not proceed further if a linting error is detected.

# Pa11y and Accessibility
<In progress>
