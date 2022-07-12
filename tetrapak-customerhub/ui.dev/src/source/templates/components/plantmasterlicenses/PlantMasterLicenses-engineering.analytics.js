import { trackAnalytics } from '../../../scripts/utils/analytics';

export const _trackTabClick = function () {
  const eventObj = {
    event: 'breadcrumb',
    eventType: 'linkClick'
  };

  const linkClickObj = {
    linkName: 'Request annual engineering licenses',
    linkType: 'internal',
    linkSection: 'hyperlink click',
    linkParentTitle: 'Tetra Pak® PlantMaster licenses'
  };

  trackAnalytics(linkClickObj, 'linkClick', 'linkClick', undefined, false, eventObj);
};

export const _trackFormStart = function (trainingName) {
  const formObj = {
    formName: 'Request annual engineering licenses',
    trainingName
  };

  const eventObj = {
    eventType: 'formstart',
    event: 'Request annual engineering licenses form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackFormComplete = function (formData) {
  const formField = [];
  const formattedUsers = [];
  const { users, comments } = formData;

  users.forEach(user => {
    const userFields = [];

    for (const [key, value] of Object.entries(user)) {
      userFields.push({
        formFieldName: key,
        formFieldValue: value
      });
    }

    formattedUsers.push(userFields);
  });

  formField.push({
    users: formattedUsers,
    comments
  });

  const formObj = {
    formName: 'Request annual engineering licenses',
    formField
  };

  const eventObj = {
    eventType: 'formcomplete',
    event: 'Request annual engineering licenses form'
  };

  trackAnalytics(formObj, 'form', 'formload', undefined, false, eventObj);
};

export const _trackFormError = function (formError) {
  const formObj = {
    formName: 'Request annual engineering licenses',
    formError
  };

  const eventObj = {
    eventType: 'formerror',
    event: 'Request annual engineering licenses form'
  };

  trackAnalytics(formObj, 'form', 'formclick', undefined, false, eventObj);
};
