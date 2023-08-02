import $ from 'jquery';
import { isDesktopScreenMode, scrollToElement } from '../../../scripts/common/common';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class Anchorv2 {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$anchorLink = this.root.find('a');
    this.cache.$anchorMenu = document.getElementsByClassName('pw-anchor-menu');
    this.cache.$anchorMenuTitleContainer = document.getElementsByClassName('pw-anchor-menu-title-container');
    this.cache.$anchorMenuTitle = document.getElementById('pw-anchor-menu-title');
    this.cache.$progressIndicator = this.root.find('.pw-anchor-menu-progress-indicator');
    this.cache.$anchorMenuContent = this.root.find('.pw-anchor-menu-content');
    this.cache.$anchorMenuContainer = document.querySelector('.pw-anchor-menu__regular');
    this.cache.$offsetTop = this.cache.$anchorMenu[0].offsetTop;
    this.cache.$maxPrimaryLinks = 5;
    this.cache.$isAnchorMenuClicked = false;
    this.cache.$currentIndex = 0;

    //Default selection
    if (this.cache.$progressIndicator && this.cache.$anchorMenuContent) {
      this.cache.$progressIndicator.css('width', `${this.cache.$anchorMenuContent[0].children[0].offsetWidth}px`);
      this.cache.$progressIndicator.css('margin-left',`${this.cache.$anchorMenuContent[0].children[0].offsetLeft}px`);
      this.setAnchorMenuSelection(0);
    }
    this.getMaxNumberOfPrimaryLinks();
    if (document.querySelector('.pw-anchor-menu__regular') && isDesktopScreenMode()) {
      const container = this.cache.$anchorMenuContainer;
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
          this.onMoreButtonClick(container, moreBtn, secondary);
        });

        const onWindowResize = () => {
          const hiddenItems = [];
          primaryItems.forEach((item) => {
            item.classList.remove('hiddenItem');
          });
          secondaryItems.forEach((item) => {
            item.classList.remove('hiddenItem');
          });

          primaryItems.forEach((item, i) => {
            if(i > this.cache.$maxPrimaryLinks - 1) {
              item.classList.add('hiddenItem');
              hiddenItems.push(i);
            }
          });
          // toggle the visibility of More button and items in Secondary
          this.toggleVisibilityOfItems(hiddenItems, moreLi, moreBtn, secondaryItems);
        };
        onWindowResize();
        window.addEventListener('resize', () => {
          this.resetOverflowMenu();
          this.getMaxNumberOfPrimaryLinks();
          this.setAnchorMenuSelection(this.cache.$currentIndex);
          onWindowResize();
        });

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
    } else {
      // For mobile view, hide anchor menu list item
      this.cache.$anchorMenuContainer.classList.add('collapsed');
    }
  }

  toggleVisibilityOfItems(hiddenItems, moreLi, moreBtn, secondaryItems) {
    const { $anchorMenuContainer } = this.cache;
    if(!hiddenItems.length) {
      moreLi.classList.add('hiddenItem');
      $anchorMenuContainer.classList.remove('show-secondary');
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
  }

  onMoreButtonClick(container, moreBtn, secondary) {
    container.classList.toggle('show-secondary');
    moreBtn.setAttribute('aria-expanded', container.classList.contains('show-secondary'));
    //Set center position for secondary list
    secondary.style.left = `${moreBtn.offsetLeft - (secondary.offsetWidth/2)}px`;
  }

  getMaxNumberOfPrimaryLinks() {
    if(window.outerWidth > 1023 && window.outerWidth < 1280 ) {
      this.cache.$maxPrimaryLinks = 5;
    } else {
      this.cache.$maxPrimaryLinks = 7;
    }
  }

  resetOverflowMenu() {
    const { $anchorMenuContainer } = this.cache;
    if ($anchorMenuContainer && $anchorMenuContainer.classList.contains('show-secondary')) {
      $anchorMenuContainer.classList.remove('show-secondary');
      $anchorMenuContainer.setAttribute('aria-expanded', false);
    }
  }

  bindEvents() {
    const { $anchorLink, $anchorMenu } = this.cache;
    $anchorLink.on('click', this.scrollToSection);
    if($anchorMenu) {
      const doc = document.getElementsByClassName('anchorv2');
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
    const { $anchorMenuTitleContainer, $anchorMenuContainer } = this.cache;
    const $this = $(e.target);
    if (!$this[0].classList.contains('pw-anchor-menu-title-container') && $this[0].id !== 'pw-anchor-menu-title' && !$this[0].classList.contains('icon')) {
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

      this.resetOverflowMenu();

      //Set selection
      this.cache.$isAnchorMenuClicked = true;
      this.cache.$currentIndex = currentIndex;
      this.setAnchorMenuSelection(currentIndex);
      scrollToElement(() => {
        this.cache.$isAnchorMenuClicked = false;
      },`#${anchorId}`);
    } else if (!$anchorMenuTitleContainer[0].classList.contains('active')) {
    //Handle anchor menu title click
      $anchorMenuTitleContainer[0].classList.add('active');
      $anchorMenuTitleContainer[0].parentElement.classList.add('active');
      $anchorMenuContainer.classList.remove('collapsed');
    } else {
      $anchorMenuTitleContainer[0].classList.remove('active');
      $anchorMenuTitleContainer[0].parentElement.classList.remove('active');
      $anchorMenuContainer.classList.add('collapsed');
    }
  };

  setAnchorMenuSelection(currentIndex) {
    const { $progressIndicator, $anchorMenuContent, $maxPrimaryLinks, $secondaryAnchorMenuContent, $anchorMenuTitle, $anchorMenuTitleContainer, $anchorMenuContainer   } = this.cache;
    if ($anchorMenuTitle) {
      $anchorMenuTitle.textContent = $anchorMenuContent[currentIndex].innerText;
      if (!isDesktopScreenMode()){
        $anchorMenuTitleContainer[0].classList.remove('active');
        $anchorMenuTitleContainer[0].parentElement.classList.remove('active');
        $anchorMenuContainer.classList.add('collapsed');
      }
      else{
        $anchorMenuContainer.classList.remove('collapsed');

      }

    }
    //Progress indicator
    $progressIndicator.css('margin-left', `${$anchorMenuContent[currentIndex].children[0].offsetLeft}px`);
    $progressIndicator.css('width', `${$anchorMenuContent[currentIndex].children[0].offsetWidth}px`);
    for(let index=0; index<$anchorMenuContent.length; index++) {
      //Reset active state
      $anchorMenuContent[index].children[0].classList.remove('active');
      if ($secondaryAnchorMenuContent && isDesktopScreenMode()) {
        $secondaryAnchorMenuContent[index].children[0].classList.remove('active');
      }
      if (currentIndex >= $maxPrimaryLinks && currentIndex === index && isDesktopScreenMode()) {
        $secondaryAnchorMenuContent[index].children[0].classList.add('active');
      } else if (currentIndex === index) {
        $anchorMenuContent[index].children[0].classList.add('active');
      }
    }
  }

  isElementInViewport(element) {
    const rect = element.getBoundingClientRect();
    const windowHeight = (window.innerHeight || document.documentElement.clientHeight);
    const vertInView = (rect.top <= (windowHeight/3)) && ((rect.top + rect.height) >= 0);
    return vertInView;
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Anchorv2;