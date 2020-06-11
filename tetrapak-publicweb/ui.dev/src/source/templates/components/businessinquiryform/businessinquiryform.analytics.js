import { trackAnalytics } from '../../../scripts/utils/analytics';

export const makeLoad = function () {
  const digitalData = {
    form: {
      formName: 'Business Enquiry',
      formStep: 'Step 1',
      formType: '',
      areaofInterest: '',
      formField: []
    },
    event: {
      eventType: 'formstart',
      event: 'Hard Conversion Form'
    }
  };
  trackAnalytics(digitalData.form, 'formload', 'formload', undefined, false, digitalData.event);
};


export const changeStepNext = function (formStep, formType, areaofInterest, dataObj) {
  const digitalData = {
    form: {
      formName: 'Business Enquiry',
      formStep: formStep,
      formType: formType,
      areaofInterest: areaofInterest,
      formField: []
    },
    event: {
      eventType: formStep + ' complete',
      event: 'Hard Conversion Form'
    }
  };
  Object.keys(dataObj).forEach(i => {
    digitalData.form.formField.push({
      formFieldName: i,
      formFieldValue: dataObj[i]
    });
  });
  trackAnalytics(digitalData.form, formStep + ' complete', formStep + ' complete', undefined, false, digitalData.event);
};


export const loadThankYou = function (formType, areaofInterest, dataObj) {
  const digitalData = {
    form: {
      formName: 'Business Enquiry',
      formStep: 'Step 4',
      formType: 'Contact information',
      areaofInterest: areaofInterest,
      formField: []
    },
    event: {
      eventType: 'formcomplete',
      event: 'Hard Conversion Form'
    }
  };
  Object.keys(dataObj).forEach(i => {
    digitalData.form.formField.push({
      formFieldName: i,
      formFieldValue: dataObj[i]
    });
  });
  trackAnalytics(digitalData.form, 'formcomplete', 'formcomplete', undefined, false, digitalData.event);
};


export const NewPage = function () {
  const digitalData = {
    linkClick: {
      linkType: 'internal',
      linkSection: 'Hard Conversion Form_button click',
      linkParentTitle: '',
      linkName: 'New Request'
    },
    event: {
      eventType: 'linkClick',
      event: 'Hard Conversion Form'
    }
  };
  trackAnalytics(digitalData.linkClick, 'linkClick', 'formcomplete', undefined, false, digitalData.event);
};
