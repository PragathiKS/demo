/**
 * Fire analytics
 */
export const trackAnalytics = (objectData, objectName, objectKey) => {
  window.digitalData = window.digitalData || {};

  if (objectName) {
    objectName = objectName.toLowerCase();
    window.digitalData[objectName] = window.digitalData[objectName] || {};

    if (objectKey) {
      window.digitalData[objectName][objectKey.toLowerCase()] = objectData;

      if (window._satellite) {
        window._satellite.track(objectKey);
      }
    }
  }
};

/**
 * Track multiple parameters at once
 * @param {object} ob Analytics params
 * @param {string} obKey Analytics object key
 * @param {string} trackingKey Tracking key
 */
export const trackParams = (ob, obKey, trackingKey) => {
  window.digitalData = $.extend(window.digitalData, {});
  if (
    !$.isEmptyObject(ob)
    && typeof obKey === 'string'
  ) {
    window.digitalData[obKey] = $.extend(window.digitalData[obKey], ob);
    if (window._satellite) {
      window._satellite.track(trackingKey);
    }
  }
};
