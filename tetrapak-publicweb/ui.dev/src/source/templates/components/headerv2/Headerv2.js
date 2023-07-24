import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { HEADER_MIN_MARGIN } from './Headerv2.constants';
import { checkActiveOverlay, isDesktopMode} from '../../../scripts/common/common';

class Headerv2 {
  constructor({ el }) {
    this.root = $(el);
  }

  initCache = () => {
    this.cache = {
      constants: {
        'extraMargin': 0
      },
      variables: {
        'mainNavigationLinksInititalWidth': this.getMainNavigationWidth(),
        /*
          The summed width of all the items in the main navigation menu.
        */
        'nOfElementsInSubmenu': 0,
        /*
          The 'nOfElementsInSubmenu' is equal to the elements in the collapsible menu.
        */
        'mainNavigationLinksWidthList': this.getMainNavigationLinksWidthList(),
        /*
          The 'mainNavigationLinksWidthList' contains each of the main navigation menu's item width.
        */
        'isMegaMenuHovered': false
      },
      $elements: {
        'mainNavigationLinks': this.root.find('.tp-pw-headerv2-main-navigation a'),
        'backdrop': this.root.find('.tp-pw-headerv2__backdrop'),
        'searchIcon': this.root.find('.tp-pw-headerv2-search-box-toggle')
      }
    };
  }

  hideSearchbar = () => {
    $('.js-pw-header-search-barv2').removeClass('show');
  }

  bindSearchIconClickEvent = () => {
    const $searchBar = $('.js-pw-header-search-bar');

    $searchBar.addClass('show');
    this.cache.$elements.searchIcon.on('click', this.searchIconClick);
  }

  searchIconClick = () => {
    const $searchBarWrapper = $('.tp-pw-headerv2-searchbar-wrapper');
    if (!isDesktopMode) {
      if ($searchBarWrapper.hasClass('show')) {
        $searchBarWrapper.removeClass('show');
      } else {
        $('.js-search-bar-input').val('');
        $searchBarWrapper.addClass('show');
        $('.search-bar-input').trigger('focus');

        const dataObj = {
          linkType: 'internal',
          linkSection: 'Hyperlink click',
          linkParentTitle: '',
          linkName: 'Search'
        };
        const eventObj = {
          eventType: 'linkClick',
          event: 'Search'
        };

        trackAnalytics(dataObj, 'linkClick', 'linkClick', undefined, false, eventObj);
      }
    }
    else if (this.cache.$elements.searchIcon.children('i').hasClass('icon-Search_pw')) {
      this.cache.$elements.searchIcon.children('i').removeClass('icon-Search_pw');
      this.cache.$elements.searchIcon.children('i').addClass('icon-Close_pw');
      $('.js-search-bar-input').val('');
      $searchBarWrapper.addClass('show');
      $('.search-bar-input').trigger('focus');
      $('body').css('overflow','hidden');

      const dataObj = {
        linkType: 'internal',
        linkSection: 'Hyperlink click',
        linkParentTitle: '',
        linkName: 'Search'
      };
      const eventObj = {
        eventType: 'linkClick',
        event: 'Search'
      };

      trackAnalytics(dataObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }
    else {
      this.cache.$elements.searchIcon.children('i').addClass('icon-Search_pw');
      this.cache.$elements.searchIcon.children('i').removeClass('icon-Close_pw');
      $searchBarWrapper.removeClass('show');
      const activeOverlay = ['.tp-pw-headerv2-searchbar-wrapper'];
      checkActiveOverlay(activeOverlay);
    }
  }

  getMainNavigationWidth = () => this.root.find('.tp-pw-headerv2-main-navigation').width();
  getMainNavigationLinksWidthList = () => {
    const navLinks = Array.from(this.root.find('.js-tp-pw-headerv2-main-navigation-link'));
    const navLinksWidths = navLinks.map(el => $(el).width());

    return navLinksWidths;
  }

  bindWindowSizeChangeEvent = () => {
    const handleResize = () => {
      const windowWidth = $(window).width();
      const navWidth = $('.tp-pw-headerv2-main-navigation').width();
      const widthDiff = windowWidth - navWidth;
      const navLinkWidths = this.cache.variables['mainNavigationLinksWidthList'];
      const navLinkLastWidthIndex = navLinkWidths.length - this.cache.variables.nOfElementsInSubmenu;
      const lastItemsWidth = navLinkWidths[navLinkLastWidthIndex];
      const minMargin = HEADER_MIN_MARGIN + this.cache.constants.extraMargin;
      const isLargerThanMargin = widthDiff <= minMargin;
      const isLargerThanLastItemWithMargin = this.cache.variables.nOfElementsInSubmenu &&
        (widthDiff > lastItemsWidth + minMargin * 2);

      if (isLargerThanMargin) {
        this.shiftMainNavigationLink();
      }
      else if (isLargerThanLastItemWithMargin) {
        this.unshiftMainNavigationLink();
      }
    };

    let isLargerThanMargin = true;
    let isLargerThanLastItemWithMargin = true;
    while (isLargerThanMargin || isLargerThanLastItemWithMargin) {
      $(window).off('resize');
      $(window).on('resize', handleResize);
      const windowWidth = $(window).width();
      const navWidth = $('.tp-pw-headerv2-main-navigation').width();
      const widthDiff = windowWidth - navWidth;
      const navLinkWidths = this.cache.variables['mainNavigationLinksWidthList'];
      const navLinkLastWidthIndex = navLinkWidths.length - this.cache.variables.nOfElementsInSubmenu;
      const lastItemsWidth = navLinkWidths[navLinkLastWidthIndex];
      isLargerThanMargin = widthDiff <= HEADER_MIN_MARGIN;
      isLargerThanLastItemWithMargin = this.cache.variables.nOfElementsInSubmenu &&
        (widthDiff > lastItemsWidth + HEADER_MIN_MARGIN * 2);
      handleResize();
    }
  }

  shiftMainNavigationLink = () => {
    this.cache.variables.nOfElementsInSubmenu++;
    const lastEl = this.getLastMainNavigationLink();
    const lastElClone = lastEl.clone();
    lastEl.css('display', 'none');
    this.root.find('.tp-pw-headerv2-submenu').prepend(lastElClone);
    this.root.find('.tp-pw-headerv2-submenu-wrapper').toggle(true);
  }

  unshiftMainNavigationLink = () => {
    const lastEl = this.getLastMainNavigationLink();
    lastEl.css('display', 'block');
    this.cache.variables.nOfElementsInSubmenu--;
    this.root.find('.tp-pw-headerv2-submenu a:first').remove();
    if (!this.cache.variables.nOfElementsInSubmenu) {
      this.root.find('.tp-pw-headerv2-submenu-wrapper').toggle(false);
    }
  }

  getLastMainNavigationLink = () => {
    const navLinks = this.cache.$elements['mainNavigationLinks'];
    const lastElIndex = navLinks.length - this.cache.variables.nOfElementsInSubmenu;
    return $(navLinks[lastElIndex]);
  }

  bindSubmenuOpenEvent = () => {
    const $subMenu = this.root.find('.tp-pw-headerv2-submenu');
    const $subMenuIcon = this.root.find('.js-submenu-icon');

    if (!this.cache.variables.nOfElementsInSubmenu) {
      this.root.find('.tp-pw-headerv2-submenu-wrapper').toggle(false);
    }
    $subMenu.toggle(false);

    $subMenuIcon.on('click', () => {
      $subMenu.toggle();
      $subMenuIcon.hasClass('active') ? $subMenuIcon.removeClass('active') : $subMenuIcon.addClass('active');
    });
  }

  bindSubmenuMobileOpenEvent = () => {
    const $subMenu = this.root.find('.tp-pw-headerv2-mobile-secondary-navigation-menu');
    const $subMenuIcon = this.root.find('.js-submenu-mobile-icon');
    $subMenu.toggle(false);
    $subMenuIcon.on('click', () => {
      $subMenu.toggle();
      if ($subMenuIcon.hasClass('icon-Burger_pw')) {
        $subMenuIcon.addClass('icon-Close_pw').removeClass('icon-Burger_pw');
        $('body').css('overflow','hidden');
      } else {
        $subMenuIcon.addClass('icon-Burger_pw').removeClass('icon-Close_pw');
        // check if other overlay is active
        const activeOverlay = ['.tp-pw-headerv2-mobile-secondary-navigation-menu'];
        checkActiveOverlay(activeOverlay);
      }
    });
  }

  bindMarketSelectorOpenEvent = () => {
    this.root.find('.js-header__selected-lang-pw').on('click', (e) => {
      $('.js-lang-modal').trigger('showlanuagepreferencepopup-pw');
      this.trackLanguageSelector(e);
    });
  }

  trackLanguageSelector = () => {
    const trackingObj = {
      linkType: 'internal',
      linkSection: 'Hyperlink click',
      linkParentTitle: '',
      linkName: 'Market Selector'
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'Market Selector'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
  }

  bindMegaMenuLinkHoverEvent = () => {
    const $backdrop = this.cache.$elements.backdrop;
    const linksSelector = '.tp-pw-headerv2-main-navigation > a';
    this.root.find(linksSelector).each(function(index) {
      const getMegaMenuSelector = (megaMenuSelectorIndex) => `.js-megamenu-${megaMenuSelectorIndex}`;
      const getMegaMenuLinkSelector = (megaMenuLinkSelectorIndex) => `.js-megamenulink-${megaMenuLinkSelectorIndex}`;
      const firstRowSelector = `.tp-pw-headerv2-first-row`;
      const allMegaMenuSelector = `.tp-pw-headerv2-megamenu`;
      const $link = $(this);
      const $megaMenu = $(getMegaMenuSelector(index));
      const $firstRow= $(firstRowSelector);
      const $allMegaMenu = $(allMegaMenuSelector);

      const showMegaMenu = (i=index) => {
        $(getMegaMenuLinkSelector(i)).addClass('active');
        $(getMegaMenuSelector(i)).removeClass('hidden');
        $('.tp-pw-headerv2-searchbar-wrapper').removeClass('show');
        $backdrop.removeClass('hidden');
      };

      const hideMegaMenu = (i=index) => {
        $(getMegaMenuLinkSelector(i)).removeClass('active');
        $(getMegaMenuSelector(i)).addClass('hidden');
        $backdrop.addClass('hidden');
      };

      const hideOtherMegaMenus = (hideAll=false) => {
        $allMegaMenu.each(function(megaMenuIndex) {
          if (hideAll) {
            hideMegaMenu(megaMenuIndex);
          } else if (megaMenuIndex !== index) {
            hideMegaMenu(megaMenuIndex);
          }
        });
      };

      $link.on('mouseleave', () => {
        hideOtherMegaMenus(true);
      });

      $link.on('mouseenter', () => {
        hideOtherMegaMenus();
        showMegaMenu();
      });

      $megaMenu.on('mouseenter', () => {
        showMegaMenu();
      });

      $megaMenu.on('mouseleave', () => {
        hideMegaMenu();
      });

      $firstRow.on('mouseenter', () => {
        hideOtherMegaMenus(true);
      });
    });
  }

  setBackdropPosition = () => {
    const headerHeight = this.root.height();
    this.cache.$elements.backdrop.css('top', headerHeight);
  }

  bindEvents = () => {
    this.bindWindowSizeChangeEvent();
    this.bindSubmenuOpenEvent();
    this.bindSubmenuMobileOpenEvent();
    this.bindMegaMenuLinkHoverEvent();
    this.bindMarketSelectorOpenEvent();
    this.setBackdropPosition();
    this.bindSearchIconClickEvent();
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Headerv2;
