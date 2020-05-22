import $ from 'jquery';

const myDomain = 'tetrapak.com';
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
  '.tp-pw-richText'
];



const isExternal = function (url) {
  if (url.includes('http://') || url.includes('https://')) {
    if (url.includes(myDomain)) {
      return false;
    } else {
      return true;
    }
  } else {
    return false;
  }
};

const isDownloable = function (url) {
  const fileList = ['.pdf', '.xls', '.xlsx', '.doc', '.docx', '.ppt', '.pttx', '.jpeg', '.png', '.jpg', '.svg'];
  let flag = false;
  const endPart = url.split('/').pop();

  for (let i = 0; i < fileList.length; i++) {
    if (endPart.includes(fileList[i])) {
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
        $(iconEl).removeClass('icon-Circle_Arrow_Right_pw');
        if (isDownloable(thisHref)) {
          $(iconEl).addClass('icon-Download');
          $(this).attr('target', '_self');
          $(this).attr('data-link-section', 'Related links and downloads_Hyperlink_Download');
        } else if (isExternal(thisHref)) {
          $(iconEl).addClass('icon-Union');
          $(this).attr('target', '_blank');
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
