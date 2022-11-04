/**
 * Utility to detect browser
 * @author Sachin Singh
 * @date 16-02-2019
 */

const userAgent = window.navigator.userAgent;

const UA = {
  hasWebkit: (/webkit/i).test(userAgent),
  hasChrome: (/chrome/i).test(userAgent),
  hasSafari: (/safari/i).test(userAgent),
  hasMozilla: (/mozilla/i).test(userAgent),
  hasFirefox: (/firefox/i).test(userAgent),
  hasMSIE: (/msie/i).test(userAgent),
  hasTrident: (/trident/i).test(userAgent),
  hasEdge: (/edge/i).test(userAgent),
  hasOpera: (/OPR/).test(userAgent)
};

/**
 * Returns true if current browser is webkit
 */
export const isWebkit = () => UA.hasWebkit;

/**
 * Returns true if current browser is chrome
 */
export const isChrome = () => (UA.hasWebkit && UA.hasChrome);

/**
 * Returns true if current browser is safari
 */
export const isSafari = () => (UA.hasWebkit && UA.hasSafari && !UA.hasChrome);

/**
 * Returns true if current browser is edge
 */
export const isEdge = () => (UA.hasWebkit && UA.hasEdge);

/**
 * Returns true if current browser is firefox
 */
export const isFirefox = () => (UA.hasMozilla && UA.hasFirefox);

/**
 * Returns true if current browser is internet explorer
 */
export const isIE = () => (UA.hasMSIE || UA.hasTrident || UA.isEdge);

/**
 * Returns true if current browser is opera
 */
export const isOpera = () => (UA.hasWebkit && UA.hasOpera);
