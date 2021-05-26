import $ from 'jquery';
import { getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';
class TextImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$textImageLink = this.root.find('.js-textImage-analytics');
    this.cache.componentName = this.root.find('.componentNameTextImage').val();
  }
  bindEvents() {
    this.cache.$textImageLink.on('click', this.trackAnalytics);

    // Open SoftConversion Form
    this.root.find('.js-softconversion-pw').on('click', (e) => {
      getLinkClickAnalytics(e,'image-title','Text & Image','.js-softconversion-pw', false);
      $('body').find('.'+this.cache.componentName).trigger('showsoftconversion-pw');
    });

    // Open Subscription Form
    this.root.find('.js-subscription-pw').on('click', (e) => {
      getLinkClickAnalytics(e,'image-title','Text & Image','.js-subscription-pw', false);
      $('body').find('.'+this.cache.componentName).trigger('showSubscription-pw');
    });
  }

  trackAnalytics = e => {
    e.preventDefault();

    // Add Dummy Image for delay
    const anyNumber = Math.floor(1000 + Math.random() * 9000);
    const url = 'https://s7g10.scene7.com/is/image/tetrapak/cow-from-left?wid=1920&hei=640&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0&q='+anyNumber;
    const image = new Image();
    image.src = url;
    $(image).addClass('dummyImage');
    $(image).css({'display':'none'});
    $('body').append(image);

    setTimeout(function() {
      $('body').find('dummyImage').remove();
    }, 500);
    getLinkClickAnalytics(e,'image-title','Text & Image','.js-textImage-analytics');
  };


  seoChanges() {
    const titleDiv = this.root.find('.pw-text-image__title');
    const h1tag = titleDiv.find('h1') ;
    if( h1tag.length) {
      $(h1tag).attr('class','tpatom-heading tpatom-heading--regular');
      const h2Tag = titleDiv.find('h2')[0];
      h2Tag && h2Tag.parentNode.removeChild(h2Tag);
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.seoChanges();
    addLinkAttr('.js-textImage-analytics');
  }
}

export default TextImage;
