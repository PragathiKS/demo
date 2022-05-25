import { trackAnalytics } from '../../../scripts/utils/analytics';

export const trackFormStart = function (formName) {
  const formObj = {
    formName: formName
  };
  const eventObj = {
    eventType: 'formstart',
    event: 'PlantMaster COTS Support form'
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormComplete = function (formName, formFields) {
  const formObj = {
    formName: formName,
    formField: formFields
  };
  const eventObj = {
    eventType: 'formcomplete',
    event: 'PlantMaster COTS Support form'
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormError = function (formName, formErrors) {
  const formObj = {
    formName: formName,
    formError: formErrors
  };
  const eventObj = {
    eventType: 'formerror',
    event: 'PlantMaster COTS Support form'
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};
