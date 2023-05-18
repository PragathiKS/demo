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
    this.cache.$maxPrimaryLinks = 7;
    this.cache.$isAnchorMenuClicked = false;

    //Default selection
    if (this.cache.$progressIndicator && this.cache.$anchorMenuContent) {
      this.cache.$progressIndicator.css('width', `${this.cache.$anchorMenuContent[0].children[0].offsetWidth}px`);
      this.cache.$progressIndicator.css('margin-left',`${this.cache.$anchorMenuContent[0].children[0].offsetLeft}px`);
      this.setAnchorMenuSelection(0);
    }

    if (document.querySelector('.pw-anchor-menu__regular')) {
      const container = document.querySelector('.pw-anchor-menu__regular');
      const primary = container.querySelector('.pw-anchor-menu-list');
      const primaryItems = container.querySelectorAll('.pw-anchor-menu-list > li:not(.more)');
      const allItems = container.querySelectorAll('li');
      if(allItems.length > this.cache.$maxPrimaryLinks) {
        primary.insertAdjacentHTML('beforeend', `
          <li class="more">
            <button type="button" aria-haspopup="true" aria-expanded="true"><span>⋮</span></button>
            <ul class="secondary">
              ${primary.innerHTML}
            </ul>
          </li>
        `);

        const secondary = container.querySelector('.secondary');
        const secondaryItems = secondary.querySelectorAll('li');
        const moreLi = primary.querySelector('.more');
        const moreBtn = moreLi.querySelector('button');
        moreBtn.addEventListener('click', (e) => {
          e.preventDefault();
          container.classList.toggle('show-secondary');
          moreBtn.setAttribute('aria-expanded', container.classList.contains('show-secondary'));
        });
        const hiddenItems = [];
        primaryItems.forEach((item, i) => {
          if(i > this.cache.$maxPrimaryLinks - 1) {
            item.classList.add('hiddenItem');
            hiddenItems.push(i);
          }
        });

        // toggle the visibility of More button and items in Secondary
        if(!hiddenItems.length) {
          moreLi.classList.add('hiddenItem');
          container.classList.remove('show-secondary');
          moreBtn.setAttribute('aria-expanded', false);
        } else {
          secondaryItems.forEach((item, i) => {
            if(!hiddenItems.includes(i)) {
              item.classList.add('hiddenItem');
            } else {
              item.children[0].addEventListener('click', this.scrollToSection, false);
            }
          });
        }

        document.addEventListener('click', (e) => {
          let el = e.target;
          while(el) {
            if(el === secondary || el === moreBtn) {
              return;
            }
            el = el.parentNode;
          }
          container.classList.remove('show-secondary');
          moreBtn.setAttribute('aria-expanded', false);
        });
        //Find child after creation of secondary anchor menus
        this.cache.$secondaryAnchorMenuContent = this.root.find('.secondary')[0].children;
      }
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
    if (!this.cache.$isAnchorMenuClicked) {
      for(let index=0; index<$anchorMenuContent.length; index++) {
        if (this.isElementInViewport(document.querySelector(`#${$anchorMenuContent[index].children[0].dataset.linkSection}`))) {
          this.setAnchorMenuSelection(index);
        }
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

    //Set selection
    this.cache.$isAnchorMenuClicked = true;
    this.setAnchorMenuSelection(currentIndex);
    scrollToElement(()=>{
      this.cache.$isAnchorMenuClicked = false;
    },`#${anchorId}`);
  };

  setAnchorMenuSelection(currentIndex) {
    const { $progressIndicator, $anchorMenuContent, $maxPrimaryLinks, $secondaryAnchorMenuContent } = this.cache;
    //Progress indicator
    $progressIndicator.css('margin-left', `${$anchorMenuContent[currentIndex].children[0].offsetLeft}px`);
    $progressIndicator.css('width', `${$anchorMenuContent[currentIndex].children[0].offsetWidth}px`);
    for(let index=0; index<$anchorMenuContent.length; index++) {
      //Reset active state
      $anchorMenuContent[index].children[0].classList.remove('active');
      if ($secondaryAnchorMenuContent) {
        $secondaryAnchorMenuContent[index].children[0].classList.remove('active');
      }
      if (currentIndex >= $maxPrimaryLinks && currentIndex === index) {
        $secondaryAnchorMenuContent[index].children[0].classList.add('active');
      } else if (currentIndex === index) {
        $anchorMenuContent[index].children[0].classList.add('active');
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