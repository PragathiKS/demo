import { trackAnalytics } from '../../../scripts/utils/analytics';

export const _trackAccordionClick = function (linkName, expand) {
  const eventObj = {
    event: expand ? 'accordion expand' : 'accordion collapse',
    eventType: 'linkClick'
  };

  const linkClickObj = {
    linkName: linkName,
    linkType: 'internal',
    linkSection: 'Learning History',
    linkParentTitle: ''
  };

  trackAnalytics(linkClickObj, 'linkClick', 'accordionClick', undefined, false, eventObj);
};

export const _trackFormStart = function (trainingName) {
  const formObj = {
    formName: 'PlantMaster Trainings',
    trainingName
  };

  const eventObj = {
    eventType: 'formstart',
    event: 'PlantMaster Trainings form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackFormComplete = function (trainingName, processedFormData) {
  const formField = [];

  for (const [key, value] of processedFormData) {
    formField.push(
      {
        formFieldName: key,
        formFieldValue: value
      }
    );
  }

  const formObj = {
    formName: 'PlantMaster Trainings',
    trainingName,
    formField
  };

  const eventObj = {
    eventType: 'formcomplete',
    event: 'PlantMaster Trainings form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackFormError = function (trainingName, formError) {
  const formObj = {
    formName: 'PlantMaster Trainings',
    formError,
    trainingName
  };

  const eventObj = {
    eventType: 'formerror',
    event: 'PlantMaster Trainings form'
  };

  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};

