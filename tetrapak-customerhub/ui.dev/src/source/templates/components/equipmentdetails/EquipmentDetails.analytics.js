import { trackAnalytics } from '../../../scripts/utils/analytics';

export const trackFormStart = function (formName, equipment) {
  const eventObj = {
    eventType: 'formstart',
    event: 'Equipment Information Update'
  };
  const formObj = {
    equipment: equipment,
    formName: formName,
    formStep: 'Step 1',
    formType: 'Equipment Information Update'
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormStepComplete = function (formName, equipment, formFields) {
  const eventObj = {
    eventType: 'step1complete',
    event: 'Equipment Information Update'
  };
  const formObj = {
    equipment: equipment,
    formName: formName,
    formStep: 'Step 1',
    formType: 'Equipment Information Update',
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormComplete = function (formName, equipment, formFields) {
  const eventObj = {
    eventType: 'formcomplete',
    event: 'Equipment Information Update'
  };
  const formObj = {
    equipment: equipment,
    formName: formName,
    formStep: 'Step 1',
    formType: 'Equipment Information Update',
    formField: formFields
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const trackFormCancel = function (formName, equipment) {
  const eventObj = {
    eventType: 'formcancel',
    event: 'Equipment Information Update'
  };
  const formObj = {
    equipment: equipment,
    formName: formName,
    formStep: 'Step 1',
    formType: 'Equipment Information Update'
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};

export const trackFormError = function (formName, equipment, formErrors) {
  const eventObj = {
    eventType: 'formerror',
    event: 'Equipment Information Update'
  };
  const formObj = {
    equipment: equipment,
    formName: formName,
    formStep: 'Step 1',
    formType: 'Equipment Information Update',
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
      linkName
    },
    event: {
      eventType: 'linkClick',
      event: 'Equipment Information Update'
    }
  };
  trackAnalytics(formObj, 'linkClick', 'linkClick', undefined, false);
};