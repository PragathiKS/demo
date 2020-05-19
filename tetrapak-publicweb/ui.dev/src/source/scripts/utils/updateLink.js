import $ from 'jquery';

const myDomain = 'tetrapak.com';
const componentList = ['.medialink','.pw-megamenu','.pw-navigation'];

const isExternal = function (url) {
  if (url && url.includes('http://') ||  url && url.includes('https://')) {
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
  const fileList = ['.pdf', '.xls', '.xlsx', '.doc', '.docx', '.ppt', '.pttx', '.jpeg', '.png', '.jpg', '.svg'] ;
  let flag = false ;
  const endPart = url && url.split('/').pop() ;

  for (let i = 0; i < fileList.length; i++) {
    if (endPart && endPart.includes(fileList[i])) {
      flag = true ;
    }
  }
  return flag ;
};

export default  () => {
  componentList.forEach(item => {
    $(item).find('a').each(function () {
      const thisHref = $(this).attr('href');
      const iconEl = $(this).find('i.icon')[0];
      if (isDownloable(thisHref)) {
        $(iconEl).addClass('icon-Download');
        $(this).attr('target','_self');
        $(this).attr('data-link-section', 'Related links and downloads_Hyperlink_Download');
      } else if (isExternal(thisHref)) {
        $(iconEl).addClass('icon-Union');
        $(this).attr('target','_blank');
      } else if($(iconEl).hasClass('with-arrow')){
        $(iconEl).addClass('icon-Chevron_Down');
        $(this).attr('target','_self');
      }else if($(iconEl).hasClass('without-arrow')){
        $(iconEl).addClass('');
        $(this).attr('target','_self');
      } else {
        $(iconEl).addClass('icon-Circle_Arrow_Right_pw');
        $(this).attr('target','_self');
      }
    });
  });
};
