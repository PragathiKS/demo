import { trackAnalytics } from '../../../scripts/utils/analytics';

export const subscriptionAnalytics = function (formName, dataObj, eventType, trackingKey, errorObj=[], Step, formType) {
  const formObj = {
    formName: formName,
    formStep: Step,
    formType: formType,
    formField: []
  };

  if(Object.keys(dataObj).length > 0) {
    Object.keys(dataObj).forEach(i => {
      if(dataObj[i]) {
        formObj.formField.push({
          formFieldName: i,
          formFieldValue: dataObj[i]
        });  
      }
    });
  }

  if(errorObj.length > 0){
    formObj.formError = errorObj;
  }

  const eventObj = {
    eventType: eventType,
    event: 'Newsletter'
  };
  trackAnalytics(formObj, 'form', trackingKey, undefined, false, eventObj);
};
