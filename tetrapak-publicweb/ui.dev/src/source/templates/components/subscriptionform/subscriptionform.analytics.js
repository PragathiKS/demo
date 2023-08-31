import { trackAnalytics } from '../../../scripts/utils/analytics';

export const subscriptionAnalytics = function (formName, dataObj, eventType, trackingKey, errorObj=[], countryData) {
  const formObj = {
    formName: formName,
    formField: []
  };
  if(countryData.isChina) {
    formObj['formIdChina'] = countryData.formHandler;
  } else {
    formObj['formIdGlobal'] = countryData.formHandler;
  }
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
