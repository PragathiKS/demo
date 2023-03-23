

import $ from 'jquery';
import { trackAnalytics } from '../utils/analytics';
import { templates } from '../utils/templates';
import LZStorage from 'lzstorage';


// Initialize storage utility
export const storageUtil = new LZStorage();
export const strCompressed = new LZStorage({
  compression: true
});


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

export const getLinkClickAnalytics = (e, parentTitle, componentName, linkClass, redirect = true, dataObj = {}) => {
  const $target = $(e.target);
  const $this = $target.closest(linkClass);
  const downloadtype = $this.data('download-type');
  let trackingKey = 'linkClick';
  let linkParentTitle = ('linkParentTitle' in dataObj) ? (dataObj.linkParentTitle || '') : `${$this.data('anchor-type')}_${$this.data(
    parentTitle
  )}`;
  let linkSection = dataObj.linkSection || $this.data('link-section');
  const linkName = dataObj.linkName || $this.data('link-name');
  const linkType = dataObj.linkType || ($this.attr('target') === '_blank' ? 'external' : 'internal');

  const trackingObj = {};
  const eventObject = {};
  let eventType = 'linkClick';

  if (downloadtype === 'download') {
    trackingObj['dwnDocName'] = $this.data('asset-name');
    linkSection = `${$this.data('anchor-type')}_Download`;
    linkParentTitle = `${componentName}_${$this.data('anchor-type')}_Download_${getDocType(
      $this.attr('href')
    )}_${$this.data(parentTitle)}`;
    trackingObj['eventType'] = 'download';
    trackingObj['dwnType'] = 'ungated';
    trackingKey = 'downloadClick';
    eventType = 'downloadClick';
  }

  trackingObj['linkSection'] = linkSection;
  trackingObj['linkName'] = linkName;
  trackingObj['linkParentTitle'] = linkParentTitle;
  trackingObj['linkType'] = linkType;

  eventObject['event'] = componentName;
  eventObject['eventType'] = eventType;

  trackAnalytics(
    trackingObj,
    'linkClick',
    trackingKey,
    undefined,
    false,
    eventObject
  );

  if (redirect) {
    setTimeout(function () {
      if (linkType === 'internal') {
        if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224) {
          window.open($this.attr('href'), '_blank');
        }
        else {
          window.open($this.attr('href'), '_self');
        }
      }
      else {
        window.open($this.attr('href'), $this.attr('target'));
      }
    }, 500);
  }
};

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