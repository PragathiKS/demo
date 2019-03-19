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
