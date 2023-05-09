import $ from 'jquery';
import { scrollToElement } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class AnchorMenu {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
    this.cache.$anchorMenu = $('body').find('.pw-anchor-menu');
    this.cache.$offsetTop = this.cache.$anchorMenu.offsetTop;
  }
  bindEvents() {
    const { $anchorLink } = this.cache;
    $anchorLink.on('click', this.scrollToSection);
    if(this.cache.$anchorMenu) {
      // window.onscroll = function() {
      //   this.cache.$anchorMenu.classList.add('sticky');
      // };
      //window.addEventListener('scroll', this.onPageScroll);

      const doc = document.getElementsByClassName('anchormenu');
      if ( doc && doc.length > 1 ) {
        window.addEventListener('scroll', this.onPageScroll);
      } 


      // $(window).on('scroll', () => {
      //   this.cache.$anchorMenu.classList.add('sticky');
      // });
    }


  }

  onPageScroll() { 
    this.cache.$anchorMenu.classList.add('sticky');
  }

  // onPageScroll = e => {
  //   e.preventDefault();
  //   // if (window.pageYOffset >= this.cache.$offsetTop) {
  //   //   this.cache.$anchorMenu.classList.add('sticky');
  //   // } else {
  //   //   this.cache.$anchorMenu.classList.remove('sticky');
  //   // }
  //   this.cache.$anchorMenu.classList.add('sticky');
  // }

  scrollToSection = e => {
    e.preventDefault();
    const $this = $(e.target);
    const anchorId = $this.data('link-section');
    const linkName = $this.data('link-name');

    const trackingObj = {
      linkType: 'internal',
      linkSection: `Hyperlink click`,
      linkParentTitle: '',
      linkName
    };

    const eventObj = {
      eventType: 'linkClick',
      event: 'Anchor Tag'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);

    //end of analytics call
    location.hash = anchorId;
    scrollToElement(null,`#${anchorId}`);
  };

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default AnchorMenu;