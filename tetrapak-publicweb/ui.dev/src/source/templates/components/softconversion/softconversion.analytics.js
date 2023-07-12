import { trackAnalytics } from '../../../scripts/utils/analytics';

export const makeLoad = function (label, formName, parentComponent, eventType='formstart') {
  const formObj = {
    formName: formName,
    formField: []
  };
  const eventObj = {
    eventType: eventType,
    event: `${parentComponent} : Soft Conversion Form`
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};


export const changeStepNext = function (formName, dataObj, parentComponent) {
  const formObj = {
    formName: formName,
    formField: []
  };
  const eventObj = {
    eventType: `formreturn`,
    event: `${parentComponent} : Soft Conversion Form`
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


export const changeStepPrev = function (formName, formStep, formType, parentComponent) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    formField: []
  };
  const eventObj = {
    eventType: `${formStep} previous`,
    event: `${parentComponent} : Soft Conversion Form`
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};


export const changeStepError = function (formName, formStep, formType, dataObj, parentComponent, errorObj={}) {
  const formObj = {
    formName: formName,
    formField: [],
    formError:errorObj
  };
  const eventObj = {
    eventType: 'formerror',
    event: `${parentComponent} : Soft Conversion Form`
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

export const loadDownloadReady = function (formName, dataObj, parentComponent) {
  const formObj = {
    formName: formName,
    formField: []
  };
  const eventObj = {
    eventType: 'formcomplete',
    event: `${parentComponent} : Soft Conversion Form`
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

export const downloadLinkTrack = function (downObj, eventType, parentComponent) {
  const eventObj = {
    eventType: eventType,
    event: `${parentComponent} : Soft Conversion Form`
  };

  trackAnalytics(downObj, 'linkClick', eventType, undefined, false, eventObj);

};

