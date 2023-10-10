import $ from 'jquery';
import { getI18n } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';


const trackAnalyticsContactUs = (formTrackingObj,eventObj,formVal='formclick') => {

  formTrackingObj = {
    formName:$('.js-step1-main-heading').data('form-name'),
    ...formTrackingObj
  };

  formTrackingObj.formName = `Contact | ${formTrackingObj.formName}`;

  eventObj = {
    ...eventObj,
    event:'Contact us form'
  };
  trackAnalytics(
    formTrackingObj,
    'form',
    formVal,
    undefined,
    false,
    eventObj,
  );
};

export const onErrorAnalytics = (currentTarget,tab) => {
  const formType = tab.find('.form-field-heading').data('step-heading');
  const formError = [];
  const formField = [];
  const event = {
    eventType : 'formError'
  };
  if(currentTarget === 'cf-step-1'){
    formError.push({formErrorField:tab.find('.form-field-heading').data('step-heading'), formErrorMessage:tab.find('.formfield .errorMsg').data('purposeofcontact-errmsg')});
  } else if(currentTarget === 'cf-step-2'){
    if(tab.find('.first-name .field-error').length > 0){
      formError.push({formErrorField:tab.find('.first-name').data('first-name-label'), formErrorMessage:tab.find('.first-name .errorMsg').data('error-msg')});
    }
    if(tab.find('.last-name .field-error').length > 0){
      formError.push({formErrorField:tab.find('.last-name').data('last-name-label'), formErrorMessage:tab.find('.last-name .errorMsg').data('error-msg')});
    }
    if(tab.find('.email-contact .field-error').length > 0){
      formError.push({formErrorField:tab.find('.email-contact').data('email-name-label'), formErrorMessage:tab.find('.email-contact .errorMsg').data('error-msg')});
    }
    if(tab.find('.country.field-error').length > 0){
      formError.push({formErrorField:tab.find('.country').data('country-name-label'), formErrorMessage:tab.find('.country .errorMsg').data('country-error-msg')});
    }
  } else if(currentTarget === 'cf-step-3'){
    if(tab.find('.formfield.field-error').length > 0){
      formError.push({formErrorField:tab.find('.formfield .text-area-label').data('text-area-label'), formErrorMessage:tab.find('.formfield .errorMsg').data('error-msg')});
    }
  }

  const form  = {
    formType,
    formField,
    formError,
    formStep : tab.find('.button').data('form-step')
  };

  trackAnalyticsContactUs(form,event);
};

export const newRequestButtonAnalytics = (e) => {
  const $target = $(e.target);
  const linkName = $target.text().trim();
  const trackingObj = {
    linkType: 'internal',
    linkSection: 'button click',
    linkParentTitle: $('.contact-us-thankyou-text').data('thankyou-text'),
    linkName
  };
  const eventObj = {
    eventType: 'linkClick',
    event: 'Contact us form'
  };
  trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
};

export const onNextClickAnalytics = (currentTarget,tab,requestPayload) => {
  const formType = tab.find('.form-field-heading').data('step-heading');
  const formField = [];
  let event = {
    eventType : 'step 1 next'
  };
  if(currentTarget === 'cf-step-1'){
    formField.push({formFieldName:tab.find('.form-field-heading').data('step-heading'), formFieldValue:requestPayload['purposeOfContactTitle']});
  } else if(currentTarget === 'cf-step-2'){
    formField.push(
      {formFieldName:tab.find('.first-name').data('first-name-label'), formFieldValue:'NA'},
      {formFieldName:tab.find('.last-name').data('last-name-label'), formFieldValue:'NA'},
      {formFieldName:tab.find('.email-contact').data('email-name-label'), formFieldValue:'NA'},
      {formFieldName:tab.find('.country').data('country-name-label'), formFieldValue:requestPayload['country']},
    );
    event = {
      eventType : 'step 2 next'
    };
  }
  const form  = {
    formType,
    formField,
    formStep : tab.find('.button').data('form-step')
  };

  trackAnalyticsContactUs(form,event);
};

export const onPreviousClickAnalytics = (currentTarget,tab) => {
  const formType = tab.find('.form-field-heading').data('step-heading');
  const formField = [];
  let event = {
    eventType : 'step 2 previous'
  };
  if(currentTarget === 'cf-step-3'){
    event = {
      eventType : 'step 3 previous'
    };
  }
  const form  = {
    formType,
    formField,
    formStep : tab.find('.button').data('form-step')
  };

  trackAnalyticsContactUs(form,event);
};

export const onLoadTrackAnalytics = () => {
  const form = {
    formType:$('.js-form-field-heading-step1').data('step1-heading'),
    formStep:'step 1',
    formField: []
  };
  const event = {
    eventType : 'formStart'
  };
  trackAnalyticsContactUs(form,event,'formload');
};

export const onSubmitClickAnalytics = (tab) => {
  const formTitle = getI18n('pw.form.label.message');
  const formHeading = tab.find('.form-field-heading').data('step-heading');
  const formField = [];
  formField.push({formFieldName:formTitle, formFieldValue:'NA'});
  const form = {
    formType: formHeading,
    formStep:'step 3',
    formField
  };
  const event = {
    eventType : 'formComplete'
  };
  trackAnalyticsContactUs(form,event,'formload');
};
