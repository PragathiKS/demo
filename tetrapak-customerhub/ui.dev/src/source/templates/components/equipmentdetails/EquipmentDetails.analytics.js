import { trackAnalytics } from '../../../scripts/utils/analytics';

export const trackFormStart = function (formName, step, equipment) {
  const eventObj = {
    eventType: 'formstart',
    event: 'Equipment Information Updation Form'
  };
  const formObj = {
    equipment: equipment,
    formName: 'Equipment Information Updation Form',
    formStep: step,
    formType: formName
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormStepComplete = function (formName, step, equipment, formFields) {
  const eventObj = {
    eventType: 'step1complete',
    event: 'Equipment Information Updation Form'
  };
  const formObj = {
    equipment: equipment,
    formName: 'Equipment Information Updation Form',
    formStep: step,
    formType: formName,
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormComplete = function (formName, step, equipment, formFields) {
  const eventObj = {
    eventType: 'formcomplete',
    event: 'Equipment Information Updation Form'
  };
  const formObj = {
    equipment: equipment,
    formName: 'Equipment Information Updation Form',
    formStep: step,
    formType: formName,
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormCancel = function (formName, step, equipment) {
  const eventObj = {
    eventType: 'formcancel',
    event: 'Equipment Information Updation Form'
  };
  const formObj = {
    equipment: equipment,
    formName: 'Equipment Information Updation Form',
    formStep: step,
    formType: formName
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};

export const trackFormError = function (formName, step, equipment, formErrors) {
  const eventObj = {
    eventType: 'formerror',
    event: 'Equipment Information Updation Form'
  };
  const formObj = {
    equipment: equipment,
    formName: 'Equipment Information Updation Form',
    formStep: step,
    formType: formName,
    formError: formErrors
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
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
      eventType: 'linkClick',
      event: 'Equipment Information Updation Form'
    }
  };
  trackAnalytics(formObj, 'linkClick', 'linkClick', undefined, false);
};

export const trackBreadcrumbLinkClick = function (linkName) {
  const eventObj = {
    event: 'breadcrumb',
    eventType: 'linkClick'
  };

  const linkClickObj = {
    linkName: linkName,
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: ''
  };

  trackAnalytics(linkClickObj, 'linkClick', 'breadcrumbClick', undefined, false, eventObj);
};