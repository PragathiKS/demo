import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';

export const makeLoad = function (label, formName, countryData) {
  const formObj = {
    formName: formName,
    formStep: 'Step 1',
    formType: label,
    areaofInterest: '',
    formIdGlobal: countryData.formHandler,
    formField: []
  };
  const eventObj = {
    eventType: 'formstart',
    event: 'Hard Conversion Form'
  };
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const changeStepNext = function (formName, formStep, formType, areaofInterest, dataObj, countryData) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    areaofInterest: areaofInterest,
    formIdGlobal: countryData.formHandler,
    formField: []
  };
  const eventObj = {
    eventType: `${formStep} Next`,
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

export const changeStepPrev = function (formName, formStep, formType, areaofInterest, countryData) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    areaofInterest: areaofInterest,
    formIdGlobal: countryData.formHandler,
    formField: []
  };
  const eventObj = {
    eventType: `${formStep} previous`,
    event: 'Hard Conversion Form'
  };
  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};

export const changeStepError = function (formName, formStep, formType, areaofInterest, dataObj, errorObj={}, countryData) {
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formType,
    areaofInterest: areaofInterest,
    formIdGlobal: countryData.formHandler,
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

export const loadThankYou = function (formName, formStep, areaofInterest, dataObj, countryData) {
  const formTitle = $('.heading-summary h4').text().trim();
  const formObj = {
    formName: formName,
    formStep: formStep,
    formType: formTitle,
    areaofInterest: areaofInterest,
    formIdGlobal: countryData.formHandler,
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
  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const newPage = function (linkTitle, linkName) {
  const linkClickObj = {
    linkType: 'internal',
    linkSection: 'Hard Conversion Form_button click',
    linkParentTitle: linkTitle,
    linkName: linkName
  };
  const eventObj = {
    eventType: 'linkClick',
    event: 'Hard Conversion Form'
  };
  trackAnalytics(linkClickObj, 'linkClick', 'linkClick', undefined, false, eventObj);
};