/**
 * Fire analytics
 */
export const trackAnalytics = (objectData, objectKey) => {
  window.digitalData = window.digitalData || {};
  window.digitalData[objectKey.toLowerCase()] = objectData;

  if (window._satellite) {
    window._satellite.track(objectKey);
  }
};
