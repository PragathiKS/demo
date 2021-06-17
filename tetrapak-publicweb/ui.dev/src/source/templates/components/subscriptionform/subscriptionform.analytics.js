import { trackAnalytics } from '../../../scripts/utils/analytics';

export const subscriptionAnalytics = function (formName, dataObj, eventType, trackingKey, formType, errorObj=[]) {
  const formObj = {
    formName: formName,
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
    event: 'newsletter subscription form'
  };
  trackAnalytics(formObj, 'form', trackingKey, undefined, false, eventObj);
};
