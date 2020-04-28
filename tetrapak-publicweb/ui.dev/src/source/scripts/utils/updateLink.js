import $ from 'jquery';

const myDomain = 'jsbin.com';
const componentList = ['.medialink'];

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
  const fileList = ['.pdf', '.xls', '.xlsx', '.doc', '.docx', '.ppt', '.pttx', '.jpeg', '.png', '.svg'] ;
  let flag = false ;
  const endPart = url.split('/').pop() ;

  for (let i = 0; i < fileList.length; i++) {
    if (endPart.includes(fileList[i])) {
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
        $(iconEl).addClass('icon-download');
      } else if (isExternal(thisHref)) {
        $(iconEl).addClass('icon-external');
      } else {
        $(iconEl).addClass('icon-internal');
      }
    });
  });
};