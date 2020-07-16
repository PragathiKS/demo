import { trackAnalytics } from '../../../scripts/utils/analytics';

export const subscriptionAnalytics = function (formName, formField, eventType, trackingKey, errorObj=[], Step) {
  const formObj = {
    formName: formName,
    formStep: Step,
    formField: formField
  };

  if(errorObj.length > 0){
    formObj.formError = errorObj;
  }

  const eventObj = {
    eventType: eventType,
    event: 'Newsletter'
  };
  trackAnalytics(formObj, 'form', trackingKey, undefined, false, eventObj);
};
