import LZStorage from 'lzstorage';
import $ from 'jquery';
import { IS_MOBILE_REGEX, IS_TABLET_REGEX } from '../utils/constants';
import { $global } from '../utils/commonSelectors';
import { templates } from '../utils/templates';
import { trackAnalytics } from '../utils/analytics';
import { isExternal, isDownloable } from '../utils/updateLink';

const currentUserAgent = window.navigator.userAgent;

// Initialize storage utility
export const storageUtil = new LZStorage();

const { getCookie } = storageUtil;

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
export const isMobileMode = () => ($(window).outerWidth() < 768);

/**
 * Checks if current screen mode is tablet
 */
export const isTabletMode = () => {
  const outerWidth = $(window).outerWidth();
  return outerWidth >= 768 && outerWidth < 992;
};

/**
 * Checks if current screen mode is mobile or tablet
 */
export const isMobileOrTabletMode = () => (isMobileMode() || isTabletMode());

/**
 * Checks if current screen mode is desktop
 */
export const isDesktopMode = () => !isMobileOrTabletMode();

/**
 * Checks if current screen mode is tablet or desktop
 */
export const isTabletOrDesktopMode = () => (isTabletMode() || isDesktopMode());

/**
 * Checks if current screen mode is mobile
 */
export const isMobile1024Mode = () => ($(window).outerWidth() < 1024);

/**
 * Checks if current screen mode is desktop
 */
export const isDesktop1024Mode = () => !isMobile1024Mode();

/**
 * Checks if author mode is enabled
 */
export const isAuthorMode = () => {
  const wcmmode = getCookie('wcmmode');
  return ['edit', 'preview', 'design'].includes(wcmmode);
};

/**
 * Checks if given param is callable
 * @param {*} param Any param
 */
export const isCallable = (param) => (typeof param === 'function');

/**
 * Scrolls the page to a particular element
 * @param {function} callback Callback function
 * @param {string|object} selector Selector or element
 * @param {number} duration Duration in number
 */
export const scrollToElement = (callback, selector = document.body, duration = 500) => {
  let executed = false;
  let stickyViewHeight = 0;
  if($('tp-pw-header__container').length > 0) {
    stickyViewHeight = $('.tp-pw-header__container').outerHeight();
  }
  if($('.js-tp-pw-headerv2').length > 0) {
    stickyViewHeight = stickyViewHeight + $('.js-tp-pw-headerv2').outerHeight();
  }
  if($('.sticky-section-menu').length > 0){
    stickyViewHeight = stickyViewHeight + $('.sticky-section-menu .js-pw-navigation').outerHeight();
  }
  if($('.sticky-anchor-menu').length > 0){
    stickyViewHeight = stickyViewHeight + $('.sticky-anchor-menu').outerHeight();
  }

  $global.animate(
    {
      scrollTop: $(selector).offset().top - parseInt(stickyViewHeight,10)
    },
    {
      duration,
      complete() {
        if (!executed) {
          executed = true;
          if (isCallable(callback)) {
            callback.apply(this, arguments);
          }
        }
      }
    }
  );
};

/**
 * Scrolls the page to given offset location
 * @param {number} offset Offset from top
 * @param {function} callback Callback function
 * @param {number} duration Duration in number
 */
export const scrollToOffset = (offset, callback, duration = 500) => {
  let executed = false;
  $global.animate(
    {
      scrollTop: offset
    },
    {
      duration,
      complete() {
        if (!executed) {
          executed = true;
          if (isCallable(callback)) {
            callback.apply(this, arguments);
          }
        }
      }
    }
  );
};

/**
 * Scrolls the page to top
 * @param {function} callback Callback function
 * @param {number} duration Duration in number
 */
export const scrollToTop = (callback, duration = 500) => {
  scrollToOffset(0, callback, duration);
};

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
  const loading = templates.loading();
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
      var keyRegex = new RegExp(`\\{${  index  }\\}`, 'g');
      queryString = queryString.replace(keyRegex, value);
    });
  } else if (typeof replaceMap === 'object' && replaceMap !== null) {
    Object.keys(replaceMap).forEach(function (key) {
      var keyRegex = new RegExp(`\\{${  key  }\\}`, 'g');
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

export const digitalData = window.digitalData || {};

export const loc = {
  replace(...args) {
    window.location.replace(...args);
  },
  open(...args) {
    window.open(...args);
  }
};

export const getDocType = (url) => {
  const fileList = ['pdf', 'xls', 'xlsx', 'doc', 'docx', 'ppt', 'pttx', 'jpeg', 'png', 'jpg', 'svg'];
  const endPart = url && url.split('/').pop();
  const docType = endPart.split('.').pop();

  if(fileList.includes(docType)){
    return docType;
  }
};

export const addLinkAttr = (linkClass) => {
  $(linkClass).each(function() {
    const thisHref = $(this).attr('href');
    if (thisHref) {
      $(this).attr('target', '_self');
      if (isExternal(thisHref)) {
        $(this).attr('target', '_blank');
      }
      if (isDownloable(thisHref)) {
        $(this).data('download-type', 'download');
        $(this).attr('target', '_blank');
      }
    }
  });
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

export const validateFieldsForTags = (value='') => value.replace(/[`<>]/gi, '');

export const updateQueryStringParameter = (uri, key, value) => {
  var re = new RegExp(`([?&])${  key  }=.*?(&|$)`, 'i');
  var separator = uri.indexOf('?') !== -1 ? '&' : '?';
  if (uri.match(re)) {
    return uri.replace(re, `$1${  key  }=${  value  }$2`);
  }
  else {
    return `${uri + separator + key  }=${  value}`;
  }
};

export const checkActiveOverlay = (activeOverlays) => {
  let isActiveOverlay = false;
  activeOverlays.forEach((overlayClass) => {
    if($(overlayClass).hasClass('show') || $(overlayClass).css('display') !== 'none'){
      isActiveOverlay = true;
    }
  });
  if(!isActiveOverlay) {
    $('body').css('overflow','auto');
  }
};

export const parseQueryString = () => {
  // Use location.search to access query string instead
  const qs = window.location.search.replace('?', '');
  const items = qs.split('&');

  // Consider using reduce to create the data mapping
  return items.reduce((data, item) => {
    const [key, value] = item.split('=');

    // Sometimes a query string can have multiple values
    // for the same key, so to factor that case in, you
    // could collect an array of values for the same key
    if(data[key] !== undefined) {

      // If the value for this key was not previously an
      // array, update it
      if(!Array.isArray(data[key])) {
        data[key] = [ data[key] ];
      }
      data[key].push(value);
    }
    else {
      data[key] = value;
    }
    return data;
  }, {});
};

// Remove Query Params from URL
export const removeParams = (parameter, pageURL) => {
  let url = pageURL ? pageURL : document.location.href;
  const urlparts = url.split('?');
  if (urlparts.length >= 2) {
    const urlBase = urlparts.shift();
    const queryString = urlparts.join('?');
    const prefix = encodeURIComponent(parameter) + '=';
    const pars = queryString.split(/[&;]/g);
    for (let i = pars.length; i-- > 0;) {
      if (pars[i].lastIndexOf(prefix, 0) !== -1) {
        pars.splice(i, 1);
      }
      url = urlBase + '?' + pars.join('&');
    }
  }
  return url;
};

// Capitalize String
export const capitalizeFirstLetter = ([ first, ...rest ]) => {
  return [ first.toUpperCase(), ...rest ].join('');
};