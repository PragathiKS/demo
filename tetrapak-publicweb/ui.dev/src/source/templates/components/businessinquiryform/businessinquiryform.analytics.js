import { trackAnalytics } from '../../../scripts/utils/analytics';

export const makeLoad = function () {
  const formObj = {
    formName: 'Business Enquiry',
    formStep: 'Step 1',
    formType: '',
    areaofInterest: '',
    formField: []
  };
  const eventObj = {
    eventType: 'formstart',
    event: 'Hard Conversion Form'
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};


export const changeStepNext = function (formStep, formType, areaofInterest, dataObj) {
  const formObj = {
    formName: 'Business Enquiry',
    formStep: formStep,
    formType: formType,
    areaofInterest: areaofInterest,
    formField: []
  };
  const eventObj = {
    eventType: formStep + ' complete',
    event: 'Hard Conversion Form'
  };
  Object.keys(dataObj).forEach(i => {
    if(dataObj[i]) {
      formObj.formField.push({
        formFieldName: i,
        formFieldValue: dataObj[i]
      });  
    }
  });
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};


export const changeStepPrev = function (formStep, formType, areaofInterest, dataObj) {
  const formObj = {
    formName: 'Business Enquiry',
    formStep: formStep,
    formType: formType,
    areaofInterest: areaofInterest,
    formField: []
  };
  const eventObj = {
    eventType: formStep + ' previous',
    event: 'Hard Conversion Form'
  };
  Object.keys(dataObj).forEach(i => {
    if(dataObj[i]) {
      formObj.formField.push({
        formFieldName: i,
        formFieldValue: dataObj[i]
      });  
    }
  });
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};


export const changeStepError = function (formStep, formType, areaofInterest, dataObj, errorObj={}) {
  const formObj = {
    formName: 'Business Enquiry',
    formStep: formStep,
    formType: formType,
    areaofInterest: areaofInterest,
    formField: [],
    formError:errorObj
  };
  const eventObj = {
    eventType: 'formerror',
    event: 'Hard Conversion Form'
  };
  Object.keys(dataObj).forEach(i => {
    if(dataObj[i]) {
      formObj.formField.push({
        formFieldName: i,
        formFieldValue: dataObj[i]
      });  
    }
  });
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};



export const loadThankYou = function (areaofInterest, dataObj) {
  const formObj = {
    formName: 'Business Enquiry',
    formStep: 'Step 4',
    formType: 'Contact information',
    areaofInterest: areaofInterest,
    formField: []
  };
  const eventObj = {
    eventType: 'formcomplete',
    event: 'Hard Conversion Form'
  };
  Object.keys(dataObj).forEach(i => {
    if(dataObj[i]) {
      formObj.formField.push({
        formFieldName: i,
        formFieldValue: dataObj[i]
      });  
    }
  });
  trackAnalytics(formObj, 'form', 'formcomplete', undefined, false, eventObj);
};




export const NewPage = function () {
  const linkClickObj = {
    linkType: 'internal',
    linkSection: 'Hard Conversion Form_button click',
    linkParentTitle: '',
    linkName: 'New Request'
  };
  const eventObj = {
    eventType: 'linkClick',
    event: 'Hard Conversion Form'
  };
  trackAnalytics(linkClickObj, 'linkclick', 'linkclick', undefined, false, eventObj);
};
