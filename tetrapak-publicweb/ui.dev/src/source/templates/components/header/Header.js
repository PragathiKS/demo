import $ from 'jquery';
// import { $document, $win } from '../../../scripts/utils/commonSelectors';
// import { loc } from '../../../scripts/common/common';

class Header {
  constructor({ el }) {
    this.root = $(el);
    this.toggleFlag = false;
  }

  cache = {};

  initCache() {
    this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    this.cache.$headerLogoPlaceholder = this.root.find('.js-tp-pw-header-logo-digital-data');
  }

  bindEvents() {
    const { $hamburgerToggle, $headerLogoPlaceholder } = this.cache;
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $headerLogoPlaceholder.on('click', function (e) {
      e.preventDefault();
      const $this = $(this);
      if (window.digitalData) {
        $.extend(window.digitalData, {
          linkClick: {
            linkType: 'internal',
            linkSection: $this.data('link-section'),
            linkParentTitle: $this.data('link-parent-title'),
            linkName: $this.data('link-name')
          }
        });
        if (window._satellite) {
          window._satellite.track('linkClicked');
        }
      }
    });
  }

  openMobileMenuBoxToggle = () => {
    if(!this.toggleFlag){
      this.cache.$mobileMenu.fadeIn(300);
      this.cache.$hamburgerToggle.children('#toggle-button').removeClass('icon-Burger');
      this.cache.$hamburgerToggle.children('#toggle-button').addClass('icon-Close');
      this.toggleFlag = true;
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children('#toggle-button').removeClass('icon-Close');
      this.cache.$hamburgerToggle.children('#toggle-button').addClass('icon-Burger');
      this.toggleFlag = false;
    }
  }

  // setLogoDigitalData = (e) => {
  //   e.preventDefault();
  //   const $this = $(this);
  //   // eslint-disable-next-line no-console
  //   console.log(this,'before linkClicked', this.cache.$headerLogoPlaceholder.data('link-section'), this.cache.$headerLogoPlaceholder.data('link-parent-title'));
  //   if (window.digitalData) {
  //     $.extend(window.digitalData, {
  //       linkClick: {
  //         linkType: 'internal',
  //         linkSection: $this.data('link-section'),
  //         linkParentTitle: $this.data('link-parent-title'),
  //         linkName: $this.data('link-name')
  //       }
  //     });
  //     if (window._satellite) {

  //       // eslint-disable-next-line no-console
  //       console.log('linkClicked');
  //       window._satellite.track('linkClicked');
  //     }
  //   }
  // }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Header;
