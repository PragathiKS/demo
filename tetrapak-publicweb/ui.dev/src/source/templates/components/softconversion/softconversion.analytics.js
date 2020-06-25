import { trackAnalytics } from '../../../scripts/utils/analytics';

export const makeLoad = function (label, formName, eventType='formstart') {
  const formObj = {
    formName: formName,
    formStep: 'Step 1',
    formType: label,
    formField: []
  };
  const eventObj = {
    eventType: eventType,
    event: 'Soft Conversion Form'
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};


export const changeStepNext = function (formName, formStep, formType, dataObj) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    formField: []
  };
  const eventObj = {
    eventType: `${formStep} Next`,
    event: 'Soft Conversion Form'
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


export const changeStepPrev = function (formName, formStep, formType) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    formField: []
  };
  const eventObj = {
    eventType: `${formStep} previous`,
    event: 'Soft Conversion Form'
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};


export const changeStepError = function (formName, formStep, formType, dataObj, errorObj={}) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    formField: [],
    formError:errorObj
  };
  const eventObj = {
    eventType: 'formerror',
    event: 'Soft Conversion Form'
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



export const loadDownloadReady = function (formName, dataObj) {
  const formObj = {
    formName: formName,
    formStep: 'Step 4',
    formType: 'Download Ready',
    formField: []
  };
  const eventObj = {
    eventType: 'formcomplete',
    event: 'Soft Conversion Form'
  };
  Object.keys(dataObj).forEach(i => {
    if(dataObj[i]) {
      formObj.formField.push({
        formFieldName: i,
        formFieldValue: dataObj[i]
      });  
    }
  });
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const downloadLinkTrack = function (downObj, eventType) {
  const eventObj = {
    eventType: eventType,
    event: 'Soft Conversion Form'
  };

  trackAnalytics(downObj, 'linkClick', eventType, undefined, false, eventObj);
};

