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

<b>Note:</b> By default ``data-module`` will load a separate chunk for your component. We need to add the component to bundled chunk. To learn more about it, refer to the <b>bundling</b> section of this page.

This read me is still in progress...
