// Vendor imports
import 'core-js/features/promise';
import 'core-js/features/array/includes';
import $ from 'jquery';
// Local imports
import bundleImport from '../bundle/imports';
import bundleImporter from '../bundle/importer';
import { templates } from '../utils/templates';
// CSS imports
import '../../styles/vendor.scss';
import '../../styles/global/core/core.scss';

$(function () {
  const $componentReference = $('[data-module]');
  const components = [];
  // Discover components
  $componentReference.each(function () {
    const componentList = $(this).data('module').split(',');
    componentList.forEach(function (componentName) {
      if (!components.includes(componentName)) {
        components.push(componentName);
      }
    });
  });
  // Fetch component bundles
  components.forEach(component => {
    // Import bundle
    bundleImport(component, function (args) {
      args.templates = templates;
      bundleImporter(args);
    });
  });
});
