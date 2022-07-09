import { trackAnalytics } from '../../../scripts/utils/analytics';

export const _trackAccordionClick = function (linkName, expand) {
  const eventObj = {
    event: expand ? 'accordion expand' : 'accordion collapse',
    eventType: 'linkClick'
  };

  const linkClickObj = {
    linkName: linkName,
    linkType: 'internal',
    linkSection: 'Active licenses',
    linkParentTitle: ''
  };

  trackAnalytics(linkClickObj, 'linkClick', 'accordionClick', undefined, false, eventObj);
};

export const _trackFormStart = function () {
  const formObj = {
    formName: 'Request Site Licenses'
  };

  const eventObj = {
    eventType: 'formstart',
    event: 'Request Site Licenses form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackFormComplete = function (processedFormData) {
  const formField = [];

  for (const [key, value] of Object.entries(processedFormData)) {
    formField.push(
      {
        formFieldName: key,
        formFieldValue: value
      }
    );
  }

  const formObj = {
    formName: 'Request Site Licenses',
    formField
  };

  const eventObj = {
    eventType: 'formcomplete',
    event: 'Request Site Licenses form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackFormError = function (formError) {
  const formObj = {
    formName: 'Request Site Licenses',
    formError
  };

  const eventObj = {
    eventType: 'formerror',
    event: 'Request Site Licenses form'
  };

  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};
