/**
 * Process dynamic media images
 * @author Sachin Singh
 * @date 16-02-2019
 */

import $ from 'jquery';
import LazyLoad from 'vanilla-lazyload';

function _processImageAttributes(container,isMediaChanged) {
  $('.js-dynamic-media').each(function () {
    const $this = $(this);
    const desktopSrc = $this.attr('data-src_desktop');
    let desktopL = $this.attr('data-src_desktopL');
    let desktopXL = $this.attr('data-src_desktopXL');
    let mobileLandSrc = $this.attr('data-src_mobilel');
    let mobilePortSrc = $this.attr('data-src_mobilep');

    // To re-process image on media query change
    if (isMediaChanged) {
      $this.removeAttr('data-was-processed');
    }

    if (typeof desktopSrc !== 'undefined') {
      if (typeof desktopL === 'undefined') {
        desktopL = desktopSrc;
      }
      if (typeof desktopXL === 'undefined') {
        desktopXL = desktopSrc;
      }
      if (typeof mobileLandSrc === 'undefined') {
        mobileLandSrc = desktopSrc;
      }
      if (typeof mobilePortSrc === 'undefined') {
        mobilePortSrc = mobileLandSrc;
      }
    }

    if (window.matchMedia('(max-width: 414px)').matches || window.matchMedia('(max-width: 414px) and (-webkit-min-device-pixel-ratio: 2), (max-width: 414px) and (min-resolution: 192dpi), (max-width: 414px) and (min-resolution: 2dppx)').matches) {
      // mobile portrait
      $this.attr('data-src', mobilePortSrc);
    } else if (window.matchMedia('(min-width: 415px) and (max-width: 1023px)').matches || window.matchMedia('(min-width: 415px) and (max-width: 1023px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 415px) and (max-width: 1023px) and (min-resolution: 192dpi), (min-width: 415px) and (max-width: 1023px) and (min-resolution: 2dppx)').matches) {
      // mobile landscape
      $this.attr('data-src', mobileLandSrc);
    } else if (window.matchMedia('(min-width: 1024px) and (max-width: 1439px)').matches || window.matchMedia('(min-width: 1024px) and (max-width: 1439px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 1024px) and (max-width: 1439px) and (min-resolution: 192dpi), (min-width: 1024px) and (max-width: 1439px) and (min-resolution: 2dppx)').matches) {
      // desktop
      $this.attr('data-src', desktopSrc);
    } else if (window.matchMedia('(min-width: 1440px) and (max-width: 1680px)').matches || window.matchMedia('(min-width: 1440px) and (max-width: 1680px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 1440px) and (max-width: 1680px) and (min-resolution: 192dpi), (min-width: 1440px) and (max-width: 1680px) and (min-resolution: 2dppx)').matches) {
      // desktop Large
      $this.attr('data-src', desktopL);
    } else {
      //desktop xtra large
      $this.attr('data-src', desktopXL);
    }

    if($this.hasClass('load-mega-menu-dynamic-media')){
      if(window.matchMedia('(min-width: 1200px) and (max-width: 1439px)').matches || window.matchMedia('(min-width: 1200px) and (max-width: 1439px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 1200px) and (max-width: 1439px) and (min-resolution: 192dpi), (min-width: 1200px) and (max-width: 1439px) and (min-resolution: 2dppx)').matches) {
        // desktop Large
        $this.attr('data-src', desktopL);
      }
    }


  });
  if (typeof container === 'string') {
    return new LazyLoad({
      container: document.querySelector(container),
      elements_selector: '.js-dynamic-media[data-src]',
      skip_invisible: false
    });
  }
  return new LazyLoad({
    elements_selector: '.js-dynamic-media[data-src]',
    skip_invisible: false
  });
}

export default {
  bindEvents() {
    $(window).on('load resize orientationchange', () => {
      this.processImageAttributes(null,true);
    });
  },
  processImageAttributes() {
    return _processImageAttributes.apply(this, arguments);
  },
  init() {
    this.bindEvents();
    this.processImageAttributes();
  }
};

export const dynMedia = {
  processImages() {
    return _processImageAttributes.apply(this, arguments);
  }
};
