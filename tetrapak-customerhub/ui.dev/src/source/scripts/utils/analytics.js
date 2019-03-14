/**
 * Fire analytics
 */
export const trackAnalytics = (objectKey) => {
  window._satellite.track(objectKey);
};
