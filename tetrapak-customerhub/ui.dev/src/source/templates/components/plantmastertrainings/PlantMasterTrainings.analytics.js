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
