import { trackAnalytics } from '../../../scripts/utils/analytics';

export const trackFormStart = function (formName, equipment) {
  const formObj = {
    formName,
    equipment,
    event: {
      eventType: 'formstart',
      event: 'equipment information updation form'
    }
  };
  trackAnalytics(formObj, 'form', 'form start', undefined, false);
};

export const trackFormStepComplete = function (formName, equipment, formFields) {
  const formObj = {
    formName,
    equipment,
    event: {
      eventType: 'step1complete',
      event: 'equipment information updation form'
    },
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'form complete', undefined, false);
};

export const trackFormComplete = function (formName, equipment, formFields) {
  const formObj = {
    formName,
    equipment,
    event: {
      eventType: 'formcomplete',
      event: 'equipment information updation form'
    },
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'form complete', undefined, false);
};

export const trackFormCancel = function (formName) {
  const formObj = {
    formName,
    event: {
      eventType: 'formcancel',
      event: 'equipment information updation form'
    }
  };
  trackAnalytics(formObj, 'form', 'form cancel', undefined, false);
};

export const trackFormError = function (formName, formErrors) {
  const formObj = {
    formName,
    event: {
      eventType: 'formerror',
      event: 'equipment information updation form'
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
      linkName
    },
    event: {
      eventType: 'linkclick',
      event: 'equipment information updation form'
    }
  };
  trackAnalytics(formObj, 'linkClick', 'linkClicked', undefined, false);
};