import { trackAnalytics } from '../../../scripts/utils/analytics';

export const trackFormStart = function (formName) {
  const formObj = {
    formName: formName,
    event: {
      eventType: 'formstart',
      event: 'add new equipment form'
    }
  };
  trackAnalytics(formObj, 'form', 'form start', undefined, false);
};

export const trackFormComplete = function (formName, formFields) {
  const formObj = {
    formName: formName,
    event: {
      eventType: 'formcomplete',
      event: 'add new equipment form'
    },
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'form complete', undefined, false);
};

export const trackFormError = function (formName, formErrors) {
  const formObj = {
    formName: formName,
    event: {
      eventType: 'formerror',
      event: 'add new equipment form'
    },
    formError: formErrors
  };
  trackAnalytics(formObj, 'form', 'form error', undefined, false);
};

export const trackLinkClick = function (formName, linkName) {
  const formObj = {
    linkClick: {
      linkType: 'internal',
      linkSection: 'button click',
      linkParentTitle: formName,
      linkName: linkName
    },
    event: {
      eventType: 'linkclick',
      event: 'add new equipment form'
    }
  };
  trackAnalytics(formObj, 'linkClick', 'linkClicked', undefined, false);
};
