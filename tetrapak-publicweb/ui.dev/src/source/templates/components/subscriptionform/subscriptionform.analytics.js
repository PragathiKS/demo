import { trackAnalytics } from '../../../scripts/utils/analytics';

export const subscriptionAnalytics = function (formName, formField, eventType, trackingKey, errorObj=[], Step) {
  const formObj = {
    formName: formName,
    formStep: Step,
    formField: formField
  };

  if(errorObj.length > 0){
    formObj.formError = errorObj;
  }

  const eventObj = {
    eventType: eventType,
    event: 'Newsletter'
  };
  trackAnalytics(formObj, 'form', trackingKey, undefined, false, eventObj);
};


// export const changeStepError = function (formName, formStep, formType, areaofInterest, dataObj, errorObj={}) {
//   const formObj = {
//     formName: formName,
//     formStep: formStep,
//     formType: formType,
//     areaofInterest: areaofInterest,
//     formField: [],
//     formError:errorObj
//   };
//   const eventObj = {
//     eventType: 'formerror',
//     event: 'Hard Conversion Form'
//   };
//   Object.keys(dataObj).forEach(i => {
//     if(dataObj[i]) {
//       formObj.formField.push({
//         formFieldName: i,
//         formFieldValue: dataObj[i]
//       });  
//     }
//   });
//   trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
// };



export const loadThankYou = function (formName, areaofInterest, dataObj) {
  const formObj = {
    formName: formName,
    formStep: 'Step 4',
    formType: 'Thankyou',
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