

import $ from 'jquery';
import { trackAnalytics } from '../utils/analytics';
import LZStorage from 'lzstorage';


// Initialize storage utility
export const storageUtil = new LZStorage();
export const strCompressed = new LZStorage({
  compression: true
});

export const getDocType = (url) => {
  const fileList = ['pdf', 'xls', 'xlsx', 'doc', 'docx', 'ppt', 'pttx', 'jpeg', 'png', 'jpg', 'svg'];
  const endPart = url && url.split('/').pop();
  const docType = endPart.split('.').pop();

  if(fileList.includes(docType)){
    return docType;
  }
};

export const getLinkClickAnalytics = (e, parentTitle, componentName, linkClass, redirect = true, dataObj = {}) => {
  const $target = $(e.target);
  const $this = $target.closest(linkClass);
  const downloadtype = $this.data('download-type');
  let trackingKey = 'linkClick';
  let linkParentTitle = ('linkParentTitle' in dataObj) ? (dataObj.linkParentTitle || '') : `${$this.data('anchor-type')}_${$this.data(
    parentTitle
  )}`;
  let linkSection = dataObj.linkSection || $this.data('link-section');
  const linkName = dataObj.linkName || $this.data('link-name');
  const linkType = dataObj.linkType || ($this.attr('target') === '_blank' ? 'external' : 'internal');

  const trackingObj = {};
  const eventObject = {};
  let eventType = 'linkClick';

  if (downloadtype === 'download') {
    trackingObj['dwnDocName'] = $this.data('asset-name');
    linkSection = `${$this.data('anchor-type')}_Download`;
    linkParentTitle = `${componentName}_${$this.data('anchor-type')}_Download_${getDocType(
      $this.attr('href')
    )}_${$this.data(parentTitle)}`;
    trackingObj['eventType'] = 'download';
    trackingObj['dwnType'] = 'ungated';
    trackingKey = 'downloadClick';
    eventType = 'downloadClick';
  }

  trackingObj['linkSection'] = linkSection;
  trackingObj['linkName'] = linkName;
  trackingObj['linkParentTitle'] = linkParentTitle;
  trackingObj['linkType'] = linkType;

  eventObject['event'] = componentName;
  eventObject['eventType'] = eventType;

  trackAnalytics(
    trackingObj,
    'linkClick',
    trackingKey,
    undefined,
    false,
    eventObject
  );

  if (redirect) {
    setTimeout(function () {
      if (linkType === 'internal') {
        if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224) {
          window.open($this.attr('href'), '_blank');
        }
        else {
          window.open($this.attr('href'), '_self');
        }
      }
      else {
        window.open($this.attr('href'), $this.attr('target'));
      }
    }, 500);
  }
};