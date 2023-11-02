// Vendor imports
import $ from 'jquery';
// Local imports
import './polyfill';
import bundleImport from '../bundle/imports';
import bundleImporter from '../bundle/importer';
import { templates } from '../utils/templates';
import './corescss';

$(function () {
  const $componentReference = $('[data-module]');
  const components = [];
  // Discover components
  $componentReference.each(function () {
    const componentList = $(this).data('module').split(',').map(componentName => componentName.trim());
    componentList.forEach((componentName) => {
      if (!components.includes(componentName)) {
        components.push({ componentName, el: this });
      }
    });
  });
  // Fetch component bundles
  components.forEach(({ componentName, el }) => {
    // Import bundle
    bundleImport(componentName, function (args) {
      args.templates = templates;
      args.el = el;
      bundleImporter(args);
    });
  });
});
