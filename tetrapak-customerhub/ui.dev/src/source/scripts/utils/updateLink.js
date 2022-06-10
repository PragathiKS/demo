const myDomainAdobe = 'adobecqms.net';
const isInternalUrl = url => {
  let isInternal = false;
  const setOfInternalUrl = [
    'https://mypages-dev.tetrapak.com/',
    'https://mypages-qa.tetrapak.com/',
    'https://mypages-stage.tetrapak.com/',
    'https://mypages.tetrapak.com/',
    'https://tetrapak.com/'
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