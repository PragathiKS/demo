import { trackAnalytics } from '../../../scripts/utils/analytics';

export const trackFormStart = function (formName) {
  const formObj = {
    formName: formName
  };
  const eventObj = {
    eventType: 'formstart',
    event: 'add new equipment form'
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
    event: 'add new equipment form'
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
    event: 'add new equipment form'
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};

export const trackLinkClick = function (formName, linkName) {
  const formObj = {
    linkType: 'internal',
    linkSection: 'button click',
    linkParentTitle: formName,
    linkName: linkName
  };
  const eventObj = {
    eventType: 'linkclick',
    event: 'add new equipment form'
  };
  trackAnalytics(formObj, 'linkClick', 'linkClick', undefined, false, eventObj);
};
