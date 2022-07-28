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

export const _trackWithdrawStart = function () {
  const formObj = {
    formName: 'Licenses Withdraw Request'
  };

  const eventObj = {
    eventType: 'formstart',
    event: 'Licenses Withdraw Request form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackWithdrawCancel = function () {
  const formObj = {
    formName: 'Licenses Withdraw Request'
  };

  const eventObj = {
    eventType: 'formcancel',
    event: 'Licenses Withdraw Request form'
  };

  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};

export const _trackWithdrawComplete = function (licenseDetails) {
  const formField = [];

  for (const [key, value] of Object.entries(licenseDetails)) {
    formField.push(
      {
        formFieldName: key,
        formFieldValue: value
      }
    );
  }

  const formObj = {
    formName: 'Licenses Withdraw Request',
    formField
  };

  const eventObj = {
    eventType: 'formcomplete',
    event: 'Licenses Withdraw Request form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};
