// import $ from 'jquery';
/* eslint-disable no-console */
export const campaignTrack = {
  init() {
    var hash, valObj = {};
    if(!window.OptanonActiveGroups.includes(',2,')) {
      var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
      for(var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        if(hash[0] === 'utm_medium' || hash[0] === 'utm_source' || hash[0] === 'utm_campaign' || hash[0] === 'utm_term' || hash[0] === 'utm_content') {
          valObj[hash[0]] = hash[1];
          console.log('Value Object', valObj);
          if(i === hashes.length - 1) {
            sessionStorage.setItem('CTPOTI', JSON.stringify(valObj));
          }
        }
      }
    }
  }
};

