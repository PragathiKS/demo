import LZStorage from 'lzstorage';
import $ from 'jquery';
import { IS_MOBILE_REGEX, IS_TABLET_REGEX } from '../utils/constants';
import { trackAnalytics } from '../utils/analytics';
import { templates } from '../utils/templates';
import Handlebars from 'handlebars';
import * as money from 'argon-formatter';

const currentUserAgent = window.navigator.userAgent;

// Initialize storage utility
export const storageUtil = new LZStorage();
export const strCompressed = new LZStorage({
  compression: true
});

// Initialize functions for user agent detection
/**
 * Checks if current user agent belongs to mobile
 */
export const isMobile = () => IS_MOBILE_REGEX.test(currentUserAgent);

/**
 * Checks if current user agent belongs to tablet
 */
export const isTablet = () => IS_TABLET_REGEX.test(currentUserAgent);

/**
 * Checks if current user agent belongs to desktop
 */
export const isDesktop = () => !(isMobile() || isTablet());

/**
 * Checks if current screen mode is mobile
 */
export const isMobileMode = () => ($(window).outerWidth() < 1024);

/**
 * Checks if current screen mode is desktop
 */
export const isDesktopMode = () => !isMobileMode();

/**
 * Checks if author mode is enabled
 */
export const isAuthorMode = () => {
  const wcmmode = storageUtil.getCookie('wcmmode');
  return ['edit', 'preview', 'design'].includes(wcmmode);
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

/**
 * Replaces placeholders in query string with values
 * @param {string} queryString Query string
 * @param {any[]|object} replaceMap Replacement list or map
 */
export const resolveQuery = (queryString, replaceMap) => {
  // Replace the replaceable data
  if (Array.isArray(replaceMap)) {
    replaceMap.forEach(function (value, index) {
      var keyRegex = new RegExp(`\\{${index}\\}`, 'g');
      queryString = queryString.replace(keyRegex, value);
    });
  } else if (typeof replaceMap === 'object' && replaceMap !== null) {
    Object.keys(replaceMap).forEach(function (key) {
      var keyRegex = new RegExp(`\\{${key}\\}`, 'g');
      queryString = queryString.replace(keyRegex, replaceMap[key]);
    });
  }
  return queryString;
};

/**
 * Returns the value of i18n key
 * @param {string} key I18n key
 * @param {string[]} replaceList Variable replacement list
 * @param {object} hash Helper hash object
 */
export const getI18n = (key, replaceList, hash) => {
  let variables = [];
  if (!Array.isArray(replaceList)) {
    hash = replaceList;
    replaceList = [];
  }
  if (hash && typeof hash.replaceKeys === 'string') {
    variables = hash.replaceKeys.split(',');
    key = resolveQuery(key, variables);
  }
  if (window.Granite && window.Granite.I18n) {
    return window.Granite.I18n.get(key, replaceList);
  }
  return key;
};

export const getDocType = (url) => {
  const fileList = ['pdf', 'xls', 'xlsx', 'doc', 'docx', 'ppt', 'pttx', 'jpeg', 'png', 'jpg', 'svg'];
  const endPart = url && url.split('/').pop();
  const docType = endPart.split('.').pop();

  if(fileList.includes(docType)){
    return docType;
  }
};

export const getLinkClickAnalytics =(e,parentTitle,componentName,linkClass, redirect=true,dataObj={}) => {
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

  if(redirect) {
    setTimeout(function() {
      if (linkType === 'internal')  {
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
 * Wrapper to throw a generic error
 * @param {string} message Error message
 */
export const throwError = (message) => {
  throw new Error(message);
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
 * Sanitize input value to prevent XSS
 * @param {any} input Input value
 */
export const sanitize = (input) => Handlebars.escapeExpression(input);


/**
 * sort table data
 * @param {object} data data object
 * @param {string[]} keys List of keys
 * @param {dataLink} dataLink Row link
 */
export const tableSort = (data, keys, dataLink, isClickable, rtKeys = []) => {
  const dataObject = {
    row: []
  };

  if (dataLink) {
    dataObject.rowLink = `${dataLink}`;
  }

  dataObject.isClickable = !!dataLink || isClickable;

  keys.forEach((key, index) => {
    const value = data[key];
    dataObject.row[index] = {
      key,
      value,
      isRTE: rtKeys.includes(key)
    };
  });
  return dataObject;
};

/**
 * Adds currency symbol to value
 * @param {string} value Input value
 * @param {string} isoCode ISO currency code
 */
export const resolveCurrency = (value, isoCode) => {
  if (isNaN(+value) || !isoCode) {
    return value; // No transformation
  }
  return money.format(value, {
    code: isoCode
  });
};

/**
 * Checks if current page is iframe
 */
export const isCurrentPageIframe = () => ((window.location !== window.parent.location) && (window.frameElement.getAttribute('id') !== 'ContentFrame'));

/**
 * Checks if current environment is localhost
 */
export const isLocalhost = () => ($('#isDummyLayout').val() === 'true');

/**
 * Returns max safe integer
 */
export const getMaxSafeInteger = () => (Number.MAX_SAFE_INTEGER || 9007199254740991);

/**
 * Checks if key exists in object
 * @param {object} obj Current object
 * @param {string} key Object key
 */
export function hasOwn(obj, key) {
  if (obj && typeof obj === 'object') {
    return Object.prototype.hasOwnProperty.call(obj, key);
  }
  return false;
}