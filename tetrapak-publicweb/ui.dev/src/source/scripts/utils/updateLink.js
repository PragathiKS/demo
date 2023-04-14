import $ from 'jquery';

const myDomainAdobe = 'adobecqms.net';
const componentList = [
  '.medialink',
  '.textImage',
  '.pw-megamenu',
  '.tp-pw-footer',
  '.pw-megamenu',
  '.pw-tablist',
  '.tp-teaser',
  '.pw-text-image',
  '.pw-text-video',
  '.pw-banner',
  '.tp-pw-richText',
  '.pw-navigation'
];

const isInternalUrl = url => {
  let isInternal = false;
  const setOfInternalUrl = [
    'https://www-dev.tetrapak.com/',
    'https://www-qa.tetrapak.com/',
    'https://www-stage.tetrapak.com/',
    'https://www.tetrapak.com/',
    'https://www-dev.tetralaval.com/',
    'https://www-qa.tetralaval.com/',
    'https://www-stage.tetralaval.com/',
    'https://www.tetralaval.com/'
  ];

  for (var i = 0; i < setOfInternalUrl.length; i++) {
    if (setOfInternalUrl[i] && url.indexOf(setOfInternalUrl[i]) !== -1) {
      isInternal = true;
      break;
    }
  }
  return isInternal;
};

export const isExternal = function(url) {
  if (url && (url.includes('http://') || url.includes('https://'))) {
    return isInternalUrl(url) || url.includes(myDomainAdobe) ? false : true;
  } else {
    return false;
  }
};

export const getUrl = function($el) {
  if ($el.prop('nodeName') === 'A') { 
    return $el.attr('href'); 
  }
  return $el.closest('a').attr('href') || '';
};

export const isDownloable = function(url) {
  const fileList = [
    '.pdf',
    '.xls',
    '.xlsx',
    '.doc',
    '.docx',
    '.ppt',
    '.pttx',
    '.jpeg',
    '.png',
    '.jpg',
    '.svg'
  ];
  let flag = false;
  const endPart = url && url.split('/').pop();

  for (let i = 0; i < fileList.length; i++) {
    if (endPart && endPart.includes(fileList[i])) {
      flag = true;
    }
  }
  return flag;
};

export default () => {
  componentList.forEach(item => {
    $(item).find('a').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        const iconEl = $(this).find('i.icon')[0];
        // external link flag
        const iconExternal = $(this).find('i.icon.is-external')[0];
        $(iconEl).removeClass('icon-Circle_Arrow_Right_pw');
        if (isDownloable(thisHref)) {
          $(iconEl).addClass('icon-Download');
          $(this).attr('target', '_blank');
        } else if (isExternal(thisHref) || iconExternal) {
          $(iconEl).addClass('icon-Union');
          $(this).attr('target', '_blank');
        } else if($(iconEl).hasClass('with-arrow')){
          $(iconEl).addClass('icon-Navigation_Right_pw');
          $(this).attr('target', '_self');
        } else if($(iconEl).hasClass('without-arrow')){
          $(iconEl).addClass('');
        } else {
          if ($(this).hasClass('tpatom-link--primary')) {
            $(iconEl).addClass('icon-Arrow_Right_pw');
          } else {
            $(iconEl).addClass('icon-Circle_Arrow_Right_pw');
          }
          $(this).attr('target', '_self');
        }
      }
    });
  });
};
