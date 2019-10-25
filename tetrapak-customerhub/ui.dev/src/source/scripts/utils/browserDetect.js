/**
 * Utility to detect browser
 * @author Sachin Singh
 * @date 16-02-2019
 */

const userAgent = window.navigator.userAgent;
const platform = window.navigator.platform;
const maxTouchPoints = window.navigator.maxTouchPoints;

const UA = {
  hasWebkit: (/webkit/i).test(userAgent),
  hasChrome: (/chrome/i).test(userAgent),
  hasChromeIOS: (/crios/i).test(userAgent),
  hasSafari: (/safari/i).test(userAgent),
  hasMozilla: (/mozilla/i).test(userAgent),
  hasFirefox: (/firefox/i).test(userAgent),
  hasMSIE: (/msie/i).test(userAgent),
  hasTrident: (/trident/i).test(userAgent),
  hasEdge: (/edge/i).test(userAgent),
  hasOpera: (/OPR/).test(userAgent),
  hasIphone: (/iphone/i).test(userAgent),
  hasIpad: (/ipad/i).test(userAgent),
  hasHeadless: (/headlesschrome/i).test(userAgent)
};

/**
 * Returns true if current browser is webkit
 */
export function isWebkit() {
  return UA.hasWebkit;
}

/**
 * Returns true if current browser is chrome
 */
export function isChrome() {
  return (UA.hasWebkit && (UA.hasChrome || UA.hasChromeIOS));
}

/**
 * Returns true if current browser is chrome iOS
 */
export function isChromeIOS() {
  return (UA.hasWebkit && UA.hasChromeIOS);
}

/**
 * Returns true if current browser is headless chrome
 */
export function isHeadlessChrome() {
  return UA.hasHeadless;
}

/**
 * Returns true if current platform is ipad OS 13
 */
function isIPadOS() {
  return (platform === 'MacIntel' && maxTouchPoints > 1) || UA.hasIpad;
}

/**
 * Returns true if current platform is iOS
 */
export function isIOS() {
  return (UA.hasIpad || UA.hasIphone || isIPadOS());
}

/**
 * Returns true if current browser is safari
 */
export function isSafari() {
  return (UA.hasWebkit && UA.hasSafari && !(UA.hasChrome || UA.hasChromeIOS));
}

/**
 * Returns true if current browser is edge
 */
export function isEdge() {
  return (UA.hasWebkit && UA.hasEdge);
}

/**
 * Returns true if current browser is firefox
 */
export function isFirefox() {
  return (UA.hasMozilla && UA.hasFirefox);
}

/**
 * Returns true if current browser is internet explorer
 */
export function isIE() {
  return (UA.hasMSIE || UA.hasTrident || UA.isEdge);
}

/**
 * Returns true if current browser is opera
 */
export function isOpera() {
  return (UA.hasWebkit && UA.hasOpera);
}
