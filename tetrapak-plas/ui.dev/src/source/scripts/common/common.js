

import $ from 'jquery';
import { templates } from '../utils/templates';

export const getDocType = (url) => {
  const fileList = ['pdf', 'xls', 'xlsx', 'doc', 'docx', 'ppt', 'pttx', 'jpeg', 'png', 'jpg', 'svg'];
  const endPart = url && url.split('/').pop();
  const docType = endPart.split('.').pop();
  if(fileList.includes(docType)){
    return docType;
  }
};
/**
 * Parse JSON string if it is parsable. Otherwise returns the original string.
 * @param {string} jsonData JSON string
*/
export const parseJson = (data) => {
  let parsedData = data;
  if (typeof data === 'string') {
    try {
      parsedData = JSON.parse(data);
    } catch (e) {
      parsedData = data;
    }
  }
  return parsedData;
};
/**
  * Returns true if current selector is valid
  * @param {string|object|object[]} selector Selector string or object
*/
export const isValidSelector = (selector) => (
  typeof selector === 'string'
    || selector instanceof $
    || selector instanceof Node
    || selector instanceof NodeList
    || selector instanceof HTMLCollection
    || Array.isArray(selector)
);

/**
 * Checks if given param is callable
 * @param {*} param Any param
 */
export const isCallable = (param) => (typeof param === 'function');

/**
* Loader class to automatically insert loading animation through code
*/
export class Loader {
  constructor(loader) {
    this.context = $(loader);
    this.length = this.context.length;
    if (this.length) {
      this.htmlText = this.context[0].outerHTML;
      this[0] = this.context[0];
    }
  }
  target(el) {
    if (this.length) {
      $(el).html(this.context);
    }
    return this;
  }
  appendTo(el) {
    if (this.length) {
      $(el).append(this.context);
    }
    return this;
  }
  prependTo(el) {
    if (this.length) {
      $(el).prepend(this.context);
    }
    return this;
  }
  insertAfter(el) {
    if (this.length) {
      $(el).after(this.context);
    }
    return this;
  }
  insertBefore(el) {
    if (this.length) {
      $(el).before(this.context);
    }
    return this;
  }
  remove() {
    return this.context.remove();
  }
}

/**
 * Returns a loader object with default template
 * @param {object} target Target where loader needs to be placed
 * @param {boolean} appendMode Append mode
 */
export const loader = (target, appendMode) => {
  const loading = templates.loader();
  if (target) {
    $(target)[appendMode ? 'append' : 'html'](loading);
  } else {
    return new Loader(loading);
  }
};