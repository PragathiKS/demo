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
    this.cache.$anchorMenu = document.getElementsByClassName('pw-anchor-menu');
    this.cache.$progressIndicator = this.root.find('.pw-anchor-menu-progress-indicator');
    this.cache.$anchorMenuContent = this.root.find('.pw-anchor-menu-content');
    this.cache.$offsetTop = this.cache.$anchorMenu[0].offsetTop;

    //Default selection
    if (this.cache.$progressIndicator && this.cache.$anchorMenuContent) {
      this.cache.$progressIndicator.css('width', `${this.cache.$anchorMenuContent[0].children[0].offsetWidth}px`);
      this.cache.$progressIndicator.css('margin-left',`${this.cache.$anchorMenuContent[0].children[0].offsetLeft}px`);
      this.setAnchorMenuSelection(0);
    }
  }
  bindEvents() {
    const { $anchorLink, $anchorMenu } = this.cache;
    $anchorLink.on('click', this.scrollToSection);
    if($anchorMenu) {
      const doc = document.getElementsByClassName('anchormenu');
      if ( doc && doc.length > 0 ) {
        window.addEventListener('scroll', this.onPageScroll, false);
      }
    }
  }

  onPageScroll = () => {
    const { $anchorMenu, $offsetTop, $anchorMenuContent } = this.cache;
    if (window.pageYOffset >= $offsetTop) {
      $anchorMenu[0].classList.add('sticky-anchor-menu');
      $anchorMenu[0].classList.remove('tp-container');
    } else {
      $anchorMenu[0].classList.remove('sticky-anchor-menu');
      $anchorMenu[0].classList.add('tp-container');
    }

    //Update progress indicator
    for(let index=0; index<$anchorMenuContent.length; index++) {
      if (this.isElementInViewport(document.querySelector(`#${$anchorMenuContent[index].children[0].dataset.linkSection}`))) {
        this.setAnchorMenuSelection(index);
      } 
    }
  }

  scrollToSection = e => {
    e.preventDefault();
    const $this = $(e.target);
    const anchorId = $this.data('link-section');
    const linkName = $this.data('link-name');
    const currentIndex = $this.parents('li').index();

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

    //Set selection
    this.setAnchorMenuSelection(currentIndex);
  };

  setAnchorMenuSelection(currentIndex) {
    const { $progressIndicator, $anchorMenuContent } = this.cache;
    //Progress indicator
    $progressIndicator.css('margin-left', `${$anchorMenuContent[currentIndex].children[0].offsetLeft}px`);
    $progressIndicator.css('width', `${$anchorMenuContent[currentIndex].children[0].offsetWidth}px`);
    for(let index=0; index<$anchorMenuContent.length; index++) {
      if (currentIndex === index) {
        $anchorMenuContent[index].children[0].classList.add('active');
      } else {
        $anchorMenuContent[index].children[0].classList.remove('active');
      }
    }
  }

  isElementInViewport(element) {
    const rect = element.getBoundingClientRect();
    return (
      rect.top >= 0 &&
      rect.left >= 0 &&
      rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
      rect.right <= (window.innerWidth || document.documentElement.clientWidth)
    );
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default AnchorMenu;