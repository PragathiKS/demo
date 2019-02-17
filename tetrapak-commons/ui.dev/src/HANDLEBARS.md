# About Handlebars

Handlebars is a client and server side templating language. In current context we use it for client side rendering.

# Syntax

A typical handlebar template looks like below:

### HelloWorld.hbs

```html
{{#if helloWorldText}}
    <div class="atom-helloworld">
        {{helloWorldText}}
    </div>
{{/if}}
```

# Similarity with Moustache

Handlebars is based on Moustache templates which means that the syntax followed by handlebars is very similar to Moustache. The only difference between the Handlebars and Moustache is handlebars compiles much faster compared to moustache, and provides rich support for built-in and custom helpers.

# Compilation

Handlebar templates are pre-compiled by ``webpack`` in form of JavaScript functions. You can access these templates in JavaScript code using one of the following syntax:

### Approach 1
```js
import $ from 'jquery';
class HelloWorld {
    constructor({ templates }) {
        this.templates = templates;
    }
    init() {
        $('.app').html(this.templates.HelloWorld({ helloWorldText: 'Hello World!' }));
    }
}
```

### Approach 2 (Recommended)
```js
import { render } from '../../../scripts/utils/render';
class HelloWorld {
    init() {
        render.fn({
            template: 'HelloWorld',
            data: { helloWorldText: 'Hello World!' },
            target: '.app'
        });
    }
}
```

# Render.js

Render JS library is designed to handle complex rendering logic on front-end. It supports ``handlebar`` templates and ``AJAX``.

# How render js works?

### Calling template with data

```js
render.fn({
    template: '<template name>',
    data: { ... }
});
```

This will append an empty div inside ``body`` or find a div with class ``js-render-content`` (if it exists) and render the template file. If template file doesn't exist, it will render JSON data.

### Provide a target

```js
render.fn({
    // ...
    target: '.app'
});
```

A target should be a valid selector. We can also pass a jQuery selector. Render js will search for target element in the page and render the template. If target element is missing, it will append an empty element inside body and render the template.

### Prevent template from rendering if target is missing

You can set ``render`` flag to ``true`` or ``false`` by checking the length of target element:

```js
render.fn({
    // ...
    target: $('.app'),
    render: $('.app').length
});
```

### Render template after successful AJAX call

```js
render.fn({
    template: 'HelloWorld',
    url: 'https://...', // API URL
    ajaxConfig: {
        method: 'GET'
    }, // jQuery AJAX configuration
    target: '.app'
});
```

Render JS also handles asynchronous calls and renders template once the AJAX call has completed.

### Transform raw data from API to consumable format

If you wish to transform data from API into a consumable format, you can use ``beforeRender`` callback.

```js
render.fn({
    // ...
    beforeRender(data) {
        this.data = data.docs.forEach(item => (item.currentDate = Date.now()));
    }
});
```

This is a good place to resolve internationalization keys. Render JS will take care of delivering transformed JSON to the template file.

### Do something after rendering has completed

```js
render.fn(
    { /* ... */ },
    function (data) {
        // Callback function executed post rendering
    }
);
```

This is a good place to resolve lazy loading of images.

### Rendering &lt;code&gt; block

Render JS can resolve a JSON ``<code>`` block present in page and render the component.

```js
render.fn({
    codeBlock: '.js-helloworld-json',
    template: 'HelloWorld'
});
```

If target is not specified, render js will place an empty div next to ``<code>`` block and render the template file.

<b>Please note</b> that ``<code>`` block is a data source and render js gives preference to it over ``data`` option.
