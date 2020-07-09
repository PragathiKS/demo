import { trackAnalytics } from '../../../scripts/utils/analytics';

export const makeLoad = function (label, formName, eventType='formstart', parentComponent) {
  const formObj = {
    formName: formName,
    formStep: 'Step 1',
    formType: label,
    formField: []
  };
  const eventObj = {
    eventType: eventType,
    event: `${parentComponent} : Soft Conversion Form`
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};


export const changeStepNext = function (formName, formStep, formType, dataObj, parentComponent) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    formField: []
  };
  const eventObj = {
    eventType: `${formStep} Next`,
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


export const changeStepError = function (formName, formStep, formType, dataObj, errorObj={}, parentComponent) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
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
    formStep: 'Step 3',
    formType: 'Company information',
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

  // if(caseCheck === 'download'){
  //   const formObj = {
  //     formName: formName,
  //     formStep: 'Step 4',
  //     formType: 'Download Ready',
  //     formField: []
  //   };
  //   const eventObjN = {
  //     eventType: 'formcomplete',
  //     event: 'Soft Conversion Form'
  //   };
  //   trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObjN);
  // }

};

