import { trackAnalytics } from '../../../scripts/utils/analytics';

export const _trackTabClick = function () {
  const eventObj = {
    event: 'breadcrumb',
    eventType: 'linkClick'
  };

  const linkClickObj = {
    linkName: 'Request site licenses',
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: 'Tetra Pak® PlantMaster licenses'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'linkClick', undefined, false, eventObj);
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
