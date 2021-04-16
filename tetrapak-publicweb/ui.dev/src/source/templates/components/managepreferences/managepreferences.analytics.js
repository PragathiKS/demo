import { trackAnalytics } from '../../../scripts/utils/analytics';

export const managePreferencesAnalytics = function (formName, dataObj, eventType, trackingKey, Step, formType, errorObj=[]) {
  const formObj = {
    formName: formName,
    formStep: Step,
    formType: formType,
    formField: []
  };

  if(Object.keys(dataObj).length > 0) {
    Object.keys(dataObj).forEach(i => {
      if(Array.isArray(dataObj[i])){
        dataObj[i].forEach((obj) => {
          Object.keys(obj).forEach((j) => {
            formObj.formField.push({
              formFieldName: j,
              formFieldValue: obj[j]
            });
          });
        });
      }
      else if(dataObj[i]) {
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
    event: 'Manage Your Preferences'
  };
  trackAnalytics(formObj, 'form', trackingKey, undefined, false, eventObj);
};
