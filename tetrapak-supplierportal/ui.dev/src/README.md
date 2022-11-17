# Install

```sh
npm install
```

# Create a component

```sh
npm run createComponent
```

# Create an atom

```sh
npm run createAtom
```

# Create molecule

```sh
npm run createMolecule
```

# Run local dev build

```sh
npm run buildStart
```

With watch

```sh
npm run buildWatch
```

# Production build commands

Unminified (used in DEV environment for debugging)
```sh
npm run buildDev
```

Minified (Used in QA, Stage and Production)
```sh
npm run build
```

# Test commands

```sh
npm run test
```

# SASS files inclusion

SASS files are not imported automatically using CLI. Sass imports are written under ``source > styles > global``. Please find an appropriate file to import sass file.

# Naming conventions

## JavaScript

1. File name should start with capital<br>
2. Class name is same as file name (CLI takes care of naming convention in most cases)<br>
3. Variable and function(method) names should follow pascal case convention<br>
4. Selector variable names should start with ``$`` (e.g. ``const $target = $('.target');``)<br>

## SCSS

1. SCSS should follow ``BEM`` (Block element modifier) convention<br>
2. Atom classes should start with ``tpatom-`` prefix<br>
3. Molecule classes should start with ``tpmol-`` prefix<br>
4. Component classes should start with ``tp-`` prefix<br>

<br>Example of BEM structuring:

```html
<div class="tp-teaser">
    <div class="tp-teaser__header tpmol-header">
        This is teaser header
        <button class="tpatom-btn tp-teaser__header__btn">Edit</button>
    </div>
</div>
```

<br>SCSS example:

```scss
.tp-teaser {
    &__header {
        ...
        &__btn {
            ...
        }
    }
}
```

Each molecule and atom should accept an additional class parameter to include component level classes. For example button atom above has it's own class ``tpatom-btn`` and also has component specific class ``tp-teaser__header__btn``. Atom class handles the styling of atom at atomic level where as component level class(es) helps to adjust styles specific to that component.<br>

BEM guidelines allow element nesting without increasing specificity. For more details on BEM please follow the link below:<br>

http://getbem.com/introduction/

## HTL files

HTL files should follow lowercase naming convention (without spaces, underscore or hiphens). Hbs files can (and should) follow camel case convention (This is because ``hbs`` files are bundled as JS modules and JavaScript follows camel case for function and variable names).

# Initializing JavaScript components

JavaScript components are initialized using ``data-module`` attribute in your HTML files. The ``data-module`` references is included by default if you create a component using CLI. Please note that ``data-module`` reference will not work in client side rendered templates (such as handlebars).

# Prefer using CLI

Front-end framework includes CLI commands to create components, atoms and molecules (commands listed above). CLI takes care of creating necessary component files (including a base boilerplate) and add them to appropriate bundles. Prefer using CLI to avoid any configuration and bundling related problems later on.

# Contribution

This framework uses a lot of tools (created in-house or from third party). No program is perfect, and it's our duty to contribute towards these tools and make them better and ready for the future. For more queries or help please reach out to:

## Contributors

<a href="mailto:vandana.gupta@publicissapient.com">vandana.gupta@publicissapient.com</a><br>
<a href="mailto:sachin.singh1@publicissapient.com">sachin.singh1@publicissapient.com</a><br>
Add yourself to this list if you are a contributor.
