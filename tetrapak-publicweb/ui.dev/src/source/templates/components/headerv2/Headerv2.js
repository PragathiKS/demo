import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { HEADER_MIN_MARGIN } from './Headerv2.constants';
import { checkActiveOverlay, isDesktopMode} from '../../../scripts/common/common';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

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
    this.cache.$clickMenuLink = this.root.find('.js-click-menu-link');
    this.cache.$backButton = this.root.find('.tp-pw-headerv2-back-row-back-button');
    this.cache.$closeButton = this.root.find('.tp-pw-headerv2-back-row-close-button');
    //Mobile menu
    this.cache.$subMenu = this.root.find('.tp-pw-headerv2-mobile-secondary-navigation-menu');
    this.cache.$subMenuIcon = this.root.find('.js-submenu-mobile-icon');
    this.cache.megaMenuPaths = [];
  }

  bindSearchIconClickEvent = () => {
    const $searchBar = $('.js-pw-header-search-bar');

    $searchBar.addClass('show');
    this.cache.$elements.searchIcon.on('click', this.searchIconClick);
  }

  searchIconClick = () => {
    const { $subMenu, $subMenuIcon } = this.cache;
    const $searchBarWrapper = $('.tp-pw-headerv2-searchbar-wrapper');
    //Check if submenu page opened
    if ($subMenuIcon.hasClass('icon-Close_pw')) {
      $subMenu.toggle(false);
      $subMenuIcon.addClass('icon-Burgerv3_pw').removeClass('icon-Close_pw');
      const activeOverlay = ['.tp-pw-headerv2-mobile-secondary-navigation-menu'];
      checkActiveOverlay(activeOverlay);
    }

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
    else if (this.cache.$elements.searchIcon.children('i').hasClass('icon-Searchv2_pw')) {
      this.cache.$elements.searchIcon.children('i').removeClass('icon-Searchv2_pw');
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
      //Close search
      $searchBarWrapper.removeClass('show');
      this.cache.$elements.searchIcon.children('i').addClass('icon-Searchv2_pw');
      this.cache.$elements.searchIcon.children('i').removeClass('icon-Close_pw');
      $('body').css('overflow','auto');
    }
  }

  getMainNavigationWidth = () => this.root.find('.tp-pw-headerv2-main-navigation').width();
  getMainNavigationLinksWidthList = () => {
    const navLinks = Array.from(this.root.find('.js-tp-pw-headerv2-main-navigation-link'));
    const navLinksWidths = navLinks.map(el => $(el).width());

    return navLinksWidths;
  }

  bindWindowSizeChangeEvent = () => {
    const { $subMenu, $subMenuIcon } = this.cache;
    const handleResize = () => {
      const windowWidth = $(window).width();
      const navWidth = $('.tp-pw-headerv2-main-navigation').width();
      const widthDiff = windowWidth - navWidth;
      const navLinkWidths = this.cache.variables['mainNavigationLinksWidthList'];
      const navLinkLastWidthIndex = navLinkWidths.length - this.cache.variables.nOfElementsInSubmenu;
      const lastItemsWidth = navLinkWidths[navLinkLastWidthIndex];
      const minMargin = HEADER_MIN_MARGIN + this.cache.constants.extraMargin;
      const isLargerThanMargin = widthDiff <= minMargin;
      const isLargerThanLastItemWithMargin = this.cache.variables.nOfElementsInSubmenu && (widthDiff > lastItemsWidth + minMargin * 2);
      //Check if submenu page opened
      if ($subMenuIcon.hasClass('icon-Close_pw')) {
        $subMenu.toggle(false);
        $subMenuIcon.addClass('icon-Burgerv3_pw').removeClass('icon-Close_pw');
        const activeOverlay = ['.tp-pw-headerv2-mobile-secondary-navigation-menu'];
        checkActiveOverlay(activeOverlay);
      }

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
      const navWidth = $('.js-tp-pw-headerv2-main-navigation').width();
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
      this.openSubmenuEvent();
    });
  }

  openSubmenuEvent = () => {
    const $subMenu = this.root.find('.tp-pw-headerv2-submenu');
    const $subMenuIcon = this.root.find('.js-submenu-icon');
    $subMenu.toggle();
    $subMenuIcon.hasClass('active') ? $subMenuIcon.removeClass('active') : $subMenuIcon.addClass('active');
  }

  bindSubmenuMobileOpenEvent = () => {
    const { $subMenu, $subMenuIcon } = this.cache;
    $subMenu.toggle(false);
    $subMenuIcon.on('click', () => {
      this.openMobileMenu();
    });
  }

  openMobileMenu = () => {
    const { $subMenu, $subMenuIcon } = this.cache;
    //Check if search menu enabled
    if(this.cache.$elements.searchIcon.children('i').hasClass('icon-Close_pw')) {
      const $searchBarWrapper = $('.tp-pw-headerv2-searchbar-wrapper');
      $searchBarWrapper.removeClass('show');
      this.cache.$elements.searchIcon.children('i').addClass('icon-Searchv2_pw');
      this.cache.$elements.searchIcon.children('i').removeClass('icon-Close_pw');
    }

    $subMenu.toggle();
    if ($subMenuIcon.hasClass('icon-Burgerv3_pw')) {
      $subMenuIcon.addClass('icon-Close_pw').removeClass('icon-Burgerv3_pw');
      $('body').css('overflow','hidden');
    } else {
      $subMenuIcon.addClass('icon-Burgerv3_pw').removeClass('icon-Close_pw');
      // check if other overlay is active
      const activeOverlay = ['.tp-pw-headerv2-mobile-secondary-navigation-menu'];
      checkActiveOverlay(activeOverlay);
    }
  }

  bindMarketSelectorOpenEvent = () => {
    this.root.find('.js-header__selected-lang-pw').on('click', (e) => {
      this.openMarketSelector(e);
    });
  }

  openMarketSelector = (e) => {
    $('.js-lang-modal').trigger('showlanuagepreferencepopup-pw');
    this.trackLanguageSelector(e);
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
        $('body').addClass('scrollOff');
      };

      const hideMegaMenu = (i=index) => {
        $(getMegaMenuLinkSelector(i)).removeClass('active');
        $(getMegaMenuSelector(i)).addClass('hidden');
        $backdrop.addClass('hidden');
        $('body').removeClass('scrollOff');
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
        if ($link.attr('href') === undefined) {
          hideOtherMegaMenus();
          showMegaMenu();
        }
      });

      $megaMenu.on('mouseenter', () => {
        if ($megaMenu.attr('href') === undefined) {
          showMegaMenu();
        }
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

  bindMegaMenuLinkMobileClickEvent = () => {
    const { $subMenu, $subMenuIcon, megaMenuPaths } = this.cache;
    const linksSelector = `.tp-pw-headerv2-mobile-secondary-navigation-menu > .primary-link-container > .tp-pw-anchor-container-primary > a`;

    this.root.find(linksSelector).each(function(index) {
      const getMegaMenuSelector = (megaMenuSelectorIndex) => `.tp-pw-megamenu-mobile-subpage-${megaMenuSelectorIndex}`;
      const $link = $(this);
      const allMegaMenuSelector = `.tp-pw-headerv2-megamenu-mobile`;
      const $allMegaMenu = $(allMegaMenuSelector);

      const showMegaMenu = (i=index) => {
        $(getMegaMenuSelector(i)).removeClass('hidden');
        setTimeout(() => {
          $(getMegaMenuSelector(i)).removeClass('is-close').addClass('is-open');
          ajaxWrapper.getXhrObj({
            url: megaMenuPaths[i],
            method: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            beforeSend(jqXHR) {
              jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            showLoader: true
          }).done((data) => {
            const rendererClass = `.tp-pw-meganenu-subpage-wrapper-${i}`;
            render.fn({
              template: 'megaMenuV2Subpage',
              data: data,
              target: rendererClass,
              hidden: false
            });
          });

          // Add event on back button
          $('.tp-pw-headerv2-megamenu-mobile.is-open > .tp-pw-headerv2-back-row > .tp-pw-headerv2-back-row-back-button').on('click', function() {
            $('.tp-pw-headerv2-megamenu-mobile').removeClass('is-open').addClass('is-close');
            setTimeout(() => {
              $('.tp-pw-headerv2-megamenu-mobile').addClass('hidden');
            }, 150);
          });
          // Close mega menu
          $('.tp-pw-headerv2-megamenu-mobile.is-open > .tp-pw-headerv2-back-row > .tp-pw-headerv2-back-row-close-button').on('click', function() {
            $('.tp-pw-headerv2-megamenu-mobile').removeClass('is-open').addClass('is-close');
            setTimeout(() => {
              $('.tp-pw-headerv2-megamenu-mobile').addClass('hidden');
              $subMenu.toggle(false);
              $subMenuIcon.addClass('icon-Burgerv3_pw').removeClass('icon-Close_pw');
              // check if other overlay is active
              const activeOverlay = ['.tp-pw-headerv2-mobile-secondary-navigation-menu'];
              checkActiveOverlay(activeOverlay);
            }, 50);
          });
        }, 20);
      };

      const hideMegaMenu = (i=index) => {
        $(getMegaMenuSelector(i)).removeClass('is-open').addClass('hidden is-close');
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

      $link.on('click', () => {
        if ($link.attr('href') === undefined) {
          hideOtherMegaMenus();
          showMegaMenu();
        }
      });

    });
  }

throttleScroll = function () {
  let currentScroll;
  let currentScrollTop = 0;
  let isScrolling = true;
  const header = $('header');
  $(window).on('scroll', function () {
    if(isScrolling === true) {
      const scrollTop = $(window).scrollTop();
      const headerHeight = header.height();
      currentScrollTop = scrollTop;
      if (currentScroll < currentScrollTop && scrollTop > headerHeight + headerHeight) {
        header.addClass('scrollUp');
        header.removeClass('scrollDown');
      } else if (currentScroll > currentScrollTop && !(scrollTop <= headerHeight)) {
        header.removeClass('scrollUp');
        header.addClass('scrollDown');
      }
      currentScroll = currentScrollTop;
      isScrolling = false;
      setTimeout(() => {
        isScrolling = true;
      }, 500);
    }
  });
};

  bindEvents = () => {
    this.throttleScroll();
    this.bindWindowSizeChangeEvent();
    this.bindSubmenuOpenEvent();
    this.bindSubmenuMobileOpenEvent();
    this.bindMegaMenuLinkHoverEvent();
    this.bindMarketSelectorOpenEvent();
    this.setBackdropPosition();
    this.bindSearchIconClickEvent();
    this.bindMegaMenuLinkMobileClickEvent();
    if(isDesktopMode) {
      const headerWidth = $('.first-row-nav').outerWidth();
      if ($(window).outerWidth() <= 1366) {
        $('.tp-pw-headerv2-megamenu').css('width', headerWidth);
        $('.tp-pw-megamenuconfigv2').css('width', headerWidth);
      } else {
        $('.tp-pw-headerv2-megamenu').css('width', headerWidth - 96);
        $('.tp-pw-megamenuconfigv2').css('width', headerWidth - 96);
      }
    }
  }

  buildMegaMenu = () => {
    const { megaMenuPaths } = this.cache;
    const allMegaMenuSelector = `.tp-pw-megamenuconfigv2`;
    const $allMegaMenu = $(allMegaMenuSelector);
    $allMegaMenu.each(function() {
      megaMenuPaths.push($(this).attr('data-model-json-path'));
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.buildMegaMenu();
   
  }
}

export default Headerv2;